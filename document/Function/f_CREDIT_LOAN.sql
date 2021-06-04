CREATE OR REPLACE FUNCTION GOLDMOON.F_CREDIT_LOAN(I_CD CHAR,I_CLNT_CD number, I_CO_CD varchar, I_TR_DT VARCHAR, I_AMT number)
    RETURN number is 
    PRAGMA AUTONOMOUS_TRANSACTION;

/******************************************************************************
* TYPE			: FUNCTION (Tibero)
* NAME			: F_CREDIT_LOAN
* DEVELOPER		: Jangsub.Nam
* DESCRIPTION	: 입력받은 코드, 거래처번호, 법인코드,  금액, -9999:Exception 발생)
* 	 구분 : C = 여신 잔액 체크, 잔액  Return
*		  M = 여신할당금액증가, 처리금액 --> 입금
*		  P = 여신할당금액감소, 처리금액 --> 매출
*
*		여신 잔액부족이나 오류 발생시 -9999 Return
*
*	SELECT f_CREDIT_LOAN('C', 1, 'GGS','20210101',0) FROM DUAL; -- 여신잔액
*	SELECT f_CREDIT_LOAN('T', 1, 'GGS','20210101',0) FROM DUAL; -- 총여신금액
*	SELECT f_CREDIT_LOAN('M', 1, 'GGS','20210101', 100000) FROM DUAL; 여신할당금액 100,000원 증가
*	SELECT f_CREDIT_LOAN('P', 1, 'GGS','20210101', 100000) FROM DUAL; 여신할당금액 100,000원 감소
*
*  I_CD 구분
*  I_CLNT_CD 거래처 
*  I_CO_CD 회사
*  I_TR_DT 기준일자( 매출일자(P)/수금일자(M)/조회일자(C) )
*  I_AMT  금액
******************************************************************************/


    O_BLCE_TRDT_AMT 	number := 0;	/*거래일기준 잔액 계산 */  
    O_BLCE_CURR_AMT 	number := 0;	/*현재일기준 잔액 계산 */  
    O_BLCE_AMT 	number := 0;	/*가용 잔액 */  
	O_GRP_AMT 	number := 0;	/*그룹 한도 금액*/
    O_CO_AMT 	number := 0;	/*사별 한도 금액*/
    
    C_TRSP_AMT 	 number := 0;	/*기준일자까지의 사별 매출금액 집계*/
    C_ETRDPS_AMT number := 0;	/*기준일자까지의 사별 입금누계액*/
    
    
    C_GRP_AMT 	number := 0;	/*그룹여신 잔여 금액*/
    C_CO_AMT 	number := 0;	/*사별여신 잔여 금액*/
    C_GRP_AMT2 	number := 0;	/*그룹여신 잔여 금액*/
    C_CO_AMT2 	number := 0;	/*사별여신 잔여 금액*/

    BM03_CNT number := 0;	/*BM03 건수*/
    BM04_CNT number := 0;	/*BM04 건수*/
    BM03_AMT number := 0;	/*그룹공통여신 반영금액*/
    BM04_AMT number := 0;	/*사별여신 반영금액*/
    
    C_CLNT_CD 	number := 0;
    C_LINK_GRP_YN 	VARCHAR(20) := '';
    C_LINK_GRP_CLNT_CD 	number := 0;
    
    REM_AMT 	number := 0;	/* 잔여 금액*/
    
    O_GRP_PLDG_AMT number := 0;
    O_GRP_BLCE_AMT number := 0;
    O_CO_PLDG_AMT number := 0;
    O_CO_BLCE_AMT number := 0;
    
    MIN_CO_AMT 	number := 0;	/*가용여신잔액*/
    GRP_BLCE 	number := 0;	/*그룹공통여신잔액*/
    CO_BLCE 	number := 0;	/*사별 그룹공통여신 사용금액*/
    CELL_REM  	number := 0;
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

	IF I_CD = 'C' OR I_CD = 'T' OR I_CD = 'P' THEN
		SELECT a.CLNT_CD, 
			   SUM(nvl(A.BASIS_CDTLN_AMT,0)) AS C_GRP_AMT,                --그룹공통여신-사용안함
			   SUM(nvl(b.PLDG_AMT*b.PLDG_RCOGN_RATE/100,0)) AS C_CO_AMT,  --그룹공통여신금액
				   MAX(a.LINK_GRP_YN )AS C_LINK_GRP_YN, 
				   MAX(a.LINK_GRP_CLNT_CD )AS C_LINK_GRP_CLNT_CD 
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
		WHERE a.USE_YN  = 'Y'
		  AND a.CLNT_CD = I_CLNT_CD
		GROUP BY a.CLNT_CD;
	
	
		IF C_LINK_GRP_YN = 'Y' THEN  -- 연계 여신관리 대상
	
	       C_GRP_AMT := 0; 
	       C_CO_AMT  := 0;
			
			/* 연계 거래처 금액 산정 --> 기본 신용은 마스터금액과 Detail 금액  2곳에 있음
			       	마스터 그룹여신 금액 : TB_BM02M01.BASIS_CDTLN_AMT
			        Detail 금액 : TB_BM02D02.PLDG_AMT * TB_BM02D02.PLDG_RCOGN_RATE / 100
					*/
			SELECT a.LINK_GRP_CLNT_CD as C_LINK_GRP_CLNT_CD, 
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
			  WHERE a.LINK_GRP_CLNT_CD = C_LINK_GRP_CLNT_CD
			    AND a.LINK_GRP_YN      = 'Y' 
				AND a.USE_YN           = 'Y'
	  		  GROUP BY a.LINK_GRP_CLNT_CD;
				
		END IF;  --C_LINK_GRP_YN
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
		SELECT a.CLNT_CD, 
			   SUM(nvl(A.BASIS_CDTLN_AMT,0)) AS C_GRP_AMT2, 
			   SUM(nvl(b.PLDG_AMT*b.PLDG_RCOGN_RATE/100,0)) AS C_CO_AMT2, 
				   MAX(a.LINK_GRP_YN )AS C_LINK_GRP_YN, 
				   MAX(a.LINK_GRP_CLNT_CD )AS C_LINK_GRP_CLNT_CD 
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
		WHERE a.USE_YN  = 'Y'
		  AND a.CLNT_CD = I_CLNT_CD
		GROUP BY a.CLNT_CD;
	
	
		IF C_LINK_GRP_YN = 'Y' THEN  -- 연계 여신관리 대상
	
	       C_GRP_AMT2 := 0; 
	       C_CO_AMT2  := 0;
			
			/* 연계 거래처 금액 산정 --> 기본 신용은 마스터금액과 Detail 금액  2곳에 있음
			       	마스터 그룹여신 금액 : TB_BM02M01.BASIS_CDTLN_AMT
			        Detail 금액 : TB_BM02D02.PLDG_AMT * TB_BM02D02.PLDG_RCOGN_RATE / 100
					*/
			SELECT a.LINK_GRP_CLNT_CD as C_LINK_GRP_CLNT_CD, 
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
			  WHERE a.LINK_GRP_CLNT_CD = C_LINK_GRP_CLNT_CD
			    AND a.LINK_GRP_YN      = 'Y' 
				AND a.USE_YN           = 'Y'
	  		  GROUP BY a.LINK_GRP_CLNT_CD;
				
		END IF;  --C_LINK_GRP_YN
	/*----------------------------------------------------------------------------*/
	/*  현재일자 기준 여신금액 계산 End                                                    */
	/*----------------------------------------------------------------------------*/
	END IF;
			
	IF I_CD = 'C' OR I_CD = 'T' THEN
			
	/*----------------------------------------------------------------------------*/
	/*  계산서 발행일자 기준 잔액 계산 Start                                                    */
	/*----------------------------------------------------------------------------*/
	--거래처 여신금액 관리 테이블 사별여신금액 (담보금액 * 답보인정비율 )
 		SELECT nvl(sum(PLDG_AMT*PLDG_RCOGN_RATE/100),0)
             into O_CO_AMT
			FROM TB_BM02D02
			WHERE CLNT_CD  = I_CLNT_CD
		      AND CO_CD    = nvl(I_CO_CD,CO_CD)
			  AND PLDG_DIV_CD NOT IN ('PLDG03')
			  AND SELPCH_CD = 'SELPCH2'
			  AND nvl(SETUP_DT,00010101) <= TO_NUMBER(I_TR_DT) 
			  AND nvl(EXPRTN_DT,99999999) >= TO_NUMBER(I_TR_DT)
			  AND USE_YN    = 'Y';


		C_TRSP_AMT := 0;	--매출금액 임시 집계
		C_ETRDPS_AMT := 0;	--입금금액 임시 집계
		IF  I_CD = 'C' THEN  -- 여신 총 한도 금액 산정
			--거래처 매출금액 누계액 계산
				SELECT SUM(nvl(BILG_AMT,0)) + SUM(nvl(BILG_VAT_AMT,0)) as C_TRSP_AMT  -- 청구금액 + 청구부가세금액
/*					CASE WHEN nvl(BILG_AMT,0) <> 0 			--청구금액
							THEN nvl(BILG_AMT,0)			--청금금액
					     WHEN nvl(REAL_TRST_AMT,0) <> 0 	--실출하금액
					     	THEN nvl(REAL_TRST_AMT,0) 
					     ELSE nvl(TRST_AMT,0) 			--출하지시금액
					     END  
*/
					into C_TRSP_AMT
				 FROM TB_AR02M01 
				WHERE CO_CD        = NVL(I_CO_CD,CO_CD) 
				  AND TRST_CLNT_CD = I_CLNT_CD
			      AND SELPCH_CD    = 'SELPCH2'
				  AND TRST_DT      <= TO_NUMBER(I_TR_DT);
		
			--거래처별 입급금액 누계액 계산
				SELECT sum(nvl(ETRDPS_AMT,0)) into C_ETRDPS_AMT
				 FROM TB_AR05M01
				WHERE CO_CD      = NVL(I_CO_CD,CO_CD)
				  AND CLNT_CD    = I_CLNT_CD
				  AND ETRDPS_TYP IN ('ETRDPS01', 'ETRDPS03', 'ETRDPS05') 	--지급, 상계   ETRDPS01=수금, ETRDPS02=지급, ETRDPS03=상계, ETRDPS05=지급보증보전
				  AND ETRDPS_DT <= TO_NUMBER(I_TR_DT);
		END IF;

		O_BLCE_TRDT_AMT := 0;

		--사별담보여신 잔액이 남아 있는지 확인 (거래처헤더 여신금액 +거래처신용담보금액+사별기타담보금액담 - 매출금액 + 입금금액)
		O_BLCE_TRDT_AMT := nvl(C_GRP_AMT,0) + nvl(C_CO_AMT,0) + nvl(O_CO_AMT,0) - nvl(C_TRSP_AMT,0) + nvl(C_ETRDPS_AMT,0);
	/*----------------------------------------------------------------------------*/
	/*  계산서 발행일자 기준 잔액 계산 End                                                    */
	/*----------------------------------------------------------------------------*/

	/*----------------------------------------------------------------------------*/
	/*  현재일자 기준 잔액 계산 Start                                                    */
	/*----------------------------------------------------------------------------*/
	--거래처 여신금액 관리 테이블 사별여신금액 (담보금액 * 답보인정비율 )
 		SELECT nvl(sum(PLDG_AMT*PLDG_RCOGN_RATE/100),0)
             into O_CO_AMT
			FROM TB_BM02D02
			WHERE CLNT_CD  = I_CLNT_CD
		      AND CO_CD    = nvl(I_CO_CD,CO_CD)
			  AND PLDG_DIV_CD NOT IN ('PLDG03')
			  AND SELPCH_CD = 'SELPCH2'
			  AND nvl(SETUP_DT,00010101) <= TO_CHAR(SYSDATE,'YYYYMMDD')
			  AND nvl(EXPRTN_DT,99999999) >= TO_CHAR(SYSDATE,'YYYYMMDD')
			  AND USE_YN    = 'Y';


		C_TRSP_AMT := 0;	--매출금액 임시 집계
		C_ETRDPS_AMT := 0;	--입금금액 임시 집계
		IF  I_CD = 'C' THEN  -- 여신 총 한도 금액 산정
			--거래처 매출금액 누계액 계산
				SELECT SUM(nvl(BILG_AMT,0)) + SUM(nvl(BILG_VAT_AMT,0)) as C_TRSP_AMT  -- 청구금액 + 청구부가세금액
					into C_TRSP_AMT
				 FROM TB_AR02M01 
				WHERE CO_CD        = NVL(I_CO_CD,CO_CD) 
				  AND TRST_CLNT_CD = I_CLNT_CD
			      AND SELPCH_CD    = 'SELPCH2'
				  AND TRST_DT      <= TO_CHAR(SYSDATE,'YYYYMMDD');
		
			--거래처별 입급금액 누계액 계산
				SELECT sum(nvl(ETRDPS_AMT,0)) into C_ETRDPS_AMT
				 FROM TB_AR05M01
				WHERE CO_CD      = NVL(I_CO_CD,CO_CD)
				  AND CLNT_CD    = I_CLNT_CD
				  AND ETRDPS_TYP IN ('ETRDPS01', 'ETRDPS03', 'ETRDPS05') 	--지급, 상계   ETRDPS01=수금, ETRDPS02=지급, ETRDPS03=상계, ETRDPS05=지급보증보전
				  AND ETRDPS_DT <= TO_CHAR(SYSDATE,'YYYYMMDD');
		END IF;

		O_BLCE_CURR_AMT := 0;

		--사별담보여신 잔액이 남아 있는지 확인 (거래처헤더 여신금액 +거래처신용담보금액+사별기타담보금액담 - 매출금액 + 입금금액)
		O_BLCE_CURR_AMT := nvl(C_GRP_AMT2,0) + nvl(C_CO_AMT2,0) + nvl(O_CO_AMT,0) - nvl(C_TRSP_AMT,0) + nvl(C_ETRDPS_AMT,0);
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

		RETURN O_BLCE_AMT;  --거래가능 금액 Return (

/**************************************************************
* 매출발생시 (여신 차감)
*   SELECT f_CREDIT_LOAN('P', 1, 'GGS', '20210203', 100000) FROM DUAL; 여신할당금액 100,000원 증가
*   1. 사별 매출 누게금액
*      - 첫째. 그룹 공통여신금액을 차감하고 
*      - 둘째. 그룹여신 차감 잔액 발생시 사별여신금액 차감한다. 
***************************************************************/		
	ELSIF I_CD = 'P' THEN
	  BEGIN

	  	-- 매출일자기준여신잔액(C_CO_AMT) > 현재일 기준 여신잔액(C_CO_AMT2)
	  	-- 두개중 작은값을 취함
		IF C_CO_AMT > C_CO_AMT2 THEN  --사용가능 여신 금액 산출
			MIN_CO_AMT := C_CO_AMT2;
		ELSE
			MIN_CO_AMT := C_CO_AMT;
		END IF;

	  	-- 가용여신잔액(MIN_CO_AMT) > 신규매출금액(I_AMT)
		IF MIN_CO_AMT < I_AMT THEN	--그룹여신금액 < 매출금액  : 여신잔액이 부족하면 오류!!!
			--여신잔액이 부족합니다.!!!!
			RETURN -1;
		ELSE --여신잔액이 사용 가능하면

			--그룹공통여신 가용금액 산정
			SELECT NVL(a.GRP_BLCE_AMT,0), 
			       NVL(b.GRP_BLCE_AMT,0),
			       NVL(b.CELL_REM_AMT,0)
			  INTO GRP_BLCE,	--그룹공통여신잔액
			  	   CO_BLCE,		--각사별 그룹공통여신 사용금액
			  	   CELL_REM 	--매출잔액 (매출-수금)
			  FROM TB_BM03M01 a LEFT JOIN  TB_BM04M01 b
			                       ON a.CLNT_CD = b.CLNT_CD
			                      AND b.CO_CD   = I_CO_CD
			 WHERE a.CLNT_CD = I_CLNT_CD;
			
			--그룹공통여신 가용금액 산정
			IF GRP_BLCE > I_AMT THEN
				BM03_AMT := I_AMT;
				BM04_AMT := 0;
			ELSE
				BM03_AMT := GRP_BLCE;
				BM04_AMT := I_AMT- BM03_AMT;
			END IF;
			

				SELECT COUNT(*) INTO BM03_CNT FROM TB_BM03M01 
				 WHERE CLNT_CD = I_CLNT_CD;
		
				IF BM03_CNT > 0 THEN
					UPDATE TB_BM03M01
					   SET GRP_BLCE_AMT    = NVL(GRP_BLCE_AMT,0) - BM03_AMT	--여신잔액
					 WHERE CLNT_CD = I_CLNT_CD;
				ELSE

					INSERT INTO TB_BM03M01
						(CLNT_CD, GRP_PLDG_AMT, GRP_BLCE_AMT, USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
					VALUES
						(C_CLNT_CD, 0, (0-BM03_AMT), 'Y', 'DB_TRIGER', 'F_CREDIT_LOAN', SYSDATE);
				END IF;
					
				SELECT COUNT(*) INTO BM04_CNT FROM TB_BM04M01 
				 WHERE CLNT_CD = I_CLNT_CD 
		           AND CO_CD   = I_CO_CD;

				IF BM04_CNT > 0 THEN
					UPDATE TB_BM04M01
					   SET CO_BLCE_AMT    = nvl(CO_BLCE_AMT,0) - BM04_AMT		--여신잔액
					      ,CELL_TOT_AMT   = nvl(CELL_TOT_AMT,0) + BM04_AMT		--매출금액
					      ,CELL_REM_AMT   = nvl(CELL_REM_AMT,0) + BM04_AMT		--매출잔액
					      ,GRP_BLCE_AMT   = nvl(GRP_BLCE_AMT,0) + BM03_AMT		--그룹여신사용금액
					      ,LAST_CELL_DTTM = I_TR_DT						--최종매출일자	
					 WHERE CLNT_CD = I_CLNT_CD
					   AND CO_CD   = I_CO_CD;
				ELSE
					INSERT INTO TB_BM04M01
						(CLNT_CD, CO_CD, 
						CO_PLDG_AMT, CO_BLCE_AMT, 
						CELL_TOT_AMT, CELL_CLMN_AMT, 
						PCHS_TOT_AMT, PCHS_CLMN_AMT, 
						CELL_REM_AMT, GRP_BLCE_AMT,
						LAST_CELL_DTTM,
						USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
					VALUES
					   (I_CLNT_CD, I_CO_CD, 
						0, (0-BM04_AMT),
						BM04_AMT, 0, 
						0, 0, 
						BM04_AMT, BM03_AMT,
						I_TR_DT,
						'Y', 'DB_TRIGER', 'F_CREDIT_LOAN', SYSDATE);
				END IF;
		
			   
		END IF;

 	  EXCEPTION WHEN OTHERS THEN RETURN -8;
 	  END;
		commit;	
		
		RETURN 0;	/*처리완료*/


/**************************************************************
* 수금처리 시
*	SELECT f_CREDIT_LOAN('M', 1, 'GGS', '20210204',100000) FROM DUAL; 여신할당금액 100,000원 감소
*   1. 그룹 여신 금액을 먼저 할당하고
*   2. 그룹여신이 없으면 사별 담보여신금액에 할당한다
***************************************************************/		
	ELSIF I_CD = 'M' THEN --수금 금액 처리시 FLAG
	  begin			

		SELECT NVL(a.GRP_BLCE_AMT,0), 
		       NVL(b.GRP_BLCE_AMT,0),
		       NVL(b.CELL_REM_AMT,0)
		  INTO GRP_BLCE,		--그룹공통여신잔액
		  	   CO_BLCE,		--각사별 그룹공통여신 사용금액
		  	   CELL_REM 		--매출잔액 (매출-수금)
		  FROM TB_BM03M01 a LEFT JOIN  TB_BM04M01 b
		                       ON a.CLNT_CD = b.CLNT_CD
		                      AND b.CO_CD   = I_CO_CD
		 WHERE a.CLNT_CD = I_CLNT_CD;

		--매출잔액(CELL_REM) > 수금금액(I_AMT)
		IF NVL(CELL_REM,0) > I_AMT THEN
			BM03_AMT := 0;
			BM04_AMT := I_AMT;
		ELSE
			--매출금액보다 입금금액이 많은 경우 처리
			IF (CO_BLCE + CELL_REM) > I_AMT THEN
				BM03_AMT := I_AMT - CO_BLCE;
				BM04_AMT := CO_BLCE;
			ELSE
				BM03_AMT := CO_BLCE;
				BM04_AMT := I_AMT - CO_BLCE;
			
			END IF;
		END IF;

		/* 수금처리시 사별 여신할당 금액만큼 공제후 그룹여신금액 차감함  */ 
		SELECT COUNT(*) INTO BM04_CNT FROM TB_BM04M01 
		 WHERE CLNT_CD = I_CLNT_CD 
           AND CO_CD   = I_CO_CD;

		IF BM04_CNT > 0 THEN
			UPDATE TB_BM04M01
			   SET CO_BLCE_AMT    = NVL(CO_BLCE_AMT,0) + BM04_AMT		--여신잔액
				  ,CELL_REM_AMT   = NVL(CELL_REM_AMT,0) - BM04_AMT		--수금금액
				  ,CELL_CLMN_AMT  = NVL(CELL_CLMN_AMT,0) + BM04_AMT	--수금금액
				  ,GRP_BLCE_AMT   = NVL(GRP_BLCE_AMT,0) - BM03_AMT		--그룹여신할당금액
				  ,LAST_CLMN_DTTM = I_TR_DT						--최종수금일자	
			 WHERE CLNT_CD = I_CLNT_CD
			   AND CO_CD   = I_CO_CD;
		ELSE
			INSERT INTO TB_BM04M01
				(CLNT_CD, CO_CD, 
				CO_PLDG_AMT, CO_BLCE_AMT, 
				CELL_TOT_AMT, CELL_CLMN_AMT, 
				PCHS_TOT_AMT, PCHS_CLMN_AMT, 
				CELL_REM_AMT, GRP_BLCE_AMT,
				LAST_CELL_DTTM,
				USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
			VALUES
			   (I_CLNT_CD, I_CO_CD, 
				0, BM04_AMT,
				0, BM04_AMT, 
				0, 0, 
				(0-BM04_AMT), (0-BM03_AMT),
				I_TR_DT,
				'Y', 'DB_TRIGER', 'F_CREDIT_LOAN', SYSDATE);
		END IF;
   

		SELECT COUNT(*) INTO BM03_CNT FROM TB_BM03M01 
		 WHERE CLNT_CD = I_CLNT_CD;

		IF BM03_CNT > 0 THEN
			UPDATE TB_BM03M01
			   SET GRP_BLCE_AMT   = NVL(GRP_BLCE_AMT,0) + BM03_AMT		--여신잔액
			 WHERE CLNT_CD = I_CLNT_CD;
		ELSE

			INSERT INTO TB_BM03M01
				(CLNT_CD, GRP_PLDG_AMT, GRP_BLCE_AMT, USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
			VALUES
				(C_CLNT_CD, 0, BM03_AMT, 'Y', 'DB_TRIGER', 'F_CREDIT_LOAN', SYSDATE);
		END IF;
			
	
		exception when others then return -9;
		
	  end;	
		commit;	
			
		RETURN 0;	/*처리완료*/
	ELSE 
		RETURN -9997;	/* 처리구분 오류 */
	END IF;  --I_CD END
	      
	return -9998;
	
exception
    when others then
        dbms_output.put_line('exception occurred! (' || sqlcode || ') : ' || sqlerrm);
        return -9999;

END;