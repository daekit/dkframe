CREATE OR REPLACE FUNCTION FN_BASIS_AMT (
I_CO_CD      IN  VARCHAR,   -- ȸ��
I_CLNT_CD    IN  VARCHAR,  -- �ŷ�ó
I_BASIS_DT   IN  VARCHAR,   -- ��������
I_SELPCH     IN  VARCHAR,   -- ����(SELPCH1)/����(SELPCH2)����
I_BILG_YN    IN  VARCHAR,   -- ��꼭 ���� ���α���
I_TAXIVC_COPRT    IN  VARCHAR    -- ��꼭 ���� ����
)
/******************************************************************************
* TYPE			: FUNCTION (Tibero)
* NAME			: FN_BASIS_AMT
* DEVELOPER		: abc
* DESCRIPTION	: �Է¹��� ���� �ŷ�ó�� �̿� ������ ���ϱ����� ä�Ǳݾ��� ����Ѵ�.
*	SELECT FN_BASIS_AMT('20210102',1 ) FROM DUAL;
*
*
* �۾�����
* 1. �������� ���Ե� ���� ���ʱݾ��� ��������.
* 2. ���ۿ� 1�� ~ ������-1�ϱ����� ����/����/�����ڷḦ �����ͼ� ����Ѵ�.
* 3. ��� ���� +���� +����  -���� -���� : �ܾ�
******************************************************************************/
RETURN NUMBER 
IS
    O_CREDIT_AMT      NUMBER(15,2) := 0;
   
    I_OWNER_CD        VARCHAR(20)  := 'OWNER1' ; -- �ڻ�ܰ��� ��ȸ�Ѵ�.
    P_CLOSE_YM        VARCHAR(6) := '';  -- ���ؿ�
    P_BASIS_SELL_AMT  NUMBER(15,2) := 0;  -- �ܻ���� ����
    P_BASIS_PCHS_AMT  NUMBER(15,2) := 0;  -- �ܻ���� ����
    P_PCHS_AMT_AR02   NUMBER(15,2) := 0;  -- �ܻ����
    P_SELL_AMT_AR02   NUMBER(15,2) := 0;  -- �ܻ����
    P_PCHS_AMT_AR04   NUMBER(15,2) := 0;  -- �ܻ����
    P_SELL_AMT_AR04   NUMBER(15,2) := 0;  -- �ܻ����
    P_PAY_AMT         NUMBER(15,2) := 0;  -- ���� + ���
    P_COLLECT_AMT     NUMBER(15,2) := 0;  -- ���� + ���
    
BEGIN
   --  �������ڰ� ���Ե� ���� ���ʱݾ��� �����´�.
    BEGIN
	    SELECT CLOSE_YM
	          ,SUM(DECODE(SELPCH_CD, 'SELPCH1',TRMEND_CREDIT_AMT,'SELPCH3',TRMEND_CREDIT_AMT,0))
	          ,SUM(DECODE(SELPCH_CD, 'SELPCH2',TRMEND_CREDIT_AMT,'SELPCH4',TRMEND_CREDIT_AMT,0))
	     INTO P_CLOSE_YM , P_BASIS_PCHS_AMT, P_BASIS_SELL_AMT
	     FROM TB_AR07M01
	    WHERE  CO_CD     = I_CO_CD
          AND  CLOSE_YM  = (SELECT MAX(CLOSE_YM) FROM TB_AR07M01 
                             WHERE CLNT_CD   = NVL(I_CLNT_CD, CLNT_CD)
                               AND SELPCH_CD = NVL(I_SELPCH , SELPCH_CD)
                               AND CLOSE_YM  < SUBSTR(I_BASIS_DT,1,6))
          AND  CLNT_CD      = NVL(I_CLNT_CD, CLNT_CD)
          AND  SELPCH_CD    = NVL(I_SELPCH , SELPCH_CD)
          AND  TAXIVC_COPRT = NVL(I_TAXIVC_COPRT , TAXIVC_COPRT)
         GROUP BY CLOSE_YM;
	    EXCEPTION WHEN OTHERS THEN P_BASIS_SELL_AMT  := 0; P_BASIS_PCHS_AMT := 0;
    END;
   -- ������ �̷��� ������ ������ ������ ���� ����߻����� �������� ���� ���.  
   IF(P_CLOSE_YM IS NULL) THEN    
     BEGIN  
      SELECT TO_CHAR(ADD_MONTHS(TO_DATE(MIN(TRST_DT),'YYYYMMDD'),-1),'YYYYMM') INTO P_CLOSE_YM 
        FROM TB_AR02M01
       WHERE TRST_CLNT_CD = NVL(I_CLNT_CD,TRST_CLNT_CD)
		 AND CO_CD        = I_CO_CD
         AND SELPCH_CD    = NVL(I_SELPCH, SELPCH_CD)
         AND TAXIVC_COPRT = NVL(I_TAXIVC_COPRT , TAXIVC_COPRT);
     END;
   END IF;
   
   -- ����/�����ڷḦ ����Ѵ�.
   -- 1. ����/����Ȯ���� �ȵǰ��� �������̺��� �����´�. 
   --    ����Ȯ�� ���ΰ� Y�ΰ�쿡�� �����Ѵ�. ����Ȯ��(���� ����)�� �ȵǾ���
   IF(nvl(I_BILG_YN,'N') = 'N') THEN 
	    BEGIN
		    SELECT 
			       SUM(DECODE(A.SELPCH_CD, 'SELPCH1', NVL(A.BILG_AMT,0) + NVL(A.BILG_VAT_AMT,0),'SELPCH3', NVL(A.BILG_AMT,0) + NVL(A.BILG_VAT_AMT,0), 0)) 
			      ,SUM(DECODE(A.SELPCH_CD, 'SELPCH2', NVL(A.BILG_AMT,0) + NVL(A.BILG_VAT_AMT,0),'SELPCH4', NVL(A.BILG_AMT,0) + NVL(A.BILG_VAT_AMT,0), 0))
			  INTO P_PCHS_AMT_AR02,  P_SELL_AMT_AR02 
			  FROM TB_AR02M01 A, TB_BM02M01 B
			 WHERE A.TRST_CLNT_CD = B.CLNT_CD (+)
		       AND REPLACE(A.TRST_DT, '-', '') BETWEEN SUBSTR(REPLACE(P_CLOSE_YM, '-', ''),1,6)||'32'
		                                           AND TO_CHAR(TO_DATE(REPLACE(I_BASIS_DT, '-', ''),'YYYYMMDD') - 1,'YYYYMMDD')
			   AND A.TRST_CLNT_CD = NVL(I_CLNT_CD,A.TRST_CLNT_CD)
			   AND A.CO_CD        = I_CO_CD
	           AND A.SELPCH_CD    = NVL(I_SELPCH, SELPCH_CD)
               AND A.TAXIVC_COPRT = NVL(I_TAXIVC_COPRT , TAXIVC_COPRT)
	           AND A.BILG_CERT_NO IS NULL;
		    EXCEPTION WHEN OTHERS THEN P_PCHS_AMT_AR02  := 0;  P_SELL_AMT_AR02 := 0;
	    END;
    ELSE
        P_PCHS_AMT_AR02  := 0;  P_SELL_AMT_AR02 := 0;
    END IF;
    -- 2. Ȯ���� �� ���� Ȯ�����̺��� �����´�.
    BEGIN
	    SELECT 
		        SUM(DECODE(AR04.SELPCH_CD, 'SELPCH1', NVL(AR04.TAX_MOA5_23,0) + NVL(AR04.TAX_MOA5_124,0), 'SELPCH3', NVL(AR04.TAX_MOA5_23,0) + NVL(AR04.TAX_MOA5_124,0), 0))
			  , SUM(DECODE(AR04.SELPCH_CD, 'SELPCH2', NVL(AR04.TAX_MOA5_23,0) + NVL(AR04.TAX_MOA5_124,0), 'SELPCH4', NVL(AR04.TAX_MOA5_23,0) + NVL(AR04.TAX_MOA5_124,0), 0))
		  INTO P_PCHS_AMT_AR04,  P_SELL_AMT_AR04 
		  FROM TB_AR04M01 AR04
		 WHERE REPLACE(AR04.SELL_DT, '-', '') BETWEEN SUBSTR(REPLACE(P_CLOSE_YM, '-', ''),1,6)||'32'
	                                           AND TO_CHAR(TO_DATE(REPLACE(I_BASIS_DT, '-', ''),'YYYYMMDD') - 1,'YYYYMMDD')
		   AND AR04.CLNT_CD      = NVL(I_CLNT_CD,AR04.CLNT_CD)
		   AND AR04.CO_CD        = I_CO_CD
           AND AR04.TAXIVC_COPRT = NVL(I_TAXIVC_COPRT , AR04.TAXIVC_COPRT)
   --        AND AR04.TAXIVC_COPRT   = (SELECT CODE_ETC FROM TB_CM05M01 CM05 WHERE CM05.CODE_ID = NVL(I_TAXIVC_COPRT , AR04.TAXIVC_COPRT) )
           AND AR04.SELPCH_CD    = NVL(I_SELPCH, AR04.SELPCH_CD);
	    EXCEPTION WHEN OTHERS THEN P_PCHS_AMT_AR04  := 0;  P_SELL_AMT_AR04 := 0;
    END;
    
    
   -- �����ڷḦ ����Ѵ�.
   -- ETRDPS01	����
   -- ETRDPS02	����
   -- ETRDPS03	���
   -- ETRDPS04	���(���޺���), ���(���޺���)-����/����  , ���԰� �������� ��������� ����ä�Ǹ� ������.
   -- ETRDPS05	���(���޺��� ����),���(���޺��� ����)-����/����, ����� �������� ��������� ����ä�Ǹ� ���� ��.
   IF(I_SELPCH = 'SELPCH1')
   THEN
		BEGIN
		    SELECT  -- SUM(DECODE(A.ETRDPS_TYP, 'ETRDPS02', A.ETRDPS_AMT, 'ETRDPS03', A.ETRDPS_AMT, 'ETRDPS04', A.ETRDPS_AMT)) 
				  -- , SUM(DECODE(A.ETRDPS_TYP, 'ETRDPS01', A.ETRDPS_AMT, 'ETRDPS03', A.ETRDPS_AMT, 'ETRDPS05', A.ETRDPS_AMT))
				   SUM(DECODE(NVL(CM05.CODE_RPRC,I_SELPCH),'SELPCH1', 0,NVL(A.ETRDPS_AMT,0))) -- ����,     ��� ���޺��� ���� 
  				 , SUM(DECODE(NVL(CM05.CODE_RPRC,I_SELPCH),'SELPCH1', NVL(A.ETRDPS_AMT,0),0))   -- ����, ���, ��� ���޺��� 
			INTO P_COLLECT_AMT ,P_PAY_AMT
			FROM TB_AR05M01 A
			 LEFT JOIN TB_BM02M01 B ON A.CLNT_CD = B.CLNT_CD
			 LEFT JOIN TB_CM05M01 CM05 ON CM05.CODE_ID = A.ETRDPS_TYP
	        WHERE						   
			      REPLACE(A.ETRDPS_DT, '-', '') BETWEEN SUBSTR(REPLACE(P_CLOSE_YM, '-', ''),1,6)||'32'
		                                            AND TO_CHAR(TO_DATE(REPLACE(I_BASIS_DT, '-', ''),'YYYYMMDD') - 1,'YYYYMMDD')
		      AND A.CLNT_CD      = NVL(I_CLNT_CD,A.CLNT_CD)
			  AND A.CO_CD        = I_CO_CD
	          AND A.TAXIVC_COPRT = NVL(I_TAXIVC_COPRT , A.TAXIVC_COPRT)
	          AND (CM05.CODE_RPRC = I_SELPCH OR  CM05.CODE_RPRC IS NULL );
	        EXCEPTION WHEN OTHERS THEN P_PAY_AMT := 0; P_COLLECT_AMT := 0;
	    END;
    ELSE
        BEGIN
 		    SELECT  -- SUM(DECODE(A.ETRDPS_TYP, 'ETRDPS02', A.ETRDPS_AMT, 'ETRDPS03', A.ETRDPS_AMT, 'ETRDPS04', A.ETRDPS_AMT)) 
				  -- , SUM(DECODE(A.ETRDPS_TYP, 'ETRDPS01', A.ETRDPS_AMT, 'ETRDPS03', A.ETRDPS_AMT, 'ETRDPS05', A.ETRDPS_AMT))
				   SUM(DECODE(NVL(CM05.CODE_RPRC,I_SELPCH),'SELPCH2', NVL(A.ETRDPS_AMT,0),0))  -- ����,     ��� ���޺��� ���� 
  				 , SUM(DECODE(NVL(CM05.CODE_RPRC,I_SELPCH),'SELPCH2', 0,NVL(A.ETRDPS_AMT,0)))  -- ����, ���, ��� ���޺��� 
			INTO P_COLLECT_AMT , P_PAY_AMT
			FROM TB_AR05M01 A
			 LEFT JOIN TB_BM02M01 B ON A.CLNT_CD = B.CLNT_CD
			 LEFT JOIN TB_CM05M01 CM05 ON CM05.CODE_ID = A.ETRDPS_TYP
	        WHERE						   
			      REPLACE(A.ETRDPS_DT, '-', '') BETWEEN SUBSTR(REPLACE(P_CLOSE_YM, '-', ''),1,6)||'32'
		                                            AND TO_CHAR(TO_DATE(REPLACE(I_BASIS_DT, '-', ''),'YYYYMMDD') - 1,'YYYYMMDD')
		      AND A.CLNT_CD      = NVL(I_CLNT_CD,A.CLNT_CD)
			  AND A.CO_CD        = I_CO_CD
	          AND A.TAXIVC_COPRT = NVL(I_TAXIVC_COPRT , A.TAXIVC_COPRT)
	          AND (CM05.CODE_RPRC = I_SELPCH OR  CM05.CODE_RPRC IS NULL );
	        EXCEPTION WHEN OTHERS THEN P_PAY_AMT := 0; P_COLLECT_AMT := 0;       
        
        END;
    END IF;
 --��� ���� +���� +����  -���� -���� : �ܾ�  
   -- ���� ä�� ��ȸ�� : ������� - ���� +����
   -- ���� ä�� ��ȸ �� : ������� + ���� - ����
   -- ��� ���� +���� +����  -���� -���� : �ܾ�  
--- ���� : ����, ����
--- �뺯 : ����, ����

--        SELECT  NVL(P_BASIS_SELL_AMT,0) -NVL(P_BASIS_PCHS_AMT,0) + NVL(P_SELL_AMT_AR02,0) + NVL(P_SELL_AMT_AR04,0)
--               + NVL(P_PAY_AMT,0) - NVL(P_PCHS_AMT_AR02,0) - NVL(P_PCHS_AMT_AR04,0) - NVL(P_COLLECT_AMT,0)
--        INTO    O_CREDIT_AMT
--        FROM    DUAL ;

        O_CREDIT_AMT  :=  NVL(P_BASIS_SELL_AMT,0)
                  + NVL(P_SELL_AMT_AR02,0)
                  + NVL(P_SELL_AMT_AR04,0)
                  + NVL(P_PAY_AMT,0)
                  - NVL(P_BASIS_PCHS_AMT,0) 
                  - NVL(P_PCHS_AMT_AR02,0) 
                  - NVL(P_PCHS_AMT_AR04,0)
                  - NVL(P_COLLECT_AMT,0);
                  
  RETURN O_CREDIT_AMT;
  
END;