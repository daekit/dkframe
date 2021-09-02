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
*
*		여신 잔액부족이나 오류 발생시 -9999 Return
*
*	SELECT f_CREDIT_LOAN('C', 1, 'GGS','20210101',0) FROM DUAL; -- 여신잔액
*	SELECT f_CREDIT_LOAN('T', 1, 'GGS','20210101',0) FROM DUAL; -- 총여신금액
*
*  I_CD 구분
*  I_CLNT_CD 거래처 
*  I_CO_CD 회사
*  I_TR_DT 기준일자( 매출일자(P)/수금일자(M)/조회일자(C) )
*                                 제품그룹  PRDTGRP1:철재류, PRDTGRP2:건재류
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

/*----------------------------------------------------------------------------*/
/* 여신체크 및 총금액만 처리 그외 오류처리 '-7'응답                                                                                                                              */
/* 여신처리는 f_CREDIT_LOAN_PRDTGRP 함수에서 제품그룹별로 처리함                                                                                    */
/* 제품그룹 구분없이 거래처별 여신 총액 및 잔액 조회만 처리합니다                                                                                                               */
/*----------------------------------------------------------------------------*/

	IF I_CD = 'C' OR I_CD = 'T'  THEN
		I_PRDT_GRP  := 'PRDTGRP1'; 
	ELSE
		 return -7;  --여신처리는 f_CREDIT_LOAN_PRDTGRP 에서 처리함
	END IF;
	 
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
--								AND b.PRDT_GRP = I_PRDT_GRP
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
--										AND b.PRDT_GRP = I_PRDT_GRP
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
--				  AND PRDT_GRP  = I_PRDT_GRP
				  AND USE_YN    = 'Y';
		exception 
		    when others then O_CO_AMT := 0;
		end;
	/*----------------------------------------------------------------------------*/
	/*  계산서 발행일자 기준 연신금액 계산 END                                                    */
	/*----------------------------------------------------------------------------*/
	
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
				 WHERE a.CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD);
--				   AND a.PRDT_GRP = I_PRDT_GRP;
			exception
			    when others then GRP_BLCE := 0;
    		end;
				
			--거래처 매출금액 누계액 계산
			begin
				SELECT SUM(DECODE(SELPCH_CD,'SELPCH2',nvl(BILG_AMT,0) + nvl(BILG_VAT_AMT,0),0))   -- 청구금액 + 청구부가세금액
				    -  SUM(DECODE(SELPCH_CD,'SELPCH1',nvl(BILG_AMT,0) + nvl(BILG_VAT_AMT,0),0))   -- 지급금액 + 지급부가세금액
					into C_TRSP_AMT
				 FROM TB_AR02M01 a LEFT OUTER JOIN TB_BM01M01 b
				                    ON a.TRST_PRDT_CD = b.PRDT_CD  
				WHERE CO_CD        = NVL(I_CO_CD,CO_CD) 
				  AND TRST_CLNT_CD = I_CLNT_CD
--			      AND SELPCH_CD    = 'SELPCH2'
--			      AND NVL(b.PRDT_GRP,'PRDTGRP1')  = I_PRDT_GRP
				  AND TRST_DT      <= TO_NUMBER(I_TR_DT);
			exception
			    when others then C_TRSP_AMT := 0;
    		end;
				
	
			--거래처별 입급금액 누계액 계산
			begin
			   SELECT sum(DECODE(NVL(CODE_RPRC,'A'),'SELPCH1',0, nvl(D02.ETRDPS_AMT,0)))      /* 수금, 상계,  */
			        - sum(DECODE(NVL(CODE_RPRC,'A'),'SELPCH2',0, nvl(D02.ETRDPS_AMT,0)))      /* 지급, 상계,  */
			           into C_ETRDPS_AMT
				 FROM TB_AR05D02  D02
				 INNER JOIN TB_AR05M01 M01 ON M01.ETRDPS_SEQ = D02.ETRDPS_SEQ
				 INNER JOIN TB_CM05M01 CM05 ON CODE_ID = M01.ETRDPS_TYP 
				WHERE D02.CO_CD      = NVL(I_CO_CD,D02.CO_CD)
				  AND D02.CLNT_CD    = I_CLNT_CD
--				  AND PRDT_GRP       = I_PRDT_GRP
				  AND D02.ETRDPS_DT <= TO_NUMBER(I_TR_DT);
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
	
		O_BLCE_AMT := O_BLCE_TRDT_AMT;
--		O_BLCE_AMT := 100000000000; --임시테스트용 여신 할당급액 부과

		RETURN O_BLCE_AMT;  --거래가능 금액 Return ==> (I_CD = 'C' OR I_CD = 'T') 

	ELSE     --I_CD 예외코드
		RETURN -9997;	/* 처리구분 오류 */
	END IF;  --I_CD END
	      
	return -9998;
	
exception
    when others then
        dbms_output.put_line('exception occurred! (' || sqlcode || ') : ' || sqlerrm);
        return -9999;

END;