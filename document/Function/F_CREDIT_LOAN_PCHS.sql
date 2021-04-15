CREATE OR REPLACE FUNCTION F_CREDIT_LOAN_PCHS(I_CD CHAR,I_CLNT_CD number, I_CO_CD varchar, I_TR_DT VARCHAR, I_AMT number)
    RETURN number is 
    PRAGMA AUTONOMOUS_TRANSACTION;

/******************************************************************************
* TYPE			: FUNCTION (Tibero)
* NAME			: F_CREDIT_LOAN_PCHS , �ŷ�ó�� ���Կ���üũ
* DEVELOPER		: Jangsub.Nam
* DESCRIPTION	: �Է¹��� �ڵ�, �ŷ�ó��ȣ, �����ڵ�,  �ݾ�, -9999:Exception �߻�)
* 	 ���� : C = ���� �ܾ� üũ, �ܾ�  Return
*		  M = �����Ҵ�ݾ�����, ó���ݾ�
*		  P = �����Ҵ�ݾװ���, ó���ݾ�
*
*		���� �ܾ׺����̳� ���� �߻��� -9999 Return
*
*	SELECT f_CREDIT_LOAN_PCHS('C', 1, 'GGS','20210101',0) FROM DUAL; -- �����ܾ�
*	SELECT f_CREDIT_LOAN_PCHS('T', 1, 'GGS','20210101',0) FROM DUAL; -- �ѿ��űݾ�
*	SELECT f_CREDIT_LOAN_PCHS('M', 1, 'GGS','20210101', 100000) FROM DUAL; �����Ҵ�ݾ� 100,000�� ����
*	SELECT f_CREDIT_LOAN_PCHS('P', 1, 'GGS','20210101', 100000) FROM DUAL; �����Ҵ�ݾ� 100,000�� ����
*
*  I_CD ����
*  I_CLNT_CD �ŷ�ó 
*  I_CO_CD ȸ��
*  I_TR_DT ��������( ��������(P)/��������(M)/��ȸ����(C) )
*  I_AMT  �ݾ�
******************************************************************************/


    O_BLCE_AMT 	number := 0;  
	O_GRP_AMT 	number := 0;	/*�׷� �ѵ� �ݾ�*/
    O_CO_AMT 	number := 0;	/*�纰 �ѵ� �ݾ�*/
    
    C_TRSP_AMT 	 number := 0;	/*�������ڱ����� �纰 ����ݾ� ����*/
    C_ETRDPS_AMT number := 0;	/*�������ڱ����� �纰 �Աݴ����*/
    
    
    C_GRP_AMT 	number := 0;	/*�׷쿩�� �ܿ� �ݾ�*/
    C_CO_AMT 	number := 0;	/*�纰���� �ܿ� �ݾ�*/
    C_CLNT_CD 	number := 0;
    C_LINK_GRP_YN 	VARCHAR(20) := '';
    C_LINK_GRP_CLNT_CD 	number := 0;
    
    REM_AMT 	number := 0;	/* �ܿ� �ݾ�*/
    
    O_GRP_PLDG_AMT number := 0;
    O_GRP_BLCE_AMT number := 0;
    O_CO_PLDG_AMT number := 0;
    O_CO_BLCE_AMT number := 0;
    
BEGIN
/**************************************************************
* -���� ���� �ܾ� üũ
*  SELECT f_CREDIT_LOAN_PCHS('C', 1, 'GGS','20210131',0) FROM DUAL; -- �����ܾ�
*   1. �纰 ���� ���� �ܾ� ����
***************************************************************/
	IF I_CD = 'C' OR I_CD = 'T' THEN

	/* �⺻ �ſ���  Detail�� �ݾ�  ����
	        Detail �ݾ� : TB_BM02D02.PLDG_AMT * TB_BM02D02.PLDG_RCOGN_RATE / 100
	       �ŷ�ó ���űݾ� ���� ���̺� �纰���űݾ� (�㺸�ݾ� * �亸�������� ) */
		SELECT max(a.CLNT_CD )AS CLNT_CD, 
			   0 AS BASIS_AMT, 
			   sum(nvl(b.PLDG_AMT*b.PLDG_RCOGN_RATE/100,0)) AS PLDG_AMT, 
  			   max(a.LINK_GRP_YN )AS LINK_GRP_YN, 
  			   max(a.LINK_GRP_CLNT_CD )AS LINK_GRP_CLNT_CD 
              into C_CLNT_CD, 
                   C_GRP_AMT, 
                   C_CO_AMT, 
                   C_LINK_GRP_YN, 
                   C_LINK_GRP_CLNT_CD 	 
		from TB_BM02M01 a LEFT OUTER JOIN TB_BM02D02 b 
							 ON a.CLNT_CD     = b.CLNT_CD
							AND b.PLDG_DIV_CD = 'PLDG03'
							AND b.USE_YN      = 'Y' 
							AND b.SELPCH_CD   = 'SELPCH1'
							AND nvl(B.EXPRTN_DT,99999999) >= TO_NUMBER(I_TR_DT) 
		WHERE a.USE_YN  = 'Y'
		  AND a.CLNT_CD = I_CLNT_CD;


		IF C_LINK_GRP_YN = 'Y' THEN  -- ���� ���Ű��� ���

	       C_GRP_AMT := 0; 
           C_CO_AMT  := 0;
			
			/* ���� �ŷ�ó �ݾ� ���� --> �⺻ �ſ���  Detail�� �ݾ�   ����
			       	Detail �ݾ� : TB_BM02D02.PLDG_AMT * TB_BM02D02.PLDG_RCOGN_RATE / 100
			*/
			SELECT max(a.LINK_GRP_CLNT_CD )AS LINK_GRP_CLNT_CD, 
			       0 AS BASIS_AMT, 
			       sum(nvl(b.PLDG_AMT*b.PLDG_RCOGN_RATE/100,0)) AS PLDG_AMT 
	               into C_LINK_GRP_CLNT_CD, 
	                    C_GRP_AMT, 
	                    C_CO_AMT
 			  FROM TB_BM02M01 a LEFT OUTER JOIN TB_BM02D02 b 
								 ON a.CLNT_CD     = b.CLNT_CD
								AND b.PLDG_DIV_CD = 'PLDG03'
								AND b.USE_YN      = 'Y' 
								AND b.SELPCH_CD   = 'SELPCH1'
								AND nvl(B.EXPRTN_DT,99999999) >= TO_NUMBER(I_TR_DT) 
			 WHERE a.LINK_GRP_CLNT_CD = C_LINK_GRP_CLNT_CD
		  	   AND a.LINK_GRP_YN      = 'Y' 
			   AND a.USE_YN           = 'Y';
				
		END IF;  --C_LINK_GRP_YN
		
	--�ŷ�ó ���űݾ� ���� ���̺� �纰���űݾ� (�㺸�ݾ� * �亸�������� )
 		SELECT sum(nvl((PLDG_AMT*PLDG_RCOGN_RATE/100),0)) 
             into O_CO_AMT
		  FROM TB_BM02D02
	  	 WHERE CLNT_CD   = I_CLNT_CD
	       AND CO_CD     = I_CO_CD
		   AND SELPCH_CD = 'SELPCH1'
		   AND PLDG_DIV_CD NOT IN ('PLDG03')
		   AND nvl(EXPRTN_DT,99999999) >= TO_NUMBER(I_TR_DT)
		   AND USE_YN    = 'Y';


		C_TRSP_AMT := 0;	--����ݾ� �ӽ� ����
		C_ETRDPS_AMT := 0;	--�Աݱݾ� �ӽ� ����
		IF  I_CD = 'C' THEN  -- ���� �� �ѵ� �ݾ� ����
			--�ŷ�ó ����ݾ� ����� ���
				SELECT sum(
					CASE WHEN nvl(BILG_AMT,0) <> 0 			--����Ȯ���ݾ�
							THEN nvl(BILG_AMT,0)			--����Ȯ���ݾ�
					     	WHEN nvl(REAL_TRST_AMT,0) <> 0 	--�Ǹ��Աݾ�
					     	THEN nvl(REAL_TRST_AMT,0) 
					     	ELSE nvl(TRST_AMT,0) 			--�������ñݾ�
					     END ) 
					into C_TRSP_AMT
				 FROM GOLDMOON.TB_AR02M01 
				WHERE co_cd        = I_CO_CD 
				  AND TRST_CLNT_CD = I_CLNT_CD
			      AND SELPCH_CD    = 'SELPCH1'
				  AND TRST_DT <= TO_NUMBER(I_TR_DT);
		
			--�ŷ�ó�� �Աޱݾ� ����� ���
				SELECT sum(nvl(ETRDPS_AMT,0)) into C_ETRDPS_AMT
				 FROM TB_AR05M01
				WHERE co_cd      = I_CO_CD
				  AND CLNT_CD    = I_CLNT_CD
				  AND ETRDPS_TYP IN ('ETRDPS02', 'ETRDPS03') 	--����, ���   ETRDPS01=����, ETRDPS02=����, ETRDPS03=���
				  AND ETRDPS_DT <= TO_NUMBER(I_TR_DT);
		END IF;

		O_BLCE_AMT := 0;

		--�纰�㺸���� �ܾ��� ���� �ִ��� Ȯ�� (�ŷ�ó��� ���űݾ� +�ŷ�ó�ſ�㺸�ݾ�+�纰��Ÿ�㺸�ݾ״� - ���Աݾ� + ���ޱݾ�)
		O_BLCE_AMT := nvl(C_GRP_AMT,0) + nvl(C_CO_AMT,0) + nvl(O_CO_AMT,0) - nvl(C_TRSP_AMT,0) + nvl(C_ETRDPS_AMT,0);

/*		IF O_BLCE_AMT < 0 THEN
	       O_BLCE_AMT := 0;
		END IF;
*/	
		RETURN O_BLCE_AMT;  --�ŷ����� �ݾ� Return (

/**************************************************************
* ���� ����
*   SELECT f_CREDIT_LOAN_PCHS('M', 1, 'GGS', '20210203', 100000) FROM DUAL; 
*   1. �纰 ���� ����ݾ�
***************************************************************/		
	ELSIF I_CD = 'M' THEN
	  BEGIN
		UPDATE TB_BM04M01
		   SET 
		       PCHS_CLMN_AMT = PCHS_CLMN_AMT + I_AMT	--���ޱݾ�
--			  ,LAST_PCHS_DTTM = I_TR_DT					--�������԰ŷ�����
		 WHERE CLNT_CD = I_CLNT_CD
		   AND CO_CD   = I_CO_CD;
		   
 	  EXCEPTION WHEN OTHERS THEN RETURN -8;
 	  END;
		commit;	
		
		RETURN 0;	/*ó���Ϸ�*/


/**************************************************************
*  ���Թ߻���
*	SELECT f_CREDIT_LOAN_PCHS('P', 1, 'GGS', '20210204',100000) FROM DUAL; 
*   1. �纰 �����ѱݾ׿� ����
***************************************************************/		
	ELSIF I_CD = 'P' THEN
	  begin			
		UPDATE TB_BM04M01
		   SET 
			   PCHS_TOT_AMT   = PCHS_TOT_AMT + I_AMT	--�����ѱݾ�
			  ,LAST_PCHS_DTTM = I_TR_DT					--�������԰ŷ�����
	 	 WHERE CLNT_CD      = I_CLNT_CD
		   AND CO_CD        = I_CO_CD;
		
		exception when others then return -9;
	  end;	
		commit;	
		
	
		RETURN 0;	/*ó���Ϸ�*/
	ELSE 
		RETURN -9997;	/* ó������ ���� */
	END IF;  --I_CD END
	      
	return -9998;
	
exception
    when others then
        dbms_output.put_line('exception occurred! (' || sqlcode || ') : ' || sqlerrm);
        return -9999;

END;