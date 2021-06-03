CREATE OR REPLACE TRIGGER TB_BM02D02_PLDG_AMT
AFTER INSERT OR UPDATE OR DELETE 	-- ���� �߰��ǰų� ���� ����Ǿ��� ��
ON TB_BM02D02 						-- �ŷ�ó(TB_BM02D02)
FOR EACH ROW 						-- ��� ����ǿ� ���� Ʈ���� ����

--�ŷ�ó������ �㺸�ݾ� ������
--�ŷ�ó�����űݾ�(TB_BM03M01) ���̺� �纰���űݾ�(CO_PLDG_AMT)����ó��
--�����ڵ忡 �ִ� �׷�ȸ�� ��ü�� �ϰ� ������
BEGIN
	IF inserting THEN
		--�纰���űݾ� = �հ�(�㺸�ݾ� * �㺸��������)
		UPDATE TB_BM03M01
		SET CO_PLDG_AMT = 
			(select nvl(sum(PLDG_AMT*PLDG_RCOGN_RATE/100),0) from TB_BM02D02
			where CLNT_CD = :NEW.CLNT_CD
			and  CO_CD = :NEW.CO_CD)
		WHERE CLNT_CD = :NEW.CLNT_CD
		AND CO_CD = :NEW.CO_CD;

	ELSIF updating THEN
		--�纰���űݾ� = �հ�(�㺸�ݾ� * �㺸��������)
		UPDATE TB_BM03M01
		SET CO_PLDG_AMT = 
			(select nvl(sum(PLDG_AMT*PLDG_RCOGN_RATE/100),0) from TB_BM02D02
			where CLNT_CD = :NEW.CLNT_CD
			and  CO_CD = :NEW.CO_CD)
		WHERE CLNT_CD = :NEW.CLNT_CD
		AND CO_CD = :NEW.CO_CD;
               
	ELSIF deleting THEN
		--�纰���űݾ� = �հ�(�㺸�ݾ� * �㺸��������)
		UPDATE TB_BM03M01
		SET CO_PLDG_AMT = 
			(select nvl(sum(PLDG_AMT*PLDG_RCOGN_RATE/100),0) from TB_BM02D02
			where CLNT_CD = :OLD.CLNT_CD
			and  CO_CD = :OLD.CO_CD)
		WHERE CLNT_CD = :OLD.CLNT_CD
		AND CO_CD = :OLD.CO_CD;
	END IF;
END;
