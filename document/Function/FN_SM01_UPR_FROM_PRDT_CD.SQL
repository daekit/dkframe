CREATE OR REPLACE FUNCTION FN_SM01_UPR_FROM_PRDT_CD (
I_CO_CD        IN  VARCHAR, -- 회사
I_PRDT_CD      IN  VARCHAR  -- 자재코드

)
/******************************************************************************
* TYPE			: FUNCTION (Tibero)
* NAME			: FN_SM01_UPR_FROM_PRDT_CO
* DEVELOPER		: abc
* DESCRIPTION	: 입력받은 자재코드 정보로 해당 회사의 재고단가를 계산한다.
*	SELECT FN_SM01_UPR_FROM_PRDT_CD('GUM','1') FROM DUAL;U




I_IMP_YN  IN  VARCHAR,  -- 일반/수입구분   --- 일반만
I_OWNER_CD  --- 자사만
I_CLNT_CD      IN  VARCHAR   -- 거래처  자사만
I_PRJCT_CD
I_TYP_CD  ---  유통만...




I_MAKR_CD ---몰라 
I_PRDT_LEN  --- 몰



*
******************************************************************************/
RETURN NUMBER 
IS
    O_STOCK_UPR       NUMBER(15,2) := 0;
    I_STOCK_UPR       NUMBER(15,2) := 0;
 
BEGIN
   BEGIN
        SELECT STOCK_UPR
        INTO   I_STOCK_UPR
        FROM   TB_SM01M01
        WHERE  CO_CD    = I_CO_CD
          AND  PRDT_CD  = I_PRDT_CD
          AND  OWNER_CD = 'OWNER1'    --  자사재고 기준 
          AND  TYP_CD   = 'SELLTYPE1' -- 유퉁기준 
          AND  IMP_YN = 'N'  -- 국산기준
          AND  STOCK_QTY != 0
          ;
       EXCEPTION WHEN OTHERS THEN I_STOCK_UPR := 0;
   END;
   O_STOCK_UPR := I_STOCK_UPR;  -- 매출단가
  

    RETURN O_STOCK_UPR;
END;