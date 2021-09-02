CREATE OR REPLACE PROCEDURE PL_TAX_SENDCHK
/*
* ���ν����� : ���ڼ��ݰ�꼭 ���� ���� üũ ó��
* ���ν��� ID: PL_TAX_SENDCHK
* ���� :  ���ڼ��ݰ�꼭 ����ó���� �������� ���� �߻��� ó���ϴ�  ���ν����̴�
* �ۼ��� : Jangsub.nam
* �ۼ������ : 2021.09.02
* ���������� : Jangsub.nam
* ���������� : 2021.09.02
* Ư�� ����   : �������̺�  MANAGE_TBL����  ERROR_CODE ���� '00000000' �ƴѰ��߿� ���ݰ�꼭�� �ش��ϴ� �͸� ó�� �Ѵ�.
*         ó���Ϸ��     UPDATE_IND 'Y' �����Ѵ�.
*         ó��������     UPDATE_IND 'E' �����Ѵ�.
*/
 
IS
   C_CSEQ_RCV_YN VARCHAR(01) := '';
   C_SND_YN      VARCHAR(01) := '';  
   
   CURSOR C1 IS
		SELECT A.DOC_MSG_ID, A.XML_MSG_ID , A.ERROR_CODE,A.XML_FLOW ,A.UPDATE_IND ,A.CUR_STATUS ,
               B.BGM_1001, B.BGM_1004,  A.ERROR_MSG
		  FROM KLUSER01.MANAGE_TBL a  INNER JOIN KLUSER01.TAX_HD B 
		                                      ON A.XML_MSG_ID = B.XML_MSG_ID 
		 WHERE  A.ERROR_CODE > '00000000'
		   AND A.XML_FLOW     = 'S'
		   AND A.UPDATE_IND   = 'N'
		   AND A.CUR_STATUS   = 'ER'
		 ORDER BY A.DOC_MSG_ID;
				         
/* 1. edi �������� ���̺��� ���������� �о�´�
 * 2. ������ ������ ���� �ش� ������ ����� Update �Ѵ�.
*/

BEGIN
    FOR CUR1 IN C1 LOOP    
		
	    BEGIN	
	   		SELECT nvl(CSEQ_RCV_YN,'N'), nvl(SND_YN,'N')
			  into C_CSEQ_RCV_YN, C_SND_YN
			  FROM goldmoon.TB_AR04M01
			 WHERE TAX_BILG_NO = CUR1.BGM_1004;
	
			IF (C_SND_YN = 'Y') THEN		  --���ۿϷ�
				IF C_CSEQ_RCV_YN != 'Y' THEN  --�ý����������
					UPDATE goldmoon.TB_AR04M01
					SET  CSEQ_RCV_YN	= 'E'	-- ��꼭 ó�����
						,CSEQ_RCV_DTTM	= sysdate	-- ó�����ȸ������
						,ERR_CD         = CUR1.ERROR_CODE	--�����ڵ�
						,ERR_CNTS       = CUR1.ERROR_MSG 	--�����޼���
					WHERE TAX_BILG_NO = CUR1.BGM_1004;	--��꼭�����ȣ
				END IF;    		
			END IF;     				
	
		    -- ���乮�� ó���Ϸ� ����     
			UPDATE KLUSER01.MANAGE_TBL
			   SET UPDATE_IND = 'Y'				-- ó���Ϸ�
			 WHERE DOC_MSG_ID = CUR1.DOC_MSG_ID
			   AND XML_MSG_ID = CUR1.XML_MSG_ID;
	 
	 	    COMMIT;
			
	     EXCEPTION WHEN OTHERS THEN
	        	BEGIN
		            DBMS_OUTPUT.PUT_LINE('���� �߻� ��꼭��ȣ : ' || CUR1.BGM_1004 ||'-'||CUR1.DOC_MSG_ID);
			   	    -- ���乮�� ���� ����     
					UPDATE KLUSER01.MANAGE_TBL
					   SET UPDATE_IND = 'E'				-- ����
					 WHERE DOC_MSG_ID = CUR1.DOC_MSG_ID
					   AND XML_MSG_ID = CUR1.XML_MSG_ID;
					COMMIT;
				END;
	     END;
     END LOOP;
      
END;
