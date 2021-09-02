CREATE OR REPLACE FUNCTION GOLDMOON.F_CREDIT_LOAN(I_CD CHAR,I_CLNT_CD number, I_CO_CD varchar, I_TR_DT VARCHAR, I_AMT number)
    RETURN number is 
    PRAGMA AUTONOMOUS_TRANSACTION;
  
/******************************************************************************
* TYPE			: FUNCTION (Tibero)
* NAME			: F_CREDIT_LOAN
* DEVELOPER		: Jangsub.Nam
* DESCRIPTION	: �Է¹��� �ڵ�, �ŷ�ó��ȣ, �����ڵ�,  �ݾ�, -9999:Exception �߻�)
* 	 ���� : 
*         C = ���� �ܾ� üũ, �ܾ�  Return
*         T = �ѿ��űݾ�  Return
*
*		���� �ܾ׺����̳� ���� �߻��� -9999 Return
*
*	SELECT f_CREDIT_LOAN('C', 1, 'GGS','20210101',0) FROM DUAL; -- �����ܾ�
*	SELECT f_CREDIT_LOAN('T', 1, 'GGS','20210101',0) FROM DUAL; -- �ѿ��űݾ�
*
*  I_CD ����
*  I_CLNT_CD �ŷ�ó 
*  I_CO_CD ȸ��
*  I_TR_DT ��������( ��������(P)/��������(M)/��ȸ����(C) )
*                                 ��ǰ�׷�  PRDTGRP1:ö���, PRDTGRP2:�����
*  I_AMT  �ݾ�
******************************************************************************/

    I_PRDT_GRP   VARCHAR(20) := 'PRDTGRP1';   
    O_BLCE_TRDT_AMT 	number := 0;	/*�ŷ��ϱ��� �ܾ� ��� */  
    O_BLCE_CURR_AMT 	number := 0;	/*�����ϱ��� �ܾ� ��� */  
    O_BLCE_AMT 	number := 0;	/*���� �ܾ� */  
	O_GRP_AMT 	number := 0;	/*�׷� �ѵ� �ݾ�*/
    O_CO_AMT 	number := 0;	/*�����ϱ����纰 �ѵ� �ݾ�*/
    O_CO_AMT2 	number := 0;	/*�����ϱ����纰 �ѵ� �ݾ�*/
    
    C_TRSP_AMT 	 number := 0;	/*�������ڱ����� �纰 ����ݾ� ����*/
    C_ETRDPS_AMT number := 0;	/*�������ڱ����� �纰 �Աݴ����*/
    C_TRSP_AMT2  number := 0;	/*�������ڱ����� �纰 ����ݾ� ����*/
    C_ETRDPS_AMT2 number := 0;	/*�������ڱ����� �纰 �Աݴ����*/
      
    C_GRP_AMT 	number := 0;	/*�����ϱ����׷쿩�� �ܿ� �ݾ�*/
    C_CO_AMT 	number := 0;	/*�����ϱ����纰���� �ܿ� �ݾ�*/
    C_GRP_AMT2 	number := 0;	/*�����ϱ����׷쿩�� �ܿ� �ݾ�*/
    C_CO_AMT2 	number := 0;	/*�����ϱ����纰���� �ܿ� �ݾ�*/

    BM03_CNT number := 0;	/*BM03 �Ǽ�*/
    BM04_CNT number := 0;	/*BM04 �Ǽ�*/
    BM03_AMT number := 0;	/*�׷���뿩�� �ݿ��ݾ�*/
    BM04_AMT number := 0;	/*�纰���� �ݿ��ݾ�*/
	BM04_CELL_CLMN number := 0;
	BM04_CELL_REM number := 0;
	BM04_CELL_TOT number := 0;
	    
    C_CLNT_CD 	number := 0;
    C_LINK_GRP_YN 	VARCHAR(20) := '';
    C_LINK_GRP_CLNT_CD 	number := 0;
    
    REM_AMT 	number := 0;	/* �ܿ� �ݾ�*/
	BLCE_AMT    number := 0;	/* ������ݾ�*/
    RVS_AMT     number := 0;	/* ���, ��ǰ��  Minusó��*/
    
    O_GRP_PLDG_AMT number := 0;
    O_GRP_BLCE_AMT number := 0;
    O_CO_PLDG_AMT number := 0;
    O_CO_BLCE_AMT number := 0;
    
    MIN_CO_AMT 	number := 0;	/*���뿩���ܾ�*/
    GRP_BLCE 	number := 0;	/*�׷���뿩���ܾ�*/
    CO_BLCE 	number := 0;	/*�纰 �׷���뿩�� ���ݾ�*/
    CELL_REM  	number := 0;
	CO_PLDG  	number := 0;
	OVER_GRP_PLDG number := 0;
	OVER_GRP_BLCE  number := 0;
BEGIN
	/**************************************************************
	* -���� �ܾ� üũ
	*  SELECT f_CREDIT_LOAN('C', 1, 'GGS','20210131',0) FROM DUAL; -- �����ܾ�
	*   1. �纰 ���� �ܾ� ����
	***************************************************************/

	/*----------------------------------------------------------------------------*/
	/*  ��꼭 �������� ���� ���űݾ� ��� Start                                                  */
	/*----------------------------------------------------------------------------*/
	/* �⺻ �ſ��� �����ͱݾװ� Detail �ݾ�  2���� ����
	       	������ �׷쿩�� �ݾ� : TB_BM02M01.BASIS_CDTLN_AMT
	        Detail �ݾ� : TB_BM02D02.PLDG_AMT * TB_BM02D02.PLDG_RCOGN_RATE / 100
	       �ŷ�ó ���űݾ� ���� ���̺� �纰���űݾ� (�㺸�ݾ� * �亸�������� ) */

/*----------------------------------------------------------------------------*/
/* ����üũ �� �ѱݾ׸� ó�� �׿� ����ó�� '-7'����                                                                                                                              */
/* ����ó���� f_CREDIT_LOAN_PRDTGRP �Լ����� ��ǰ�׷캰�� ó����                                                                                    */
/* ��ǰ�׷� ���о��� �ŷ�ó�� ���� �Ѿ� �� �ܾ� ��ȸ�� ó���մϴ�                                                                                                               */
/*----------------------------------------------------------------------------*/

	IF I_CD = 'C' OR I_CD = 'T'  THEN
		I_PRDT_GRP  := 'PRDTGRP1'; 
	ELSE
		 return -7;  --����ó���� f_CREDIT_LOAN_PRDTGRP ���� ó����
	END IF;
	 
	IF I_CD = 'C' OR I_CD = 'T'  THEN
		begin
			SELECT a.CLNT_CD, 
				   SUM(nvl(A.BASIS_CDTLN_AMT,0)) AS C_GRP_AMT,                --�׷���뿩��-������
				   SUM(nvl(b.PLDG_AMT*b.PLDG_RCOGN_RATE/100,0)) AS C_CO_AMT,  --�׷���뿩�űݾ�
					   MAX(a.LINK_GRP_YN )AS C_LINK_GRP_YN, 
					   MAX(NVL(a.LINK_GRP_CLNT_CD,A.CLNT_CD))AS C_LINK_GRP_CLNT_CD 
		          into C_CLNT_CD, 
		               C_GRP_AMT, 
		               C_CO_AMT, 
		               C_LINK_GRP_YN, 
		               C_LINK_GRP_CLNT_CD 	 
			FROM TB_BM02M01 a LEFT OUTER JOIN TB_BM02D02 b 
								 ON a.CLNT_CD = b.CLNT_CD
								AND b.PLDG_DIV_CD = 'PLDG03'
								AND b.USE_YN = 'Y' 
								AND b.SELPCH_CD = 'SELPCH2'
				  				AND nvl(B.SETUP_DT,00010101) <= TO_NUMBER(I_TR_DT) 
								AND nvl(B.EXPRTN_DT,99999999) >= TO_NUMBER(I_TR_DT) 
--								AND b.PRDT_GRP = I_PRDT_GRP
			WHERE a.USE_YN  = 'Y'
			  AND a.CLNT_CD = I_CLNT_CD
			GROUP BY a.CLNT_CD;
		exception
		    when others then
		    	C_CLNT_CD := I_CLNT_CD;
	    		C_GRP_AMT := 0;
	    		C_CO_AMT  := 0;
	    		C_LINK_GRP_YN := 'N';
	    		C_LINK_GRP_CLNT_CD := I_CLNT_CD;
		end;
	
	
		IF C_LINK_GRP_YN = 'Y' THEN  -- ���� ���Ű��� ���
	
	       C_GRP_AMT := 0; 
	       C_CO_AMT  := 0;
			
			/* ���� �ŷ�ó �ݾ� ���� --> �⺻ �ſ��� �����ͱݾװ� Detail �ݾ�  2���� ����
			       	������ �׷쿩�� �ݾ� : TB_BM02M01.BASIS_CDTLN_AMT
			        Detail �ݾ� : TB_BM02D02.PLDG_AMT * TB_BM02D02.PLDG_RCOGN_RATE / 100
					*/
			begin
				SELECT NVL(a.LINK_GRP_CLNT_CD, a.CLNT_CD) as C_LINK_GRP_CLNT_CD, 
				       SUM(nvl(A.BASIS_CDTLN_AMT,0)) AS C_GRP_AMT,              --�׷���뿩��-������
				       SUM(nvl(b.PLDG_AMT*b.PLDG_RCOGN_RATE/100,0)) AS C_CO_AMT --�׷���뿩�űݾ�
		               into C_LINK_GRP_CLNT_CD, C_GRP_AMT, C_CO_AMT
				  FROM TB_BM02M01 a LEFT OUTER JOIN TB_BM02D02 b 
										 ON a.CLNT_CD = b.CLNT_CD
										AND b.PLDG_DIV_CD = 'PLDG03'
										AND b.USE_YN = 'Y' 
										AND b.SELPCH_CD = 'SELPCH2'
										AND nvl(B.SETUP_DT,00010101) <= TO_NUMBER(I_TR_DT) 
										AND nvl(B.EXPRTN_DT,99999999) >= TO_NUMBER(I_TR_DT) 
--										AND b.PRDT_GRP = I_PRDT_GRP
				  WHERE a.LINK_GRP_CLNT_CD = C_LINK_GRP_CLNT_CD
				    AND a.LINK_GRP_YN      = 'Y' 
					AND a.USE_YN           = 'Y'
		  		  GROUP BY NVL(a.LINK_GRP_CLNT_CD, a.CLNT_CD);
			exception
			    when others then
		    		C_GRP_AMT := 0;
		    		C_CO_AMT  := 0;
		    		C_LINK_GRP_CLNT_CD := I_CLNT_CD;
			end;
				
		END IF;  --C_LINK_GRP_YN

	--�ŷ�ó ���űݾ� ���� ���̺� �纰���űݾ� (�㺸�ݾ� * �亸�������� )
		begin
	 		SELECT nvl(sum(PLDG_AMT*PLDG_RCOGN_RATE/100),0)
	             into O_CO_AMT
				FROM TB_BM02D02
				WHERE CLNT_CD  = I_CLNT_CD
			      AND CO_CD    = nvl(I_CO_CD,CO_CD)
				  AND PLDG_DIV_CD NOT IN ('PLDG03')
				  AND SELPCH_CD = 'SELPCH2'
				  AND nvl(SETUP_DT,00010101) <= TO_NUMBER(I_TR_DT) 
				  AND nvl(EXPRTN_DT,99999999) >= TO_NUMBER(I_TR_DT)
--				  AND PRDT_GRP  = I_PRDT_GRP
				  AND USE_YN    = 'Y';
		exception 
		    when others then O_CO_AMT := 0;
		end;
	/*----------------------------------------------------------------------------*/
	/*  ��꼭 �������� ���� ���űݾ� ��� END                                                    */
	/*----------------------------------------------------------------------------*/
	
	END IF;
			
	IF I_CD = 'C' OR I_CD = 'T' THEN

	/*----------------------------------------------------------------------------*/
	/*  ��꼭 �������� ���� �ܾ� ��� Start                                                    */
	/*----------------------------------------------------------------------------*/
		C_TRSP_AMT := 0;	--����ݾ� �ӽ� ����
		C_ETRDPS_AMT := 0;	--�Աݱݾ� �ӽ� ����
		IF  I_CD = 'C' THEN  -- ���� �� �ѵ� �ݾ� ����
			--�׷���뿩�� ����ݾ� ����(�Ϻ��ڷ� ������ ����)
			begin
				SELECT NVL(a.GRP_BLCE_AMT,0)
				  INTO GRP_BLCE			--�׷���뿩���ܾ�
				  FROM TB_BM03M01 a 
				 WHERE a.CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD);
--				   AND a.PRDT_GRP = I_PRDT_GRP;
			exception
			    when others then GRP_BLCE := 0;
    		end;
				
			--�ŷ�ó ����ݾ� ����� ���
			begin
				SELECT SUM(DECODE(SELPCH_CD,'SELPCH2',nvl(BILG_AMT,0) + nvl(BILG_VAT_AMT,0),0))   -- û���ݾ� + û���ΰ����ݾ�
				    -  SUM(DECODE(SELPCH_CD,'SELPCH1',nvl(BILG_AMT,0) + nvl(BILG_VAT_AMT,0),0))   -- ���ޱݾ� + ���޺ΰ����ݾ�
					into C_TRSP_AMT
				 FROM TB_AR02M01 a LEFT OUTER JOIN TB_BM01M01 b
				                    ON a.TRST_PRDT_CD = b.PRDT_CD  
				WHERE CO_CD        = NVL(I_CO_CD,CO_CD) 
				  AND TRST_CLNT_CD = I_CLNT_CD
--			      AND SELPCH_CD    = 'SELPCH2'
--			      AND NVL(b.PRDT_GRP,'PRDTGRP1')  = I_PRDT_GRP
				  AND TRST_DT      <= TO_NUMBER(I_TR_DT);
			exception
			    when others then C_TRSP_AMT := 0;
    		end;
				
	
			--�ŷ�ó�� �Աޱݾ� ����� ���
			begin
			   SELECT sum(DECODE(NVL(CODE_RPRC,'A'),'SELPCH1',0, nvl(D02.ETRDPS_AMT,0)))      /* ����, ���,  */
			        - sum(DECODE(NVL(CODE_RPRC,'A'),'SELPCH2',0, nvl(D02.ETRDPS_AMT,0)))      /* ����, ���,  */
			           into C_ETRDPS_AMT
				 FROM TB_AR05D02  D02
				 INNER JOIN TB_AR05M01 M01 ON M01.ETRDPS_SEQ = D02.ETRDPS_SEQ
				 INNER JOIN TB_CM05M01 CM05 ON CODE_ID = M01.ETRDPS_TYP 
				WHERE D02.CO_CD      = NVL(I_CO_CD,D02.CO_CD)
				  AND D02.CLNT_CD    = I_CLNT_CD
--				  AND PRDT_GRP       = I_PRDT_GRP
				  AND D02.ETRDPS_DT <= TO_NUMBER(I_TR_DT);
			exception
			    when others then C_ETRDPS_AMT := 0;
    		end;
		END IF;

		--�纰�㺸���� �ܾ��� ���� �ִ��� Ȯ�� (�ŷ�ó��� ���űݾ� +�ŷ�ó�ſ�㺸�ݾ�+�纰��Ÿ�㺸�ݾ״� - ����ݾ� + �Աݱݾ�)
		O_BLCE_TRDT_AMT := 0;
		IF I_CD = 'T' OR NVL(C_LINK_GRP_CLNT_CD,0) =  I_CLNT_CD THEN
			O_BLCE_TRDT_AMT := nvl(C_GRP_AMT,0) + nvl(C_CO_AMT,0) + nvl(O_CO_AMT,0) - nvl(C_TRSP_AMT,0) + nvl(C_ETRDPS_AMT,0);
		ELSE
			O_BLCE_TRDT_AMT := nvl(C_GRP_AMT,0) + nvl(GRP_BLCE,0) + nvl(O_CO_AMT,0) - nvl(C_TRSP_AMT,0) + nvl(C_ETRDPS_AMT,0);
		END IF;
	/*----------------------------------------------------------------------------*/
	/*  ��꼭 �������� ���� �ܾ� ��� End                                                    */
	/*----------------------------------------------------------------------------*/
	
		O_BLCE_AMT := O_BLCE_TRDT_AMT;
--		O_BLCE_AMT := 100000000000; --�ӽ��׽�Ʈ�� ���� �Ҵ�޾� �ΰ�

		RETURN O_BLCE_AMT;  --�ŷ����� �ݾ� Return ==> (I_CD = 'C' OR I_CD = 'T') 

	ELSE     --I_CD �����ڵ�
		RETURN -9997;	/* ó������ ���� */
	END IF;  --I_CD END
	      
	return -9998;
	
exception
    when others then
        dbms_output.put_line('exception occurred! (' || sqlcode || ') : ' || sqlerrm);
        return -9999;

END;