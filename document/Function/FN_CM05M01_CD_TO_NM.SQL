CREATE OR REPLACE FUNCTION FN_CM05M01_CD_TO_NM (I_CODE_ID VARCHAR)
/******************************************************************************
* TYPE			: FUNCTION (Tibero)
* NAME			: FN_CM05M01_CD_TO_NM
* DEVELOPER		: inyoung.park
* DESCRIPTION	: 입력받은 코드로 코드명을 SELECT 한다.
*	SELECT FN_CM05M01_CD_TO_NM('CLNTDIV02') FROM DUAL; -- '매출' CODE_NM 추출
*
******************************************************************************/
RETURN VARCHAR 
IS
     O_CODE_NM VARCHAR(100);
BEGIN
    SELECT CODE_NM INTO O_CODE_NM 
    FROM TB_CM05M01 
    WHERE CODE_ID = I_CODE_ID AND USE_YN = 'Y';
    RETURN O_CODE_NM;
END;
