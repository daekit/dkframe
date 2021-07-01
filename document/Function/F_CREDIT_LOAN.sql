CREATE OR REPLACE FUNCTION GOLDMOON.F_CREDIT_LOAN(I_CD CHAR,I_CLNT_CD number, I_CO_CD varchar, I_TR_DT VARCHAR, I_AMT number)
    RETURN number is 
    PRAGMA AUTONOMOUS_TRANSACTION;
 
/******************************************************************************
* TYPE			: FUNCTION (Tibero)
* NAME			: F_CREDIT_LOAN
* DEVELOPER		: Jangsub.Nam
* DESCRIPTION	: 입력받은 코드, 거래처번호, 법인코드,  금액, -9999:Exception 발생)
* 	 구분 : 
*         C = 여신 잔액 체크, 잔액  Return
*         T = 총여신금액  Return
*		  M = 여신할당금액증가, 처리금액 --> 매출취소
*		  P = 여신할당금액감소, 처리금액 --> 매출
*		  D = 여신할당금액증가, 처리금액 --> 입금(deposit)
*
*		여신 잔액부족이나 오류 발생시 -9999 Return
*
*	SELECT f_CREDIT_LOAN('C', 1, 'GGS','PRDTGRP1','20210101',0) FROM DUAL; -- 여신잔액
*	SELECT f_CREDIT_LOAN('T', 1, 'GGS','PRDTGRP1','20210101',0) FROM DUAL; -- 총여신금액
*	SELECT f_CREDIT_LOAN('M', 1, 'GGS','PRDTGRP1','20210101', 100000) FROM DUAL; 여신할당금액 100,000원 증가
*	SELECT f_CREDIT_LOAN('P', 1, 'GGS','PRDTGRP1','20210101', 100000) FROM DUAL; 여신할당금액 100,000원 감소
*
*  I_CD 구분
*  I_CLNT_CD 거래처 
*  I_CO_CD 회사
*  I_TR_DT 기준일자( 매출일자(P)/수금일자(M)/조회일자(C) )
*  I_PRDT_GRP 제품그룹  PRDTGRP1:철재류, PRDTGRP2:건재류
*  I_AMT  금액
******************************************************************************/

    I_PRDT_GRP   VARCHAR(20) := 'PRDTGRP1';   
    O_BLCE_TRDT_AMT 	number := 0;	/*거래일기준 잔액 계산 */  
    O_BLCE_CURR_AMT 	number := 0;	/*현재일기준 잔액 계산 */  
    O_BLCE_AMT 	number := 0;	/*가용 잔액 */  
	O_GRP_AMT 	number := 0;	/*그룹 한도 금액*/
    O_CO_AMT 	number := 0;	/*기준일까지사별 한도 금액*/
    O_CO_AMT2 	number := 0;	/*현재일까지사별 한도 금액*/
    
    C_TRSP_AMT 	 number := 0;	/*기준일자까지의 사별 매출금액 집계*/
    C_ETRDPS_AMT number := 0;	/*기준일자까지의 사별 입금누계액*/
    C_TRSP_AMT2  number := 0;	/*현재일자까지의 사별 매출금액 집계*/
    C_ETRDPS_AMT2 number := 0;	/*현재일자까지의 사별 입금누계액*/
    
    
    C_GRP_AMT 	number := 0;	/*기준일까지그룹여신 잔여 금액*/
    C_CO_AMT 	number := 0;	/*기준일까지사별여신 잔여 금액*/
    C_GRP_AMT2 	number := 0;	/*현재일까지그룹여신 잔여 금액*/
    C_CO_AMT2 	number := 0;	/*현재일까지사별여신 잔여 금액*/

    BM03_CNT number := 0;	/*BM03 건수*/
    BM04_CNT number := 0;	/*BM04 건수*/
    BM03_AMT number := 0;	/*그룹공통여신 반영금액*/
    BM04_AMT number := 0;	/*사별여신 반영금액*/
	BM04_CELL_CLMN number := 0;
	BM04_CELL_REM number := 0;
	BM04_CELL_TOT number := 0;
	    
    C_CLNT_CD 	number := 0;
    C_LINK_GRP_YN 	VARCHAR(20) := '';
    C_LINK_GRP_CLNT_CD 	number := 0;
    
    REM_AMT 	number := 0;	/* 잔여 금액*/
	BLCE_AMT    number := 0;	/* 공통사용금액*/
    RVS_AMT     number := 0;	/* 취소, 반품시  Minus처리*/
    
    O_GRP_PLDG_AMT number := 0;
    O_GRP_BLCE_AMT number := 0;
    O_CO_PLDG_AMT number := 0;
    O_CO_BLCE_AMT number := 0;
    
    MIN_CO_AMT 	number := 0;	/*가용여신잔액*/
    GRP_BLCE 	number := 0;	/*그룹공통여신잔액*/
    CO_BLCE 	number := 0;	/*사별 그룹공통여신 사용금액*/
    CELL_REM  	number := 0;
	CO_PLDG  	number := 0;
	OVER_GRP_PLDG number := 0;
	OVER_GRP_BLCE  number := 0;
BEGIN
	/**************************************************************
	* -여신 잔액 체크
	*  SELECT f_CREDIT_LOAN('C', 1, 'GGS','20210131',0) FROM DUAL; -- 여신잔액
	*   1. 사별 여신 잔액 산출
	***************************************************************/

	/*----------------------------------------------------------------------------*/
	/*  계산서 발행일자 기준 연신금액 계산 Start                                                  */
	/*----------------------------------------------------------------------------*/
	/* 기본 신용은 마스터금액과 Detail 금액  2곳에 있음
	       	마스터 그룹여신 금액 : TB_BM02M01.BASIS_CDTLN_AMT
	        Detail 금액 : TB_BM02D02.PLDG_AMT * TB_BM02D02.PLDG_RCOGN_RATE / 100
	       거래처 여신금액 관리 테이블 사별여신금액 (담보금액 * 답보인정비율 ) */

	IF I_CD = 'C' OR I_CD = 'T'  THEN
		begin
			SELECT a.CLNT_CD, 
				   SUM(nvl(A.BASIS_CDTLN_AMT,0)) AS C_GRP_AMT,                --그룹공통여신-사용안함
				   SUM(nvl(b.PLDG_AMT*b.PLDG_RCOGN_RATE/100,0)) AS C_CO_AMT,  --그룹공통여신금액
					   MAX(a.LINK_GRP_YN )AS C_LINK_GRP_YN, 
					   MAX(NVL(a.LINK_GRP_CLNT_CD,A.CLNT_CD))AS C_LINK_GRP_CLNT_CD 
		          into C_CLNT_CD, 
		               C_GRP_AMT, 
		               C_CO_AMT, 
		               C_LINK_GRP_YN, 
		               C_LINK_GRP_CLNT_CD 	 
			FROM TB_BM02M01 a LEFT OUTER JOIN TB_BM02D02 b 
								 ON a.CLNT_CD = b.CLNT_CD
								AND b.PLDG_DIV_CD = 'PLDG03'
								AND b.USE_YN = 'Y' 
								AND b.SELPCH_CD = 'SELPCH2'
				  				AND nvl(B.SETUP_DT,00010101) <= TO_NUMBER(I_TR_DT) 
								AND nvl(B.EXPRTN_DT,99999999) >= TO_NUMBER(I_TR_DT) 
								AND b.PRDT_GRP = I_PRDT_GRP
			WHERE a.USE_YN  = 'Y'
			  AND a.CLNT_CD = I_CLNT_CD
			GROUP BY a.CLNT_CD;
		exception
		    when others then
		    	C_CLNT_CD := I_CLNT_CD;
	    		C_GRP_AMT := 0;
	    		C_CO_AMT  := 0;
	    		C_LINK_GRP_YN := 'N';
	    		C_LINK_GRP_CLNT_CD := I_CLNT_CD;
		end;
	
	
		IF C_LINK_GRP_YN = 'Y' THEN  -- 연계 여신관리 대상
	
	       C_GRP_AMT := 0; 
	       C_CO_AMT  := 0;
			
			/* 연계 거래처 금액 산정 --> 기본 신용은 마스터금액과 Detail 금액  2곳에 있음
			       	마스터 그룹여신 금액 : TB_BM02M01.BASIS_CDTLN_AMT
			        Detail 금액 : TB_BM02D02.PLDG_AMT * TB_BM02D02.PLDG_RCOGN_RATE / 100
					*/
			begin
				SELECT NVL(a.LINK_GRP_CLNT_CD, a.CLNT_CD) as C_LINK_GRP_CLNT_CD, 
				       SUM(nvl(A.BASIS_CDTLN_AMT,0)) AS C_GRP_AMT,              --그룹공통여신-사용안함
				       SUM(nvl(b.PLDG_AMT*b.PLDG_RCOGN_RATE/100,0)) AS C_CO_AMT --그룹공통여신금액
		               into C_LINK_GRP_CLNT_CD, C_GRP_AMT, C_CO_AMT
				  FROM TB_BM02M01 a LEFT OUTER JOIN TB_BM02D02 b 
										 ON a.CLNT_CD = b.CLNT_CD
										AND b.PLDG_DIV_CD = 'PLDG03'
										AND b.USE_YN = 'Y' 
										AND b.SELPCH_CD = 'SELPCH2'
										AND nvl(B.SETUP_DT,00010101) <= TO_NUMBER(I_TR_DT) 
										AND nvl(B.EXPRTN_DT,99999999) >= TO_NUMBER(I_TR_DT) 
										AND b.PRDT_GRP = I_PRDT_GRP
				  WHERE a.LINK_GRP_CLNT_CD = C_LINK_GRP_CLNT_CD
				    AND a.LINK_GRP_YN      = 'Y' 
					AND a.USE_YN           = 'Y'
		  		  GROUP BY NVL(a.LINK_GRP_CLNT_CD, a.CLNT_CD);
			exception
			    when others then
		    		C_GRP_AMT := 0;
		    		C_CO_AMT  := 0;
		    		C_LINK_GRP_CLNT_CD := I_CLNT_CD;
			end;
				
		END IF;  --C_LINK_GRP_YN

	--거래처 여신금액 관리 테이블 사별여신금액 (담보금액 * 답보인정비율 )
		begin
	 		SELECT nvl(sum(PLDG_AMT*PLDG_RCOGN_RATE/100),0)
	             into O_CO_AMT
				FROM TB_BM02D02
				WHERE CLNT_CD  = I_CLNT_CD
			      AND CO_CD    = nvl(I_CO_CD,CO_CD)
				  AND PLDG_DIV_CD NOT IN ('PLDG03')
				  AND SELPCH_CD = 'SELPCH2'
				  AND nvl(SETUP_DT,00010101) <= TO_NUMBER(I_TR_DT) 
				  AND nvl(EXPRTN_DT,99999999) >= TO_NUMBER(I_TR_DT)
				  AND PRDT_GRP  = I_PRDT_GRP
				  AND USE_YN    = 'Y';
		exception
		    when others then O_CO_AMT := 0;
		end;

	/*----------------------------------------------------------------------------*/
	/*  계산서 발행일자 기준 연신금액 계산 END                                                    */
	/*----------------------------------------------------------------------------*/
	
	/*----------------------------------------------------------------------------*/
	/*  현재일자 기준 여신금액 계산 Start                                                    */
	/*----------------------------------------------------------------------------*/
	/* 기본 신용은 마스터금액과 Detail 금액  2곳에 있음
	       	마스터 그룹여신 금액 : TB_BM02M01.BASIS_CDTLN_AMT
	        Detail 금액 : TB_BM02D02.PLDG_AMT * TB_BM02D02.PLDG_RCOGN_RATE / 100
	       거래처 여신금액 관리 테이블 사별여신금액 (담보금액 * 답보인정비율 ) */
		begin
			SELECT a.CLNT_CD, 
				   SUM(nvl(A.BASIS_CDTLN_AMT,0)) AS C_GRP_AMT2, 
				   SUM(nvl(b.PLDG_AMT*b.PLDG_RCOGN_RATE/100,0)) AS C_CO_AMT2, 
					   MAX(a.LINK_GRP_YN )AS C_LINK_GRP_YN, 
					   MAX(NVL(a.LINK_GRP_CLNT_CD, A.CLNT_CD))AS C_LINK_GRP_CLNT_CD 
		          into C_CLNT_CD, 
		               C_GRP_AMT2, 
		               C_CO_AMT2, 
		               C_LINK_GRP_YN, 
		               C_LINK_GRP_CLNT_CD 	 
			FROM TB_BM02M01 a LEFT OUTER JOIN TB_BM02D02 b 
								 ON a.CLNT_CD = b.CLNT_CD
								AND b.PLDG_DIV_CD = 'PLDG03'
								AND b.USE_YN = 'Y' 
								AND b.SELPCH_CD = 'SELPCH2'
				  				AND nvl(B.SETUP_DT,00010101) <= TO_CHAR(SYSDATE,'YYYYMMDD')
								AND nvl(B.EXPRTN_DT,99999999) >= TO_CHAR(SYSDATE,'YYYYMMDD') 
								AND b.PRDT_GRP = I_PRDT_GRP
			WHERE a.USE_YN  = 'Y'
			  AND a.CLNT_CD = I_CLNT_CD
			GROUP BY a.CLNT_CD;
		exception
		    when others then
		    	C_CLNT_CD := I_CLNT_CD;
	    		C_GRP_AMT2 := 0;
	    		C_CO_AMT2  := 0;
	    		C_LINK_GRP_YN := 'N';
	    		C_LINK_GRP_CLNT_CD := I_CLNT_CD;
		end;				
	
	
		IF C_LINK_GRP_YN = 'Y' THEN  -- 연계 여신관리 대상
	
	       C_GRP_AMT2 := 0; 
	       C_CO_AMT2  := 0;
			
			/* 연계 거래처 금액 산정 --> 기본 신용은 마스터금액과 Detail 금액  2곳에 있음
			       	마스터 그룹여신 금액 : TB_BM02M01.BASIS_CDTLN_AMT
			        Detail 금액 : TB_BM02D02.PLDG_AMT * TB_BM02D02.PLDG_RCOGN_RATE / 100
					*/
			begin
				SELECT NVL(a.LINK_GRP_CLNT_CD,A.CLNT_CD) as C_LINK_GRP_CLNT_CD, 
				       SUM(nvl(A.BASIS_CDTLN_AMT,0)) AS C_GRP_AMT2, 
				       SUM(nvl(b.PLDG_AMT*b.PLDG_RCOGN_RATE/100,0)) AS C_CO_AMT2 
		               into C_LINK_GRP_CLNT_CD, C_GRP_AMT2, C_CO_AMT2
				  FROM TB_BM02M01 a LEFT OUTER JOIN TB_BM02D02 b 
										 ON a.CLNT_CD = b.CLNT_CD
										AND b.PLDG_DIV_CD = 'PLDG03'
										AND b.USE_YN = 'Y' 
										AND b.SELPCH_CD = 'SELPCH2'
										AND nvl(B.SETUP_DT,00010101) <= TO_CHAR(SYSDATE,'YYYYMMDD') 
										AND nvl(B.EXPRTN_DT,99999999) >= TO_CHAR(SYSDATE,'YYYYMMDD') 
										AND b.PRDT_GRP = I_PRDT_GRP
				  WHERE a.LINK_GRP_CLNT_CD = C_LINK_GRP_CLNT_CD
				    AND a.LINK_GRP_YN      = 'Y' 
					AND a.USE_YN           = 'Y'
		  		  GROUP BY NVL(a.LINK_GRP_CLNT_CD,A.CLNT_CD);
			exception
			    when others then
		    		C_GRP_AMT2 := 0;
		    		C_CO_AMT2  := 0;
		    		C_LINK_GRP_CLNT_CD := I_CLNT_CD;
			end;				
		END IF;  --C_LINK_GRP_YN

		--거래처 여신금액 관리 테이블 사별여신금액 (담보금액 * 답보인정비율 )
		begin
	 		SELECT nvl(sum(PLDG_AMT*PLDG_RCOGN_RATE/100),0)
	             into O_CO_AMT2
				FROM TB_BM02D02
				WHERE CLNT_CD  = I_CLNT_CD
			      AND CO_CD    = nvl(I_CO_CD,CO_CD)
				  AND PLDG_DIV_CD NOT IN ('PLDG03')
				  AND SELPCH_CD = 'SELPCH2'
				  AND nvl(SETUP_DT,00010101) <= TO_CHAR(SYSDATE,'YYYYMMDD')
				  AND nvl(EXPRTN_DT,99999999) >= TO_CHAR(SYSDATE,'YYYYMMDD')
				  AND PRDT_GRP = I_PRDT_GRP
				  AND USE_YN    = 'Y';
		exception
		    when others then O_CO_AMT2 := 0;
		end;

	/*----------------------------------------------------------------------------*/
	/*  현재일자 기준 여신금액 계산 End                                                    */
	/*----------------------------------------------------------------------------*/

	ELSE --매출(P), 매출취소(M), 입금(D)처리시 그룹으로 묶여있는지 체크함
		begin
			SELECT a.CLNT_CD, 
				   a.LINK_GRP_YN AS C_LINK_GRP_YN, 
				   NVL(a.LINK_GRP_CLNT_CD, a.CLNT_CD) AS C_LINK_GRP_CLNT_CD 
		          into C_CLNT_CD, 
		               C_LINK_GRP_YN, 
		               C_LINK_GRP_CLNT_CD 	 
			FROM TB_BM02M01 a 
			WHERE a.USE_YN  = 'Y'
			  AND a.CLNT_CD = I_CLNT_CD;
		exception
	    	when others then
	    		C_CLNT_CD := I_CLNT_CD;
	    		C_LINK_GRP_YN := 'N';
	    		C_LINK_GRP_CLNT_CD := I_CLNT_CD;
		end;
	END IF;
			
	IF I_CD = 'C' OR I_CD = 'T' THEN

	/*----------------------------------------------------------------------------*/
	/*  계산서 발행일자 기준 잔액 계산 Start                                                    */
	/*----------------------------------------------------------------------------*/
		C_TRSP_AMT := 0;	--매출금액 임시 집계
		C_ETRDPS_AMT := 0;	--입금금액 임시 집계
		IF  I_CD = 'C' THEN  -- 여신 총 한도 금액 산정
			--그룹공통여신 가용금액 산정(일부자료 없을수 있음)
			begin
				SELECT NVL(a.GRP_BLCE_AMT,0)
				  INTO GRP_BLCE			--그룹공통여신잔액
				  FROM TB_BM03M01 a 
				 WHERE a.CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
				   AND a.PRDT_GRP = I_PRDT_GRP;
			exception
			    when others then GRP_BLCE := 0;
    		end;
				
			--거래처 매출금액 누계액 계산
			begin
				SELECT SUM(nvl(BILG_AMT,0)) + SUM(nvl(BILG_VAT_AMT,0)) as C_TRSP_AMT  -- 청구금액 + 청구부가세금액
					into C_TRSP_AMT
				 FROM TB_AR02M01 a LEFT OUTER JOIN TB_BM01M01 b
				                    ON a.TRST_PRDT_CD = b.PRDT_CD  
				WHERE CO_CD        = NVL(I_CO_CD,CO_CD) 
				  AND TRST_CLNT_CD = I_CLNT_CD
			      AND SELPCH_CD    = 'SELPCH2'
			      AND NVL(b.PRDT_GRP,'PRDTGRP1')  = I_PRDT_GRP
				  AND TRST_DT      <= TO_NUMBER(I_TR_DT);
			exception
			    when others then C_TRSP_AMT := 0;
    		end;
				
	
			--거래처별 입급금액 누계액 계산
			begin
/*AR05D02로 변경 start
			   SELECT sum(nvl(ETRDPS_AMT,0)) into C_ETRDPS_AMT
				 FROM TB_AR05M01
				WHERE CO_CD      = NVL(I_CO_CD,CO_CD)
				  AND CLNT_CD    = I_CLNT_CD
				  AND ETRDPS_TYP IN ('ETRDPS01', 'ETRDPS03', 'ETRDPS05') 	--지급, 상계   ETRDPS01=수금, ETRDPS02=지급, ETRDPS03=상계, ETRDPS05=지급보증보전
				  AND ETRDPS_DT <= TO_NUMBER(I_TR_DT);
AR05D02로 변경 End */
			   SELECT sum(nvl(ETRDPS_AMT,0)) into C_ETRDPS_AMT
				 FROM TB_AR05D02
				WHERE CO_CD      = NVL(I_CO_CD,CO_CD)
				  AND CLNT_CD    = I_CLNT_CD
				  AND PRDT_GRP   = I_PRDT_GRP
				  AND ETRDPS_DT <= TO_NUMBER(I_TR_DT);
			exception
			    when others then C_ETRDPS_AMT := 0;
    		end;
		END IF;

		--사별담보여신 잔액이 남아 있는지 확인 (거래처헤더 여신금액 +거래처신용담보금액+사별기타담보금액담 - 매출금액 + 입금금액)
		O_BLCE_TRDT_AMT := 0;
		IF I_CD = 'T' OR NVL(C_LINK_GRP_CLNT_CD,0) =  I_CLNT_CD THEN
			O_BLCE_TRDT_AMT := nvl(C_GRP_AMT,0) + nvl(C_CO_AMT,0) + nvl(O_CO_AMT,0) - nvl(C_TRSP_AMT,0) + nvl(C_ETRDPS_AMT,0);
		ELSE
			O_BLCE_TRDT_AMT := nvl(C_GRP_AMT,0) + nvl(GRP_BLCE,0) + nvl(O_CO_AMT,0) - nvl(C_TRSP_AMT,0) + nvl(C_ETRDPS_AMT,0);
		END IF;
	/*----------------------------------------------------------------------------*/
	/*  계산서 발행일자 기준 잔액 계산 End                                                    */
	/*----------------------------------------------------------------------------*/

	/*----------------------------------------------------------------------------*/
	/*  현재일자 기준 잔액 계산 Start                                                    */
	/*----------------------------------------------------------------------------*/
		C_TRSP_AMT := 0;	--매출금액 임시 집계
		C_ETRDPS_AMT := 0;	--입금금액 임시 집계
		IF  I_CD = 'C' THEN  -- 여신 총 한도 금액 산정
			--거래처 매출금액 누계액 계산
			begin
				SELECT SUM(nvl(BILG_AMT,0)) + SUM(nvl(BILG_VAT_AMT,0)) as C_TRSP_AMT  -- 청구금액 + 청구부가세금액
					into C_TRSP_AMT2
				 FROM TB_AR02M01 a LEFT OUTER JOIN TB_BM01M01 b
				                    ON a.TRST_PRDT_CD = b.PRDT_CD  
				WHERE CO_CD        = NVL(I_CO_CD,CO_CD) 
				  AND TRST_CLNT_CD = I_CLNT_CD
			      AND SELPCH_CD    = 'SELPCH2'
			      AND NVL(b.PRDT_GRP,'PRDTGRP1') = I_PRDT_GRP
				  AND TRST_DT      <= TO_CHAR(SYSDATE,'YYYYMMDD');
			exception
			    when others then C_TRSP_AMT2 := 0;
    		end;
		
			--거래처별 입급금액 누계액 계산
			begin
/*AR05D02로 변경 start
				SELECT sum(nvl(ETRDPS_AMT,0)) into C_ETRDPS_AMT2
				 FROM TB_AR05M01
				WHERE CO_CD      = NVL(I_CO_CD,CO_CD)
				  AND CLNT_CD    = I_CLNT_CD
				  AND ETRDPS_TYP IN ('ETRDPS01', 'ETRDPS03', 'ETRDPS05') 	--지급, 상계   ETRDPS01=수금, ETRDPS02=지급, ETRDPS03=상계, ETRDPS05=지급보증보전
				  AND ETRDPS_DT <= TO_CHAR(SYSDATE,'YYYYMMDD');
AR05D02로 변경 End */
				SELECT sum(nvl(ETRDPS_AMT,0)) into C_ETRDPS_AMT2
				 FROM TB_AR05D02
				WHERE CO_CD      = NVL(I_CO_CD,CO_CD)
				  AND CLNT_CD    = I_CLNT_CD
				  AND PRDT_GRP   = I_PRDT_GRP
				  AND ETRDPS_DT <= TO_CHAR(SYSDATE,'YYYYMMDD');
			exception
			    when others then C_ETRDPS_AMT2 := 0;
    		end;
		END IF;

		--사별담보여신 잔액이 남아 있는지 확인 (거래처헤더 여신금액 +거래처신용담보금액+사별기타담보금액담 - 매출금액 + 입금금액)
		O_BLCE_CURR_AMT := 0;
		IF I_CD = 'T' OR NVL(C_LINK_GRP_CLNT_CD,0) =  I_CLNT_CD THEN
			O_BLCE_CURR_AMT := nvl(C_GRP_AMT2,0) + nvl(C_CO_AMT2,0) + nvl(O_CO_AMT2,0) - nvl(C_TRSP_AMT2,0) + nvl(C_ETRDPS_AMT2,0);
		ELSE
			O_BLCE_CURR_AMT := nvl(C_GRP_AMT2,0) + nvl(GRP_BLCE,0) + nvl(O_CO_AMT2,0) - nvl(C_TRSP_AMT2,0) + nvl(C_ETRDPS_AMT2,0);
		END IF;
	/*----------------------------------------------------------------------------*/
	/*  현재일자 기준 잔액 계산 End                                                    */
	/*----------------------------------------------------------------------------*/
	
		/* 현재일 기준 잔액(O_BLCE_CURR_AMT)과 거래일자 잔액(O_BLCE_TRDT_AMT)중 작은금액으로 여신잔액 처리  */
		IF O_BLCE_TRDT_AMT > O_BLCE_CURR_AMT THEN
			O_BLCE_AMT := O_BLCE_CURR_AMT;
		ELSE
			O_BLCE_AMT := O_BLCE_TRDT_AMT;
		END IF;
		
--		O_BLCE_AMT := 100000000000; --임시테스트용 여신 할당급액 부과

		RETURN O_BLCE_AMT;  --거래가능 금액 Return ==> (I_CD = 'C' OR I_CD = 'T') 

/**************************************************************
* 매출발생시 (여신 차감)
*   SELECT f_CREDIT_LOAN('P', 1, 'GGS', '20210203', 100000) FROM DUAL; 여신할당금액 100,000원 증가
*   1. 사별 매출 누게금액
*      - 첫째. 그룹 공통여신금액을 차감하고 
*      - 둘째. 그룹여신 차감 잔액 발생시 사별여신금액 차감한다. 
***************************************************************/		
	ELSIF I_CD = 'P' THEN --매출발생처리
	  BEGIN

/*		-- 총여신금액 = 그룹여신금액 + 사별 여신금액
		O_BLCE_TRDT_AMT := C_CO_AMT + NVL(O_CO_AMT,0);   	--기준일까지 잔액
		O_BLCE_CURR_AMT := C_CO_AMT2 + NVL(O_CO_AMT,0);		-- 현재일까지 잔액

	  	-- 매출일자기준여신잔액(O_BLCE_TRDT_AMT) > 현재일 기준 여신잔액(O_BLCE_CURR_AMT)
	  	-- 두개중 작은값을 취함
		IF O_BLCE_TRDT_AMT > O_BLCE_CURR_AMT THEN  --사용가능 여신 금액 산출
			MIN_CO_AMT := O_BLCE_CURR_AMT;
		ELSE
			MIN_CO_AMT := O_BLCE_TRDT_AMT;
		END IF;

	  	-- 가용여신잔액(MIN_CO_AMT) > 신규매출금액(I_AMT)
		IF MIN_CO_AMT < I_AMT THEN	--그룹여신금액 < 매출금액  : 여신잔액이 부족하면 오류!!!
			--여신잔액이 부족합니다.!!!!
			RETURN -1;
		ELSE --여신잔액이 사용 가능하면
*/
			--그룹공통여신 가용금액 산정
			begin
				SELECT NVL(a.GRP_BLCE_AMT,0)  --공통여신 잔액 확인
				  INTO GRP_BLCE
				  FROM TB_BM03M01 a
				 WHERE a.CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
				   AND a.PRDT_GRP = I_PRDT_GRP;
			exception
			    when others then GRP_BLCE := 0;
    		end;
			 
			--사별여신 가용금액 산정
			begin
				SELECT NVL(b.GRP_BLCE_AMT,0),	--그룹 여신에서 사용한 금액 누적
				       NVL(b.CELL_REM_AMT,0),   --사별 매출잔액
				       NVL(b.CO_BLCE_AMT,0)     --사별 여신 잔액
				  INTO CO_BLCE,		--각사별 그룹공통여신 사용금액
				  	   CELL_REM, 	--매출잔액 (매출-수금)
				  	   BLCE_AMT 	--
				  FROM TB_BM04M01 b
				 WHERE b.CLNT_CD  = I_CLNT_CD
	               AND b.CO_CD    = I_CO_CD
	               AND b.PRDT_GRP = I_PRDT_GRP;
			exception
			    when others then
			    	CO_BLCE := 0;
			    	CELL_REM := 0;
			    	BLCE_AMT := 0;
    		end;
			
			--그룹공통여신 가용금액 산정
			IF (NVL(BLCE_AMT,0)) > I_AMT THEN
				BM04_AMT := I_AMT;
				BM03_AMT := 0;
			ELSE
				BM04_AMT := BLCE_AMT;
				BM03_AMT := I_AMT- BM04_AMT;
			END IF;

			SELECT COUNT(*) INTO BM03_CNT FROM TB_BM03M01 
			 WHERE CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
			   AND PRDT_GRP = I_PRDT_GRP;
	
			IF BM03_CNT > 0 THEN
				UPDATE TB_BM03M01
				   SET GRP_BLCE_AMT    = NVL(GRP_BLCE_AMT,0) - BM03_AMT	--여신잔액
				 WHERE CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
				   AND PRDT_GRP = I_PRDT_GRP;
			ELSE

				INSERT INTO TB_BM03M01
					(CLNT_CD, PRDT_GRP, GRP_PLDG_AMT, GRP_BLCE_AMT, USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
				VALUES
					(NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD), I_PRDT_GRP, 0, (0-BM03_AMT), 'Y', 'DB_TRIGER', 'F_CREDIT_LOAN', SYSDATE);
			END IF;
				
			SELECT COUNT(*) INTO BM04_CNT FROM TB_BM04M01 
			 WHERE CLNT_CD  = I_CLNT_CD 
	           AND CO_CD    = I_CO_CD
	           AND PRDT_GRP = I_PRDT_GRP;

			IF BM04_CNT > 0 THEN
				UPDATE TB_BM04M01
				   SET CO_BLCE_AMT    = nvl(CO_BLCE_AMT,0) - BM04_AMT		--여신잔액
				      ,CELL_TOT_AMT   = nvl(CELL_TOT_AMT,0) + I_AMT			--매출금액
				      ,CELL_REM_AMT   = nvl(CELL_REM_AMT,0) + BM04_AMT		--매출잔액
				      ,GRP_BLCE_AMT   = nvl(GRP_BLCE_AMT,0) + BM03_AMT		--그룹여신사용금액
				      ,LAST_CELL_DTTM = I_TR_DT						--최종매출일자	
				 WHERE CLNT_CD = I_CLNT_CD
				   AND CO_CD   = I_CO_CD
		           AND PRDT_GRP = I_PRDT_GRP;
			ELSE
				INSERT INTO TB_BM04M01
					(CLNT_CD, CO_CD, PRDT_GRP,
					CO_PLDG_AMT, CO_BLCE_AMT, 
					CELL_TOT_AMT, CELL_CLMN_AMT, 
					PCHS_TOT_AMT, PCHS_CLMN_AMT, 
					CELL_REM_AMT, GRP_BLCE_AMT,
					LAST_CELL_DTTM,
					USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
				VALUES
				   (I_CLNT_CD, I_CO_CD, I_PRDT_GRP,
					0, (0-BM04_AMT),
					I_AMT, 0, 
					0, 0, 
					BM04_AMT, BM03_AMT,
					I_TR_DT,
					'Y', 'DB_TRIGER', 'F_CREDIT_LOAN', SYSDATE);
			END IF;
	
			   
--		END IF;

 	  EXCEPTION WHEN OTHERS THEN RETURN -8;
 	  END;
		commit;	
		
		RETURN 0;	/*처리완료==>I_CD = 'P' THEN --매출발생처리*/


/**************************************************************
* 수금 취소처리 시
*	SELECT f_CREDIT_LOAN('D', 1, 'GGS', '20210204',-100000) FROM DUAL; 여신할당금액 100,000원 감소
*   1. 사별여신 금액을 먼저 차감하고
*   2. 그룹여신 금액만큼 차감함.  만약에 그룸여신오버금액은 사별여신에 반영
***************************************************************/		
	ELSIF I_CD = 'D' AND I_AMT < 0 THEN --D:수금 취소  처리시 FLAG 수금취소는 금액이 Minus로 처리됨
	  begin			

		  begin			
			SELECT NVL(b.GRP_BLCE_AMT,0),
			       NVL(b.CELL_REM_AMT,0),
			       NVL(b.CO_BLCE_AMT,0),
			       NVL(b.CO_PLDG_AMT,0)
			  INTO GRP_BLCE,		--각사별 그룹공통여신 사용금액
			  	   CELL_REM, 		--매출잔액 (매출-수금)
			  	   CO_BLCE,
			  	   CO_PLDG
			  FROM TB_BM04M01 B
			 WHERE B.CLNT_CD  = I_CLNT_CD
	           AND B.CO_CD    = I_CO_CD
	           AND B.PRDT_GRP = I_PRDT_GRP;
		exception
		    when others then
		    	GRP_BLCE := 0;
		    	CELL_REM := 0;
		    	CO_BLCE := 0;
		    	CO_PLDG := 0;
		end;

		--수금취소  마이너스 금액으로 수금 발생
		RVS_AMT := I_AMT * -1;

		--1. 사별여신금액에서 차감후 공통여신에서차감처리
		IF  NVL(CO_BLCE,0) > RVS_AMT THEN
			BM04_AMT := RVS_AMT;
			BM03_AMT := 0;
		ELSE
			BM04_AMT := CO_BLCE;
			BM03_AMT := RVS_AMT - CO_BLCE;
--*********************************************************************
			begin
				SELECT GRP_PLDG_AMT, GRP_BLCE_AMT
				  INTO OVER_GRP_PLDG, OVER_GRP_BLCE FROM TB_BM03M01 
				 WHERE CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD);
			exception
			    when others then
			    	OVER_GRP_PLDG := 0;
			    	OVER_GRP_BLCE := 0;
			end;
			--임금취소잔액중 공통차감금액이 부족하면 나머지는 개별금액에 반영함
			IF NVL(OVER_GRP_BLCE,0) < BM03_AMT THEN
			-- 03금액은 한도까지만 적용하고 나머지는 04금액으로 전환 처리 필요합니다.
				BM03_AMT := NVL(OVER_GRP_BLCE,0);
				BM04_AMT := RVS_AMT - BM03_AMT;
			END IF;
	--*********************************************************************
		END IF;

		/* 수금처리시 사별 여신할당 금액만큼 공제후 그룹여신금액 차감함  */ 
		SELECT COUNT(*) INTO BM04_CNT FROM TB_BM04M01 
		 WHERE CLNT_CD = I_CLNT_CD 
           AND CO_CD   = I_CO_CD
           AND PRDT_GRP = I_PRDT_GRP;

		IF BM04_CNT > 0 THEN
			UPDATE TB_BM04M01
			   SET CO_BLCE_AMT    = NVL(CO_BLCE_AMT,0) - BM04_AMT	--여신잔액
				  ,CELL_REM_AMT   = NVL(CELL_REM_AMT,0) + BM04_AMT	--외상매출잔액
				  ,CELL_CLMN_AMT  = NVL(CELL_CLMN_AMT,0) - RVS_AMT	--수금금액
				  ,GRP_BLCE_AMT   = NVL(GRP_BLCE_AMT,0) + BM03_AMT	--그룹여신할당금액
				  ,LAST_CLMN_DTTM = I_TR_DT							--최종수금일자	
			 WHERE CLNT_CD  = I_CLNT_CD
			   AND CO_CD    = I_CO_CD
			   AND PRDT_GRP = I_PRDT_GRP;
		ELSE
			INSERT INTO TB_BM04M01
				(CLNT_CD, CO_CD, PRDT_GRP, 
				CO_PLDG_AMT, CO_BLCE_AMT, 
				CELL_TOT_AMT, CELL_CLMN_AMT, 
				PCHS_TOT_AMT, PCHS_CLMN_AMT, 
				CELL_REM_AMT, GRP_BLCE_AMT,
				LAST_CELL_DTTM,
				USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
			VALUES
			   (I_CLNT_CD, I_CO_CD, I_PRDT_GRP, 
				0, (0-BM04_AMT),
				0, (0-RVS_AMT), 
				0, 0, 
				BM04_AMT, BM03_AMT,
				I_TR_DT,
				'Y', 'DB_TRIGER', 'F_CREDIT_LOAN', SYSDATE);
		END IF;
   

		SELECT COUNT(*) INTO BM03_CNT FROM TB_BM03M01 
		 WHERE CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
		   AND PRDT_GRP = I_PRDT_GRP;

		IF BM03_CNT > 0 THEN
			UPDATE TB_BM03M01
			   SET GRP_BLCE_AMT   = NVL(GRP_BLCE_AMT,0) - BM03_AMT		--여신잔액
			 WHERE CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
		       AND PRDT_GRP = I_PRDT_GRP;
		ELSE

			INSERT INTO TB_BM03M01
				(CLNT_CD, PRDT_GRP, GRP_PLDG_AMT, GRP_BLCE_AMT, USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
			VALUES
				(NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD), I_PRDT_GRP, 0, (0-BM03_AMT), 'Y', 'DB_TRIGER', 'F_CREDIT_LOAN', SYSDATE);
		END IF;
			
	
		exception when others then return -9;
		
	  end;	
		commit;	
			
		RETURN 0;	/*처리완료==>I_CD = 'D' AND I_AMT < 0 THEN --D:수금 취소  처리시 FLAG 수금취소는 금액이 Minus로 처리됨*/

/**************************************************************
* 수금처리 시
*	SELECT f_CREDIT_LOAN('M', 1, 'GGS', '20210204',100000) FROM DUAL; 매출취소 여신할당금액 100,000원 증가
*	SELECT f_CREDIT_LOAN('D', 1, 'GGS', '20210204',100000) FROM DUAL; 입금 여신할당금액 100,000원 증가
*   1. 그룹 여신 금액을 먼저 복구하고
*   2. 개별여신 복구 처리한다.
***************************************************************/		
	ELSIF I_CD = 'M' OR I_CD = 'D' THEN --M:매출취소, D:수금 금액 처리시 FLAG
	  begin			
		  begin			
				SELECT NVL(b.GRP_BLCE_AMT,0),
				       NVL(b.CELL_REM_AMT,0),
				       NVL(b.CO_BLCE_AMT,0),
				       NVL(b.CO_PLDG_AMT,0)
				  INTO GRP_BLCE,		--각사별 그룹공통여신 사용금액
				  	   CELL_REM, 		--매출잔액 (매출-수금)
				  	   CO_BLCE,
				  	   CO_PLDG
				  FROM TB_BM04M01 B
				 WHERE B.CLNT_CD  = I_CLNT_CD
		           AND B.CO_CD    = I_CO_CD
		           AND B.PRDT_GRP = I_PRDT_GRP;
			exception
			    when others then
			    	GRP_BLCE := 0;
			    	CELL_REM := 0;
			    	CO_BLCE := 0;
			    	CO_PLDG := 0;
			end;
	
		--공통여신사용금액(GRP_BLCE) > 수금금액(I_AMT)
		IF NVL(GRP_BLCE,0) > I_AMT THEN
			BM03_AMT := I_AMT;
			BM04_AMT := 0;
		ELSE
			BM03_AMT := GRP_BLCE;
			BM04_AMT := I_AMT - GRP_BLCE;
	END IF;

		IF  I_CD = 'D' THEN  --임금
			BM04_CELL_CLMN := I_AMT;
			BM04_CELL_REM  := BM04_AMT;
			BM04_CELL_TOT  := 0;
		ELSE --매출취소
			BM04_CELL_CLMN := 0;
			BM04_CELL_REM  := BM04_AMT;
			BM04_CELL_TOT  := I_AMT;
		END IF;

--*********************************************************************
		begin
			SELECT GRP_PLDG_AMT INTO OVER_GRP_PLDG FROM TB_BM03M01 
			 WHERE CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
		       AND PRDT_GRP = I_PRDT_GRP;
		exception
		    when others then OVER_GRP_PLDG := 0;
		end;

		IF NVL(OVER_GRP_PLDG,0) < BM03_AMT THEN
		-- 03금액은 한도까지만 적용하고 나머지는 04금액으로 전환 처리 필요합니다.
			BM04_AMT := BM04_AMT + BM03_AMT - NVL(OVER_GRP_PLDG,0);
			BM03_AMT := NVL(OVER_GRP_PLDG,0);
		END IF;
--*********************************************************************
		
		/* 수금처리시 사별 여신할당 금액만큼 공제후 그룹여신금액 차감함  */ 
		SELECT COUNT(*) INTO BM04_CNT FROM TB_BM04M01 
		 WHERE CLNT_CD = I_CLNT_CD 
           AND CO_CD   = I_CO_CD
		   AND PRDT_GRP = I_PRDT_GRP;

		IF BM04_CNT > 0 THEN
			UPDATE TB_BM04M01
			   SET CO_BLCE_AMT    = NVL(CO_BLCE_AMT,0) + BM04_AMT			--여신잔액
				  ,CELL_REM_AMT   = NVL(CELL_REM_AMT,0) - BM04_CELL_REM		--외상매출잔액
				  ,CELL_TOT_AMT   = NVL(CELL_TOT_AMT,0) - BM04_CELL_TOT		--수금금액
				  ,CELL_CLMN_AMT  = NVL(CELL_CLMN_AMT,0) + BM04_CELL_CLMN	--수금금액
				  ,GRP_BLCE_AMT   = NVL(GRP_BLCE_AMT,0) - BM03_AMT			--그룹여신할당금액
				  ,LAST_CLMN_DTTM = I_TR_DT									--최종수금일자	
			 WHERE CLNT_CD  = I_CLNT_CD
			   AND CO_CD    = I_CO_CD
			   AND PRDT_GRP = I_PRDT_GRP;
		ELSE
			INSERT INTO TB_BM04M01
				(CLNT_CD, CO_CD, PRDT_GRP, 
				CO_PLDG_AMT, CO_BLCE_AMT, 
				CELL_TOT_AMT, CELL_CLMN_AMT, 
				PCHS_TOT_AMT, PCHS_CLMN_AMT, 
				CELL_REM_AMT, GRP_BLCE_AMT,
				LAST_CELL_DTTM,
				USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
			VALUES
			   (I_CLNT_CD, I_CO_CD, I_PRDT_GRP,
				0, BM04_AMT,
				(0-BM04_CELL_TOT), BM04_CELL_CLMN, 
				0, 0, 
				(0-BM04_CELL_REM), (0-BM03_AMT),
				I_TR_DT,
				'Y', 'DB_TRIGER', 'F_CREDIT_LOAN', SYSDATE);
		END IF;
   

		SELECT COUNT(*) INTO BM03_CNT FROM TB_BM03M01 
		 WHERE CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
		   AND PRDT_GRP = I_PRDT_GRP;

		IF BM03_CNT > 0 THEN
			UPDATE TB_BM03M01
			   SET GRP_BLCE_AMT   = NVL(GRP_BLCE_AMT,0) + BM03_AMT		--여신잔액
			 WHERE CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
		       AND PRDT_GRP = I_PRDT_GRP;
		ELSE

			INSERT INTO TB_BM03M01
				(CLNT_CD, PRDT_GRP, GRP_PLDG_AMT, GRP_BLCE_AMT, USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
			VALUES
				(NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD), I_PRDT_GRP, 0, BM03_AMT, 'Y', 'DB_TRIGER', 'F_CREDIT_LOAN', SYSDATE);
		END IF;
			
	
		exception when others then return -9;
		
	  end;	
		commit;	
			
		RETURN 0;	/*처리완료==> I_CD = 'M' OR I_CD = 'D' THEN --M:매출취소, D:수금 금액 처리시 FLAG*/
				
	ELSE     --I_CD 예외코드
		RETURN 0;	/* 처리구분 오류 */
	END IF;  --I_CD END
	      
	return -9998;
	
exception
    when others then
        dbms_output.put_line('exception occurred! (' || sqlcode || ') : ' || sqlerrm);
        return -9999;

END;