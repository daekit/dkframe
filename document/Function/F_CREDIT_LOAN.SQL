CREATE OR REPLACE FUNCTION F_CREDIT_LOAN(I_CD CHAR,I_CLNT_CD number, I_CO_CD varchar, I_TR_DT VARCHAR, I_AMT number)
    RETURN number is 
    PRAGMA AUTONOMOUS_TRANSACTION;

/******************************************************************************
* TYPE			: FUNCTION (Tibero)
* NAME			: F_CREDIT_LOAN
* DEVELOPER		: Jangsub.Nam
* DESCRIPTION	: 입력받은 코드, 거래처번호, 법인코드,  금액, -9999:Exception 발생)
* 	 구분 : C = 여신 잔액 체크, 잔액  Return
*		  M = 여신할당금액증가, 처리금액
*		  P = 여신할당금액감소, 처리금액
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


    O_BLCE_AMT 	number := 0;  
	O_GRP_AMT 	number := 0;	/*그룹 한도 금액*/
    O_CO_AMT 	number := 0;	/*사별 한도 금액*/
    
    C_TRSP_AMT 	 number := 0;	/*기준일자까지의 사별 매출금액 집계*/
    C_ETRDPS_AMT number := 0;	/*기준일자까지의 사별 입금누계액*/
    
    
    C_GRP_AMT 	number := 0;	/*그룹여신 잔여 금액*/
    C_CO_AMT 	number := 0;	/*사별여신 잔여 금액*/
    C_CLNT_CD 	number := 0;
    C_LINK_GRP_YN 	VARCHAR(20) := '';
    C_LINK_GRP_CLNT_CD 	number := 0;
    
    REM_AMT 	number := 0;	/* 잔여 금액*/
    
    O_GRP_PLDG_AMT number := 0;
    O_GRP_BLCE_AMT number := 0;
    O_CO_PLDG_AMT number := 0;
    O_CO_BLCE_AMT number := 0;
    
BEGIN
/**************************************************************
* -여신 잔액 체크
*  SELECT f_CREDIT_LOAN('C', 1, 'GGS','20210131',0) FROM DUAL; -- 여신잔액
*   1. 사별 여신 잔액 산출
***************************************************************/
	IF I_CD = 'C' OR I_CD = 'T' THEN

	/* 기본 신용은 마스터금액과 Detail 금액  2곳에 있음
	       	마스터 그룹여신 금액 : TB_BM02M01.BASIS_CDTLN_AMT
	        Detail 금액 : TB_BM02D02.PLDG_AMT * TB_BM02D02.PLDG_RCOGN_RATE / 100
	       거래처 여신금액 관리 테이블 사별여신금액 (담보금액 * 답보인정비율 ) */
		SELECT max(a.CLNT_CD )AS CLNT_CD, 
			   sum(nvl(A.BASIS_CDTLN_AMT,0)) AS BASIS_AMT, 
			   sum(nvl(b.PLDG_AMT*b.PLDG_RCOGN_RATE/100,0)) AS PLDG_AMT, 
  			   max(a.LINK_GRP_YN )AS LINK_GRP_YN, 
  			   max(a.LINK_GRP_CLNT_CD )AS LINK_GRP_CLNT_CD 
              into C_CLNT_CD, 
                   C_GRP_AMT, 
                   C_CO_AMT, 
                   C_LINK_GRP_YN, 
                   C_LINK_GRP_CLNT_CD 	 
		from TB_BM02M01 a LEFT OUTER JOIN TB_BM02D02 b 
							 ON a.CLNT_CD = b.CLNT_CD
							AND b.PLDG_DIV_CD = 'PLDG03'
							AND b.USE_YN = 'Y' 
							AND b.SELPCH_CD = 'SELPCH2'
							AND nvl(B.EXPRTN_DT,99999999) >= TO_NUMBER(I_TR_DT) 
		WHERE  a.USE_YN = 'Y'
		  AND a.CLNT_CD = I_CLNT_CD;


		IF C_LINK_GRP_YN = 'Y' THEN  -- 연계 여신관리 대상

	       C_GRP_AMT := 0; 
           C_CO_AMT  := 0;
			
			/* 연계 거래처 금액 산정 --> 기본 신용은 마스터금액과 Detail 금액  2곳에 있음
			       	마스터 그룹여신 금액 : TB_BM02M01.BASIS_CDTLN_AMT
			        Detail 금액 : TB_BM02D02.PLDG_AMT * TB_BM02D02.PLDG_RCOGN_RATE / 100
					*/
			SELECT max(a.LINK_GRP_CLNT_CD )AS LINK_GRP_CLNT_CD, 
			       sum(nvl(A.BASIS_CDTLN_AMT,0)) AS BASIS_AMT, 
			       sum(nvl(b.PLDG_AMT*b.PLDG_RCOGN_RATE/100,0)) AS PLDG_AMT 
	               into C_LINK_GRP_CLNT_CD, C_GRP_AMT, C_CO_AMT
				from TB_BM02M01 a LEFT OUTER JOIN TB_BM02D02 b 
									 ON a.CLNT_CD = b.CLNT_CD
									AND b.PLDG_DIV_CD = 'PLDG03'
									AND b.USE_YN = 'Y' 
									AND b.SELPCH_CD = 'SELPCH2'
									AND nvl(B.EXPRTN_DT,99999999) >= TO_NUMBER(I_TR_DT) 
				WHERE a.LINK_GRP_CLNT_CD = C_LINK_GRP_CLNT_CD
			  	  AND a.LINK_GRP_YN      = 'Y' 
				  AND a.USE_YN           = 'Y';
				
		END IF;  --C_LINK_GRP_YN
		
	--거래처 여신금액 관리 테이블 사별여신금액 (담보금액 * 답보인정비율 )
 		SELECT sum(nvl((PLDG_AMT*PLDG_RCOGN_RATE/100),0)) 
             into O_CO_AMT
			FROM TB_BM02D02
			WHERE CLNT_CD  = I_CLNT_CD
		      AND CO_CD    = I_CO_CD
			  AND PLDG_DIV_CD NOT IN ('PLDG03')
			  AND SELPCH_CD = 'SELPCH2'
			  AND nvl(EXPRTN_DT,99999999) >= TO_NUMBER(I_TR_DT)
			  AND USE_YN    = 'Y';


		C_TRSP_AMT := 0;	--매출금액 임시 집계
		C_ETRDPS_AMT := 0;	--입금금액 임시 집계
		IF  I_CD = 'C' THEN  -- 여신 총 한도 금액 산정
			--거래처 매출금액 누계액 계산
				SELECT sum(
					CASE WHEN nvl(BILG_AMT,0) <> 0 			--청구금액
							THEN nvl(BILG_AMT,0)				--청금금액
					     	WHEN nvl(REAL_TRST_AMT,0) <> 0 	--실출하금액
					     	THEN nvl(REAL_TRST_AMT,0) 
					     	ELSE nvl(TRST_AMT,0) 				--출하지시금액
					     END ) 
					into C_TRSP_AMT
				 FROM GOLDMOON.TB_AR02M01 
				WHERE co_cd = I_CO_CD 
				  AND TRST_CLNT_CD = I_CLNT_CD
			      AND SELPCH_CD = 'SELPCH2'
				  AND TRST_DT <= TO_NUMBER(I_TR_DT);
		
			--거래처별 입급금액 누계액 계산
				SELECT sum(nvl(ETRDPS_AMT,0)) into C_ETRDPS_AMT
				 FROM TB_AR05M01
				WHERE co_cd = I_CO_CD
				  AND CLNT_CD = I_CLNT_CD
				  AND ETRDPS_DT <= TO_NUMBER(I_TR_DT);
		END IF;

		O_BLCE_AMT := 0;

		--사별담보여신 잔액이 남아 있는지 확인 (거래처헤더 여신금액 +거래처신용담보금액+사별기타담보금액담 - 매출금액 + 입금금액)
		O_BLCE_AMT := nvl(C_GRP_AMT,0) + nvl(C_CO_AMT,0) + nvl(O_CO_AMT,0) - nvl(C_TRSP_AMT,0) + nvl(C_ETRDPS_AMT,0);

/*		IF O_BLCE_AMT < 0 THEN
	       O_BLCE_AMT := 0;
		END IF;
*/	
		RETURN O_BLCE_AMT;  --거래가능 금액 Return (

/**************************************************************
* 여신 차감
*   SELECT f_CREDIT_LOAN('M', 1, 'GGS', '20210203', 100000) FROM DUAL; 여신할당금액 100,000원 증가
*   1. 사별 매출 누게금액
***************************************************************/		
	ELSIF I_CD = 'M' THEN
	  BEGIN
		UPDATE TB_BM04M01
		SET CO_BLCE_AMT   = CO_BLCE_AMT + I_AMT		--여신잔액
		   ,CELL_CLMN_AMT = CELL_CLMN_AMT + I_AMT	--수금금액
		WHERE CLNT_CD = I_CLNT_CD
		  AND CO_CD   = I_CO_CD;
 	  EXCEPTION WHEN OTHERS THEN RETURN -8;
 	  END;
		commit;	
		
		RETURN 0;	/*처리완료*/


/**************************************************************
* 숙처리 시
*	SELECT f_CREDIT_LOAN('P', 1, 'GGS', '20210204',100000) FROM DUAL; 여신할당금액 100,000원 감소
*   1. 그룹 여신 금액을 먼저 할당하고
*   2. 그룹여신이 없으면 사별 담보여신금액에 할당한다
***************************************************************/		
	ELSIF I_CD = 'P' THEN
	  begin			
		UPDATE TB_BM04M01
		SET CO_BLCE_AMT  = CO_BLCE_AMT - I_AMT	
		   ,CELL_TOT_AMT = CELL_TOT_AMT + I_AMT	
		WHERE CLNT_CD    = I_CLNT_CD
		  AND CO_CD      = I_CO_CD;
		
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