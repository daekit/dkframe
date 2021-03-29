CREATE OR REPLACE FUNCTION F_CREDIT_LOAN_NEW(I_CD CHAR,I_CLNT_CD number, I_CO_CD varchar, I_TR_DT VARCHAR, I_AMT number)
    RETURN number

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
*	SELECT f_CREDIT_LOAN('M', 1, 'GGS','20210101', 100000) FROM DUAL; 여신할당금액 100,000원 증가
*	SELECT f_CREDIT_LOAN('P', 1, 'GGS','20210101', 100000) FROM DUAL; 여신할당금액 100,000원 감소
*
*  I_CD 구분
*  I_CLNT_CD 거래처 
*  I_CO_CD 회사
*  I_TR_DT 기준일자( 매출일자(P)/수금일자(M)/조회일자(C) )
*  I_AMT  금액
******************************************************************************/

AS
    O_BLCE_AMT 	number := 0;  
	O_GRP_AMT 	number := 0;	/*그룹 한도 금액*/
    O_CO_AMT 	number := 0;	/*사별 한도 금액*/
    
    C_TRSP_AMT 	 number := 0;	/*기준일자까지의 사별 매출금액 집계*/
    C_ETRDPS_AMT number := 0;	/*기준일자까지의 사별 입금누계액*/
    
    
    C_GRP_AMT 	number := 0;	/*그룹여신 잔여 금액*/
    C_CO_AMT 	number := 0;	/*사별여신 잔여 금액*/
    
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
	IF I_CD = 'C' THEN
	--거래처 여신금액 관리 테이블 사별여신금액 (담보금액 * 답보인정비율 )
		SELECT nvl(sum(PLDG_AMT*PLDG_RCOGN_RATE/100),0) 
               into O_CO_AMT
			FROM TB_BM02D02
			WHERE CLNT_CD = I_CLNT_CD
			AND CO_CD = I_CO_CD 
			AND EXPRTN_DT >= TO_NUMBER(I_TR_DT) ;

	--거래처 매출금액 누계액 계산
		SELECT sum(
			CASE WHEN BILG_AMT <> 0 			--청구금액
					THEN BILG_AMT				--청금금액
			     	WHEN REAL_TRST_AMT <> 0 	--실출하금액
			     	THEN REAL_TRST_AMT 
			     	ELSE TRST_AMT 				--출하지시금액
			     END ) 
			into C_TRSP_AMT
		FROM GOLDMOON.TB_AR02M01 
		WHERE co_cd = I_CO_CD 
		AND TRST_CLNT_CD = I_CLNT_CD
		AND TRST_DT <= TO_NUMBER(I_TR_DT);

	--거래처별 입급금액 누계액 계산
		SELECT sum(ETRDPS_AMT) into C_ETRDPS_AMT
		FROM TB_AR05M01
		WHERE co_cd = I_CO_CD
		AND CLNT_CD = I_CLNT_CD
		AND ETRDPS_DT <= TO_NUMBER(I_TR_DT);
		
		
		O_BLCE_AMT := 0;
		--사별담보여신 잔액이 남아 있는지 확인 (담보금액 - 매출금액 + 입금금액)
		O_BLCE_AMT := O_CO_AMT - C_TRSP_AMT + C_ETRDPS_AMT;
		
	    RETURN O_BLCE_AMT;  --거래가능 금액 Return (

/**************************************************************
* 여신 차감
*   SELECT f_CREDIT_LOAN('M', 1, 'GGS', '20210203', 100000) FROM DUAL; 여신할당금액 100,000원 증가
*   1. 사별 매출 누게금액
***************************************************************/		
	ELSIF I_CD = 'M' THEN
				
		UPDATE TB_BM04M01
		SET CO_BLCE_AMT = O_CO_BLCE_AMT + I_AMT		--여신잔액
		   ,CELL_CLMN_AMT = CELL_CLMN_AMT + I_AMT	--수금금액
		WHERE CLNT_CD = I_CLNT_CD
		AND CO_CD = I_CO_CD;
		commit;	
		
		RETURN 0;	/*처리완료*/


/**************************************************************
* 숙처리 시
*	SELECT f_CREDIT_LOAN('P', 1, 'GGS', '20210204',100000) FROM DUAL; 여신할당금액 100,000원 감소
*   1. 그룹 여신 금액을 먼저 할당하고
*   2. 그룹여신이 없으면 사별 담보여신금액에 할당한다
***************************************************************/		
	ELSIF I_CD = 'P' THEN
		
		UPDATE TB_BM04M01
		SET CO_BLCE_AMT = O_CO_BLCE_AMT - I_AMT	--여신잔액
		   ,CELL_TOT_AMT = CELL_TOT_AMT + I_AMT	--매출금액
		WHERE CLNT_CD = I_CLNT_CD
		AND CO_CD = I_CO_CD;
		commit;	
	
		RETURN 0;	/*처리완료*/
	ELSE 
		RETURN -9999;	/* 처리구분 오류 */
	END IF;  --I_CD END
	      
	return -9999;
	
exception
    when others then
        dbms_output.put_line('exception occurred! (' || sqlcode || ') : ' || sqlerrm);
        return -9999;

END;