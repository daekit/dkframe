CREATE OR REPLACE PROCEDURE PL_TAX_APERAK
/*
* ���ν����� : ���ڼ��ݰ�꼭 ���乮�� ó��
* ���ν��� ID: PL_TAX_APERAK
* ���� :  ���ڼ��ݰ�꼭 ����ó���� ���� ����� ���乮���� ���Ź޾� ó���ϴ� ���ν����̴�
* �ۼ��� : ABC
* �ۼ������ : 2021.04.06
* ���������� :
* ���������� :
* Ư�� ����   : �������̺�  mapin_tbl�� CUR_STATUS ���� 'Y' �ƴѰ͸� ó�� �Ѵ�.
*         ó���Ϸ��     CUR_STATUS�� 'Y' �����Ѵ�.
*/
 
IS
   C_CSEQ_RCV_YN VARCHAR(01) := '';
   C_TAX_ADMS_YN VARCHAR(01) := '';
   C_RCV_YN      VARCHAR(01) := '';
   C_RCV_PROC_ID VARCHAR(20) := '';
	
   CURSOR C1 IS
		SELECT * 
		FROM KLUSER01.MAPIN_TBL a 
		       LEFT OUTER JOIN KLUSER01.APERAK_HD b
		            ON a.XML_MSG_ID = b.XML_MSG_ID 
		           AND a.XMLDOC_SEQ = b.XMLDOC_SEQ 
		       LEFT OUTER JOIN KLUSER01.APERAK_DTL c 
		            ON a.XML_MSG_ID = c.XML_MSG_ID 
		           AND a.XMLDOC_SEQ = c.XMLDOC_SEQ
		WHERE a.DOC_NAME = 'APERAK'
		  AND a.DOC_CODE = '294'
		  AND a.ORIGINATOR = 'KLLOGISBIL'
		  AND a.RECIPIENT = 'KLDKSYL001'
		  AND A.CUR_STATUS IS null ;
		         
/* 1. ���乮���� �о�´�
 * 2. ������ ������ ���� �ش� ������ ����� Update �Ѵ�.
 * CUR1.DOC_1001   : '938':���ݰ�꼭�� ���� ����
 *                   '780':�ŷ������� ���� ����
 *                   '2'  :�Ա��ݿ� ���� ����
 *
 * CUR1.RFFZZZ1154 : '1' : �ý�������
 *                   '2' : ���������
 *                   '3' : ����û����(���ݰ�꼭�� ��츸 ���۵�)   
 *
 * CUR1.ERC_9321   : 'OK' : ����
 *                   'ER' : ����
 *
 * CUR1.ERC___1131 : 1) �ý��� ����
 *                      'O' : �ý��� ����
 *                      'E' : �ý��� ����
 *                   2) ����� ����
 *                      'C' : ����
 *                      'W' : ����
 *                      'L' : ����(������ �׼�)
 *                   3) ����û ����
 *                      'NPO' : ����û ó�� ����
 *                      'NPE' : ����û ó�� ����
 *    
 * CUR1.FTXAAP_440 : �����޼��� Ȥ�� ��������
*/

BEGIN
    FOR CUR1 IN C1 LOOP    

		
     BEGIN
     
     	SELECT CSEQ_RCV_YN,   TAX_ADMS_YN ,  RCV_YN,   RCV_PROC_ID
		  into C_CSEQ_RCV_YN, C_TAX_ADMS_YN, C_RCV_YN, C_RCV_PROC_ID
		  FROM goldmoon.TB_AR04M01
		 WHERE TAX_BILG_NO = CUR1.RFFACE1154;
		
     	IF (CUR1.DOC_1001 = '938') THEN  --���ڼ��ݰ�꼭

			--RFFZZZ1154  = 1:�ý��� ����, 2:���������,  3:����û����
     		IF (CUR1.ERC_9321 = 'OK')  THEN --���� (OK:����, ER:����)		
				IF (CUR1.RFFZZZ1154 = '1' AND CUR1.ERC___1131 = 'O') THEN		--�ý�������
					UPDATE goldmoon.TB_AR04M01
					SET  CSEQ_RCV_YN	= 'Y'	-- ��꼭 ó�����
						,CSEQ_RCV_DTTM	= CUR1.TIMESTAMP	-- ó�����ȸ������
					WHERE TAX_BILG_NO = CUR1.RFFACE1154;		--��꼭�����ȣ
						
				ELSIF (CUR1.RFFZZZ1154 = '2' AND CUR1.ERC___1131 = 'C') THEN	--���������
					UPDATE goldmoon.TB_AR04M01
					SET  TAX_ADMS_YN	= 'Y'	--���ο��� RFFZZZ1154  = 1:�ý��� ����, 2:���������,  3:����û����
						,TAX_ADMS_DTTM	= CUR1.TIMESTAMP	-- ó�����ȸ������
					WHERE TAX_BILG_NO = CUR1.RFFACE1154;	--��꼭�����ȣ

				ELSIF (CUR1.RFFZZZ1154 = '3' AND CUR1.ERC___1131 = 'NPO') THEN	--����û����
					IF C_RCV_PROC_ID IS NOT NULL THEN
						UPDATE goldmoon.TB_AR04M01
						SET  RCV_YN			= 'Y'	-- ����û���ſ���
							,RCV_DTTM		= CUR1.TIMESTAMP	-- ó�����ȸ������
							,RCV_PROC_ID	= CUR1.RFFSZ_1154	-- ����û���ι�ȣ
						WHERE TAX_BILG_NO   = CUR1.RFFACE1154;	--��꼭�����ȣ
					END IF;	
				END IF;

			ELSE -- (CUR1.ERC_9321 = 'ER')  THEN --���� (OK:����, ER:����)
					
			
				IF (CUR1.RFFZZZ1154 = '1') THEN		--�ý�������
					IF C_CSEQ_RCV_YN != 'Y' THEN
						UPDATE goldmoon.TB_AR04M01
						SET  CSEQ_RCV_YN	= 'E'	-- ��꼭 ó�����
							,CSEQ_RCV_DTTM	= CUR1.TIMESTAMP	-- ó�����ȸ������
							,ERR_CD         = CUR1.ERC___1131	--�����ڵ�
							,ERR_CNTS       = CUR1.FTXAAP_440 	--�����޼���
						WHERE TAX_BILG_NO = CUR1.RFFACE1154;	--��꼭�����ȣ
					END IF;
				ELSIF (CUR1.RFFZZZ1154 = '2') THEN	--���������
					IF C_TAX_ADMS_YN != 'Y' THEN
						UPDATE goldmoon.TB_AR04M01
						SET  TAX_ADMS_YN	= 'E'	--���ο��� RFFZZZ1154  = 1:�ý��� ����, 2:���������,  3:����û����
							,TAX_ADMS_DTTM	= CUR1.TIMESTAMP	-- ó�����ȸ������
							,ERR_CD         = CUR1.ERC___1131	--�����ڵ�
							,ERR_CNTS       = CUR1.FTXAAP_440 	--�����޼���
						WHERE TAX_BILG_NO   = CUR1.RFFACE1154;	--��꼭�����ȣ
					END IF;
	
				ELSIF (CUR1.RFFZZZ1154 = '3') THEN	--����û����
					IF C_RCV_YN != 'Y' THEN
						UPDATE goldmoon.TB_AR04M01
						SET  RCV_YN			= 'E'				-- ����û���ſ���
							,RCV_DTTM		= CUR1.TIMESTAMP	-- ó�����ȸ������
							,RCV_PROC_ID	= ''				-- ����û���ι�ȣ
							,ERR_CD         = CUR1.ERC___1131	--�����ڵ�
							,ERR_CNTS       = CUR1.FTXAAP_440 	--�����޼���
						WHERE TAX_BILG_NO   = CUR1.RFFACE1154;	--��꼭�����ȣ
					END IF;
	
				END IF;
			
			END IF  ;   	
     	     		
/*     	ELSIF (CUR1.DOC_1001 = '780') THEN  --�ŷ�����
     		UPDATE TB_AR04M01
*/     		
		END IF;     				

		EXCEPTION WHEN OTHERS THEN NULL;
	         
	    -- ���乮�� ó���Ϸ� ����     
		UPDATE KLUSER01.MAPIN_TBL
		   SET CUR_STATUS = 'Y'				-- ó���Ϸ�
		 WHERE DOC_MSG_ID = CUR1.DOC_MSG_ID
		   AND XMLDOC_SEQ = CUR1.XMLDOC_SEQ;
 
 	    COMMIT;
		
--		EXCEPTION WHEN OTHERS THEN NULL;
	         
      END;
     END LOOP;
      

END;
