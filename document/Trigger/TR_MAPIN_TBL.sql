CREATE OR REPLACE TRIGGER TR_MAPIN_TBL
AFTER INSERT  	-- ���� �߰��ǰų� ���� ����Ǿ��� ��
ON KLUSER01.MAPIN_TBL
FOR EACH ROW 

/******************************************************************************
* TYPE			: TRIGGER (Tibero)
* NAME			: TR_KLUSER01.MAPIN_TBL
* DEVELOPER		: Jangsub.Nam
* DESCRIPTION	: ���ݰ�꼭 ó�� ��� ���� ó��
******************************************************************************/
DECLARE 
    -- ����� ��������

BEGIN
	GOLDMOON.PL_TAX_APERAK;
COMMIT;

END;