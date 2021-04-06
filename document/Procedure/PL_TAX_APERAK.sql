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
   CURSOR C1 IS
		SELECT * 
		FROM mapin_tbl a 
		       LEFT OUTER JOIN APERAK_HD b
		            ON a.XML_MSG_ID = b.XML_MSG_ID 
		           AND a.XMLDOC_SEQ = b.XMLDOC_SEQ 
		       LEFT OUTER JOIN GOLDMOON.APERAK_DTL c 
		            ON a.XML_MSG_ID = c.XML_MSG_ID 
		           AND a.XMLDOC_SEQ = c.XMLDOC_SEQ
		WHERE a.DOC_NAME = 'APERAK'
		  AND a.DOC_CODE = '294'
		  AND a.ORIGINATOR = 'KLLOGISBIL'
		  AND a.RECIPIENT = 'DKSYL001'
		  AND A.CUR_STATUS <> 'Y' ;
		         
BEGIN
    FOR CUR1 IN C1 LOOP    
/* 1. ���乮���� �о�´�
 * 2. ������ ������ ���� �ش� ������ ����� Update �Ѵ�.
*/
     BEGIN
     	IF (CUR1.DOC___1001 = '938') THEN  --���ڼ��ݰ�꼭

			--RFFZZZ1154  = 1:�ý��� ����, 2:���������,  3:����û����
     		IF (CUR1.ERC___9321 = 'OK')  THEN --���� (OK:����, ER:����)		
				IF (CUR1.RFFZZZ1154 = '1') THEN		--�ý�������
					UPDATE TB_AR04M01
					SET  CSEQ_RCV_YN	= CUR1.RFFZZZ1154	-- ��꼭 ó�����
						,CSEQ_RCV_DTTM	= CUR1.TIMESTAMP	-- ó�����ȸ������
					WHERE TAX_BILG_NO = CUR1.RFFACE1154;		--��꼭�����ȣ
						
				ELSIF (CUR1.RFFZZZ1154 = '2') THEN	--���������
					UPDATE TB_AR04M01
					SET  TAX_ADMS_YN	= CUR1.RFFZZZ1154	--���ο��� RFFZZZ1154  = 1:�ý��� ����, 2:���������,  3:����û����
						,TAX_ADMS_DTTM	= CUR1.TIMESTAMP	-- ó�����ȸ������
					WHERE TAX_BILG_NO = CUR1.RFFACE1154;	--��꼭�����ȣ

				ELSIF (CUR1.RFFZZZ1154 = '3') THEN	--����û����
					UPDATE TB_AR04M01
					SET  RCV_YN			= CUR1.RFFZZZ1154	-- ����û���ſ���
						,RCV_DTTM		= CUR1.TIMESTAMP	-- ó�����ȸ������
						,RCV_PROC_ID	= CUR1.RFFSZ_1154	-- ����û���ι�ȣ
					WHERE TAX_BILG_NO   = CUR1.RFFACE1154;	--��꼭�����ȣ

				END IF;

			ELSE -- (CUR1.ERC___9321 = 'ER')  THEN --���� (OK:����, ER:����)
					UPDATE TB_AR04M01
					SET  CSEQ_RCV_YN	= 'E'				--�� �꼭 ó�����
						,CSEQ_RCV_DTTM	= CUR1.TIMESTAMP	-- ó�����ȸ������
					WHERE TAX_BILG_NO = CUR1.RFFACE1154;	--��꼭�����ȣ
			
			END IF  ;   	
     	     		
/*     	ELSIF (CUR1.BGM___1001 = '780') THEN  --�ŷ�����
     		UPDATE TB_AR04M01
*/     		
		END IF;     				
	         
	    -- ���乮�� ó���Ϸ� ����     
		UPDATE mapin_tbl
		SET  CUR_STATUS	= 'Y'				-- ó���Ϸ�
		WHERE DOC_MSG_ID = CUR1.DOC_MSG_ID
		  AND XMLDOC_SEQ = CUR1.XMLDOC_SEQ;
 
 	    COMMIT;
		
		EXCEPTION WHEN OTHERS THEN NULL;
	         
      END;
     END LOOP;
      
	       
 
END;
