CREATE OR REPLACE TRIGGER TR_MAPIN_TBL
AFTER INSERT  	-- 행이 추가되거나 값이 변경되었을 때
ON KLUSER01.MAPIN_TBL
FOR EACH ROW 

/******************************************************************************
* TYPE			: TRIGGER (Tibero)
* NAME			: TR_KLUSER01.MAPIN_TBL
* DEVELOPER		: Jangsub.Nam
* DESCRIPTION	: 세금계산서 처리 결과 수신 처리
******************************************************************************/
DECLARE 
    -- 사용할 변수선언

BEGIN
	GOLDMOON.PL_TAX_APERAK;
COMMIT;

END;