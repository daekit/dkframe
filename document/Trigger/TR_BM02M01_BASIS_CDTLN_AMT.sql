CREATE OR REPLACE TRIGGER TR_BM02M01_BASIS_CDTLN_AMT
AFTER INSERT OR UPDATE OR DELETE 	-- ���� �߰��ǰų� ���� ����Ǿ��� ��
ON TB_BM02M01 						-- �ŷ�ó(TB_BM02M01)
FOR EACH ROW 						-- ��� ����ǿ� ���� Ʈ���� ����

/******************************************************************************
* TYPE			: TRIGGER (Tibero)
* NAME			: TR_BM02M01_BASIS_CDTLN_AMT
* DEVELOPER		: Jangsub.Nam
* DESCRIPTION	: �ŷ�ó������ �⺻���űݾ� ������
* 	            1.�ŷ�ó�����űݾ�(TB_BM03M01) ���̺� �׷쿩�űݾ� ����ó��
*				2.�����ڵ�(TB_CM05M01)���� code_KIND = 'CO'�� ���κ� �㺸���űݾ� �ʱ��ڷ� ���� 
*
******************************************************************************/
DECLARE


BEGIN
	IF inserting THEN
		--�׷쿩�űݾ� ����		
		INSERT INTO TB_BM03M01
			(CLNT_CD, PRDT_GRP, 
			 GRP_PLDG_AMT, GRP_BLCE_AMT, 
			 USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
		SELECT 
		     NVL(:NEW.LINK_GRP_CLNT_CD,:NEW.CLNT_CD), CODE_ID AS PRDT_GRP, 
		     0, 0, 'Y', :NEW.CREAT_ID, :NEW.CREAT_PGM, :NEW.CREAT_DTTM
		  FROM TB_CM05M01 
		 WHERE CODE_KIND = 'PRDTGRP';

		
		--�纰����  �ʱ� ����		
		INSERT INTO TB_BM04M01
			(CLNT_CD, CO_CD, 
			CO_PLDG_AMT, CO_BLCE_AMT, CELL_TOT_AMT, CELL_CLMN_AMT, PCHS_TOT_AMT, PCHS_CLMN_AMT, 
			CELL_REM_AMT, GRP_BLCE_AMT, USE_YN, 
			CREAT_ID, CREAT_PGM, CREAT_DTTM, PRDT_GRP)
		SELECT NVL(:NEW.LINK_GRP_CLNT_CD,:NEW.CLNT_CD), CODE_ID, 
			0, 0, 0, 0, 0, 0, 
			0, 0, 'Y', 
			:NEW.CREAT_ID, :NEW.CREAT_PGM, :NEW.CREAT_DTTM, PRDT_GRP
		FROM TB_CM05M01  a,
			(SELECT CODE_ID AS PRDT_GRP FROM TB_CM05M01 WHERE code_KIND = 'PRDTGRP') b 
		WHERE code_KIND = 'CO' ;
		
			
/****���� ������� �����Ϸ� ����  �Ǿ� ó�� ���� ���� TB_BM02D02���� ��?���Ű��� �� 
	ELSIF updating THEN
		--�׷쿩�űݾ� ����		

		UPDATE TB_BM03M01 
		SET   
			USE_YN = 'Y', 
			UDT_ID = :NEW.UDT_ID , 
			UDT_PGM = :NEW.UDT_PGM , 
			UDT_DTTM = :NEW.UDT_DTTM 
		WHERE CLNT_CD = NVL(:NEW.LINK_GRP_CLNT_CD,:NEW.CLNT_CD) ;
*/
               
	ELSIF deleting THEN
		--�׷쿩�űݾ� ����		
		DELETE TB_BM03M01
		WHERE CLNT_CD = NVL(:OLD.LINK_GRP_CLNT_CD,:OLD.CLNT_CD);
		
		--�纰����  �ʱ� ����	
		DELETE TB_BM04M01
		WHERE CLNT_CD = NVL(:OLD.LINK_GRP_CLNT_CD,:OLD.CLNT_CD);
		
	END IF;

END;	
