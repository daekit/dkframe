CREATE OR REPLACE FUNCTION FN_BM02_MNG_ID (
I_CO_CD        IN  VARCHAR,  -- 회사
I_CLNT_CD      IN  VARCHAR   -- 거래처
)
/******************************************************************************
* TYPE			: FUNCTION (Tibero)
* NAME			: FN_BM02_MNG_ID
* DEVELOPER		: abc
* DESCRIPTION	: 입력받은 회사의 거래처의 영업담당자를 가져온다.
*	SELECT FN_BM02_MNG_ID(CO_CD, CLMT_CD) FROM DUAL; -- 담당자 ID 
*
******************************************************************************/
RETURN VARCHAR 
IS
    O_MNG_ID          VARCHAR(20) := NULL;
 
BEGIN
   BEGIN
   /* 현재 등록된 사원 중 하나를 가져온다.  */
        SELECT SALES_EMP_ID
        INTO   O_MNG_ID
        FROM   TB_BM02D01
        WHERE  CLNT_CD     = I_CLNT_CD
          AND  SALES_CO_CD = I_CO_CD
          AND  USE_YN      = 'Y'
          AND  ROWNUM      = 1;
          
          EXCEPTION WHEN OTHERS THEN O_MNG_ID := NULL;
   
   END;
   /* 없을 경우 기존 출하요청서에서 조회한다. */
   IF (O_MNG_ID IS NULL) THEN 
      BEGIN
         SELECT SALES_MNG INTO O_MNG_ID
         FROM   TB_AR01M01
         WHERE  CO_CD   = I_CO_CD
           AND  CLNT_CD = I_CLNT_CD
           AND  REQ_DT  = (SELECT MAX(REQ_DT) FROM TB_AR01M01
                           WHERE  CO_CD      = I_CO_CD
                             AND  CLNT_CD    = I_CLNT_CD  )
           AND ROWNUM = 1;
          EXCEPTION WHEN OTHERS THEN O_MNG_ID := NULL;
           
      END;
	END IF;       

    RETURN O_MNG_ID;
END;