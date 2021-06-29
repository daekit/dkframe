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
*		  M = �����Ҵ�ݾ�����, ó���ݾ� --> �������
*		  P = �����Ҵ�ݾװ���, ó���ݾ� --> ����
*		  D = �����Ҵ�ݾ�����, ó���ݾ� --> �Ա�(deposit)
*
*		���� �ܾ׺����̳� ���� �߻��� -9999 Return
*
*	SELECT f_CREDIT_LOAN('C', 1, 'GGS','PRDTGRP1','20210101',0) FROM DUAL; -- �����ܾ�
*	SELECT f_CREDIT_LOAN('T', 1, 'GGS','PRDTGRP1','20210101',0) FROM DUAL; -- �ѿ��űݾ�
*	SELECT f_CREDIT_LOAN('M', 1, 'GGS','PRDTGRP1','20210101', 100000) FROM DUAL; �����Ҵ�ݾ� 100,000�� ����
*	SELECT f_CREDIT_LOAN('P', 1, 'GGS','PRDTGRP1','20210101', 100000) FROM DUAL; �����Ҵ�ݾ� 100,000�� ����
*
*  I_CD ����
*  I_CLNT_CD �ŷ�ó 
*  I_CO_CD ȸ��
*  I_TR_DT ��������( ��������(P)/��������(M)/��ȸ����(C) )
*  I_PRDT_GRP ��ǰ�׷�  PRDTGRP1:ö���, PRDTGRP2:�����
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
								AND b.PRDT_GRP = I_PRDT_GRP
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
										AND b.PRDT_GRP = I_PRDT_GRP
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
				  AND PRDT_GRP  = I_PRDT_GRP
				  AND USE_YN    = 'Y';
		exception
		    when others then O_CO_AMT := 0;
		end;

	/*----------------------------------------------------------------------------*/
	/*  ��꼭 �������� ���� ���űݾ� ��� END                                                    */
	/*----------------------------------------------------------------------------*/
	
	/*----------------------------------------------------------------------------*/
	/*  �������� ���� ���űݾ� ��� Start                                                    */
	/*----------------------------------------------------------------------------*/
	/* �⺻ �ſ��� �����ͱݾװ� Detail �ݾ�  2���� ����
	       	������ �׷쿩�� �ݾ� : TB_BM02M01.BASIS_CDTLN_AMT
	        Detail �ݾ� : TB_BM02D02.PLDG_AMT * TB_BM02D02.PLDG_RCOGN_RATE / 100
	       �ŷ�ó ���űݾ� ���� ���̺� �纰���űݾ� (�㺸�ݾ� * �亸�������� ) */
		begin
			SELECT a.CLNT_CD, 
				   SUM(nvl(A.BASIS_CDTLN_AMT,0)) AS C_GRP_AMT2, 
				   SUM(nvl(b.PLDG_AMT*b.PLDG_RCOGN_RATE/100,0)) AS C_CO_AMT2, 
					   MAX(a.LINK_GRP_YN )AS C_LINK_GRP_YN, 
					   MAX(NVL(a.LINK_GRP_CLNT_CD, A.CLNT_CD))AS C_LINK_GRP_CLNT_CD 
		          into C_CLNT_CD, 
		               C_GRP_AMT2, 
		               C_CO_AMT2, 
		               C_LINK_GRP_YN, 
		               C_LINK_GRP_CLNT_CD 	 
			FROM TB_BM02M01 a LEFT OUTER JOIN TB_BM02D02 b 
								 ON a.CLNT_CD = b.CLNT_CD
								AND b.PLDG_DIV_CD = 'PLDG03'
								AND b.USE_YN = 'Y' 
								AND b.SELPCH_CD = 'SELPCH2'
				  				AND nvl(B.SETUP_DT,00010101) <= TO_CHAR(SYSDATE,'YYYYMMDD')
								AND nvl(B.EXPRTN_DT,99999999) >= TO_CHAR(SYSDATE,'YYYYMMDD') 
								AND b.PRDT_GRP = I_PRDT_GRP
			WHERE a.USE_YN  = 'Y'
			  AND a.CLNT_CD = I_CLNT_CD
			GROUP BY a.CLNT_CD;
		exception
		    when others then
		    	C_CLNT_CD := I_CLNT_CD;
	    		C_GRP_AMT2 := 0;
	    		C_CO_AMT2  := 0;
	    		C_LINK_GRP_YN := 'N';
	    		C_LINK_GRP_CLNT_CD := I_CLNT_CD;
		end;				
	
	
		IF C_LINK_GRP_YN = 'Y' THEN  -- ���� ���Ű��� ���
	
	       C_GRP_AMT2 := 0; 
	       C_CO_AMT2  := 0;
			
			/* ���� �ŷ�ó �ݾ� ���� --> �⺻ �ſ��� �����ͱݾװ� Detail �ݾ�  2���� ����
			       	������ �׷쿩�� �ݾ� : TB_BM02M01.BASIS_CDTLN_AMT
			        Detail �ݾ� : TB_BM02D02.PLDG_AMT * TB_BM02D02.PLDG_RCOGN_RATE / 100
					*/
			begin
				SELECT NVL(a.LINK_GRP_CLNT_CD,A.CLNT_CD) as C_LINK_GRP_CLNT_CD, 
				       SUM(nvl(A.BASIS_CDTLN_AMT,0)) AS C_GRP_AMT2, 
				       SUM(nvl(b.PLDG_AMT*b.PLDG_RCOGN_RATE/100,0)) AS C_CO_AMT2 
		               into C_LINK_GRP_CLNT_CD, C_GRP_AMT2, C_CO_AMT2
				  FROM TB_BM02M01 a LEFT OUTER JOIN TB_BM02D02 b 
										 ON a.CLNT_CD = b.CLNT_CD
										AND b.PLDG_DIV_CD = 'PLDG03'
										AND b.USE_YN = 'Y' 
										AND b.SELPCH_CD = 'SELPCH2'
										AND nvl(B.SETUP_DT,00010101) <= TO_CHAR(SYSDATE,'YYYYMMDD') 
										AND nvl(B.EXPRTN_DT,99999999) >= TO_CHAR(SYSDATE,'YYYYMMDD') 
										AND b.PRDT_GRP = I_PRDT_GRP
				  WHERE a.LINK_GRP_CLNT_CD = C_LINK_GRP_CLNT_CD
				    AND a.LINK_GRP_YN      = 'Y' 
					AND a.USE_YN           = 'Y'
		  		  GROUP BY NVL(a.LINK_GRP_CLNT_CD,A.CLNT_CD);
			exception
			    when others then
		    		C_GRP_AMT2 := 0;
		    		C_CO_AMT2  := 0;
		    		C_LINK_GRP_CLNT_CD := I_CLNT_CD;
			end;				
		END IF;  --C_LINK_GRP_YN

		--�ŷ�ó ���űݾ� ���� ���̺� �纰���űݾ� (�㺸�ݾ� * �亸�������� )
		begin
	 		SELECT nvl(sum(PLDG_AMT*PLDG_RCOGN_RATE/100),0)
	             into O_CO_AMT2
				FROM TB_BM02D02
				WHERE CLNT_CD  = I_CLNT_CD
			      AND CO_CD    = nvl(I_CO_CD,CO_CD)
				  AND PLDG_DIV_CD NOT IN ('PLDG03')
				  AND SELPCH_CD = 'SELPCH2'
				  AND nvl(SETUP_DT,00010101) <= TO_CHAR(SYSDATE,'YYYYMMDD')
				  AND nvl(EXPRTN_DT,99999999) >= TO_CHAR(SYSDATE,'YYYYMMDD')
				  AND PRDT_GRP = I_PRDT_GRP
				  AND USE_YN    = 'Y';
		exception
		    when others then O_CO_AMT2 := 0;
		end;

	/*----------------------------------------------------------------------------*/
	/*  �������� ���� ���űݾ� ��� End                                                    */
	/*----------------------------------------------------------------------------*/

	ELSE --����(P), �������(M), �Ա�(D)ó���� �׷����� �����ִ��� üũ��
		begin
			SELECT a.CLNT_CD, 
				   a.LINK_GRP_YN AS C_LINK_GRP_YN, 
				   NVL(a.LINK_GRP_CLNT_CD, a.CLNT_CD) AS C_LINK_GRP_CLNT_CD 
		          into C_CLNT_CD, 
		               C_LINK_GRP_YN, 
		               C_LINK_GRP_CLNT_CD 	 
			FROM TB_BM02M01 a 
			WHERE a.USE_YN  = 'Y'
			  AND a.CLNT_CD = I_CLNT_CD;
		exception
	    	when others then
	    		C_CLNT_CD := I_CLNT_CD;
	    		C_LINK_GRP_YN := 'N';
	    		C_LINK_GRP_CLNT_CD := I_CLNT_CD;
		end;
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
				 WHERE a.CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
				   AND a.PRDT_GRP = I_PRDT_GRP;
			exception
			    when others then GRP_BLCE := 0;
    		end;
				
			--�ŷ�ó ����ݾ� ����� ���
			begin
				SELECT SUM(nvl(BILG_AMT,0)) + SUM(nvl(BILG_VAT_AMT,0)) as C_TRSP_AMT  -- û���ݾ� + û���ΰ����ݾ�
					into C_TRSP_AMT
				 FROM TB_AR02M01 a LEFT OUTER JOIN TB_BM01M01 b
				                    ON a.TRST_PRDT_CD = b.PRDT_CD  
				WHERE CO_CD        = NVL(I_CO_CD,CO_CD) 
				  AND TRST_CLNT_CD = I_CLNT_CD
			      AND SELPCH_CD    = 'SELPCH2'
			      AND NVL(b.PRDT_GRP,'PRDTGRP1')  = I_PRDT_GRP
				  AND TRST_DT      <= TO_NUMBER(I_TR_DT);
			exception
			    when others then C_TRSP_AMT := 0;
    		end;
				
	
			--�ŷ�ó�� �Աޱݾ� ����� ���
			begin
/*AR05D02�� ���� start
			   SELECT sum(nvl(ETRDPS_AMT,0)) into C_ETRDPS_AMT
				 FROM TB_AR05M01
				WHERE CO_CD      = NVL(I_CO_CD,CO_CD)
				  AND CLNT_CD    = I_CLNT_CD
				  AND ETRDPS_TYP IN ('ETRDPS01', 'ETRDPS03', 'ETRDPS05') 	--����, ���   ETRDPS01=����, ETRDPS02=����, ETRDPS03=���, ETRDPS05=���޺�������
				  AND ETRDPS_DT <= TO_NUMBER(I_TR_DT);
AR05D02�� ���� End */
			   SELECT sum(nvl(ETRDPS_AMT,0)) into C_ETRDPS_AMT
				 FROM TB_AR05D02
				WHERE CO_CD      = NVL(I_CO_CD,CO_CD)
				  AND CLNT_CD    = I_CLNT_CD
				  AND PRDT_GRP   = I_PRDT_GRP
				  AND ETRDPS_DT <= TO_NUMBER(I_TR_DT);
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

	/*----------------------------------------------------------------------------*/
	/*  �������� ���� �ܾ� ��� Start                                                    */
	/*----------------------------------------------------------------------------*/
		C_TRSP_AMT := 0;	--����ݾ� �ӽ� ����
		C_ETRDPS_AMT := 0;	--�Աݱݾ� �ӽ� ����
		IF  I_CD = 'C' THEN  -- ���� �� �ѵ� �ݾ� ����
			--�ŷ�ó ����ݾ� ����� ���
			begin
				SELECT SUM(nvl(BILG_AMT,0)) + SUM(nvl(BILG_VAT_AMT,0)) as C_TRSP_AMT  -- û���ݾ� + û���ΰ����ݾ�
					into C_TRSP_AMT2
				 FROM TB_AR02M01 a LEFT OUTER JOIN TB_BM01M01 b
				                    ON a.TRST_PRDT_CD = b.PRDT_CD  
				WHERE CO_CD        = NVL(I_CO_CD,CO_CD) 
				  AND TRST_CLNT_CD = I_CLNT_CD
			      AND SELPCH_CD    = 'SELPCH2'
			      AND NVL(b.PRDT_GRP,'PRDTGRP1') = I_PRDT_GRP
				  AND TRST_DT      <= TO_CHAR(SYSDATE,'YYYYMMDD');
			exception
			    when others then C_TRSP_AMT2 := 0;
    		end;
		
			--�ŷ�ó�� �Աޱݾ� ����� ���
			begin
/*AR05D02�� ���� start
				SELECT sum(nvl(ETRDPS_AMT,0)) into C_ETRDPS_AMT2
				 FROM TB_AR05M01
				WHERE CO_CD      = NVL(I_CO_CD,CO_CD)
				  AND CLNT_CD    = I_CLNT_CD
				  AND ETRDPS_TYP IN ('ETRDPS01', 'ETRDPS03', 'ETRDPS05') 	--����, ���   ETRDPS01=����, ETRDPS02=����, ETRDPS03=���, ETRDPS05=���޺�������
				  AND ETRDPS_DT <= TO_CHAR(SYSDATE,'YYYYMMDD');
AR05D02�� ���� End */
				SELECT sum(nvl(ETRDPS_AMT,0)) into C_ETRDPS_AMT2
				 FROM TB_AR05D02
				WHERE CO_CD      = NVL(I_CO_CD,CO_CD)
				  AND CLNT_CD    = I_CLNT_CD
				  AND PRDT_GRP   = I_PRDT_GRP
				  AND ETRDPS_DT <= TO_CHAR(SYSDATE,'YYYYMMDD');
			exception
			    when others then C_ETRDPS_AMT2 := 0;
    		end;
		END IF;

		--�纰�㺸���� �ܾ��� ���� �ִ��� Ȯ�� (�ŷ�ó��� ���űݾ� +�ŷ�ó�ſ�㺸�ݾ�+�纰��Ÿ�㺸�ݾ״� - ����ݾ� + �Աݱݾ�)
		O_BLCE_CURR_AMT := 0;
		IF I_CD = 'T' OR NVL(C_LINK_GRP_CLNT_CD,0) =  I_CLNT_CD THEN
			O_BLCE_CURR_AMT := nvl(C_GRP_AMT2,0) + nvl(C_CO_AMT2,0) + nvl(O_CO_AMT2,0) - nvl(C_TRSP_AMT2,0) + nvl(C_ETRDPS_AMT2,0);
		ELSE
			O_BLCE_CURR_AMT := nvl(C_GRP_AMT2,0) + nvl(GRP_BLCE,0) + nvl(O_CO_AMT2,0) - nvl(C_TRSP_AMT2,0) + nvl(C_ETRDPS_AMT2,0);
		END IF;
	/*----------------------------------------------------------------------------*/
	/*  �������� ���� �ܾ� ��� End                                                    */
	/*----------------------------------------------------------------------------*/
	
		/* ������ ���� �ܾ�(O_BLCE_CURR_AMT)�� �ŷ����� �ܾ�(O_BLCE_TRDT_AMT)�� �����ݾ����� �����ܾ� ó��  */
		IF O_BLCE_TRDT_AMT > O_BLCE_CURR_AMT THEN
			O_BLCE_AMT := O_BLCE_CURR_AMT;
		ELSE
			O_BLCE_AMT := O_BLCE_TRDT_AMT;
		END IF;
		
--		O_BLCE_AMT := 100000000000; --�ӽ��׽�Ʈ�� ���� �Ҵ�޾� �ΰ�

		RETURN O_BLCE_AMT;  --�ŷ����� �ݾ� Return ==> (I_CD = 'C' OR I_CD = 'T') 

/**************************************************************
* ����߻��� (���� ����)
*   SELECT f_CREDIT_LOAN('P', 1, 'GGS', '20210203', 100000) FROM DUAL; �����Ҵ�ݾ� 100,000�� ����
*   1. �纰 ���� ���Աݾ�
*      - ù°. �׷� ���뿩�űݾ��� �����ϰ� 
*      - ��°. �׷쿩�� ���� �ܾ� �߻��� �纰���űݾ� �����Ѵ�. 
***************************************************************/		
	ELSIF I_CD = 'P' THEN --����߻�ó��
	  BEGIN

/*		-- �ѿ��űݾ� = �׷쿩�űݾ� + �纰 ���űݾ�
		O_BLCE_TRDT_AMT := C_CO_AMT + NVL(O_CO_AMT,0);   	--�����ϱ��� �ܾ�
		O_BLCE_CURR_AMT := C_CO_AMT2 + NVL(O_CO_AMT,0);		-- �����ϱ��� �ܾ�

	  	-- �������ڱ��ؿ����ܾ�(O_BLCE_TRDT_AMT) > ������ ���� �����ܾ�(O_BLCE_CURR_AMT)
	  	-- �ΰ��� �������� ����
		IF O_BLCE_TRDT_AMT > O_BLCE_CURR_AMT THEN  --��밡�� ���� �ݾ� ����
			MIN_CO_AMT := O_BLCE_CURR_AMT;
		ELSE
			MIN_CO_AMT := O_BLCE_TRDT_AMT;
		END IF;

	  	-- ���뿩���ܾ�(MIN_CO_AMT) > �űԸ���ݾ�(I_AMT)
		IF MIN_CO_AMT < I_AMT THEN	--�׷쿩�űݾ� < ����ݾ�  : �����ܾ��� �����ϸ� ����!!!
			--�����ܾ��� �����մϴ�.!!!!
			RETURN -1;
		ELSE --�����ܾ��� ��� �����ϸ�
*/
			--�׷���뿩�� ����ݾ� ����
			begin
				SELECT NVL(a.GRP_BLCE_AMT,0)  --���뿩�� �ܾ� Ȯ��
				  INTO GRP_BLCE
				  FROM TB_BM03M01 a
				 WHERE a.CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
				   AND a.PRDT_GRP = I_PRDT_GRP;
			exception
			    when others then GRP_BLCE := 0;
    		end;
			 
			--�纰���� ����ݾ� ����
			begin
				SELECT NVL(b.GRP_BLCE_AMT,0),	--�׷� ���ſ��� ����� �ݾ� ����
				       NVL(b.CELL_REM_AMT,0),   --�纰 �����ܾ�
				       NVL(b.CO_BLCE_AMT,0)     --�纰 ���� �ܾ�
				  INTO CO_BLCE,		--���纰 �׷���뿩�� ���ݾ�
				  	   CELL_REM, 	--�����ܾ� (����-����)
				  	   BLCE_AMT 	--
				  FROM TB_BM04M01 b
				 WHERE b.CLNT_CD  = I_CLNT_CD
	               AND b.CO_CD    = I_CO_CD
	               AND b.PRDT_GRP = I_PRDT_GRP;
			exception
			    when others then
			    	CO_BLCE := 0;
			    	CELL_REM := 0;
			    	BLCE_AMT := 0;
    		end;
			
			--�׷���뿩�� ����ݾ� ����
			IF (NVL(BLCE_AMT,0)) > I_AMT THEN
				BM04_AMT := I_AMT;
				BM03_AMT := 0;
			ELSE
				BM04_AMT := BLCE_AMT;
				BM03_AMT := I_AMT- BM04_AMT;
			END IF;

			SELECT COUNT(*) INTO BM03_CNT FROM TB_BM03M01 
			 WHERE CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
			   AND PRDT_GRP = I_PRDT_GRP;
	
			IF BM03_CNT > 0 THEN
				UPDATE TB_BM03M01
				   SET GRP_BLCE_AMT    = NVL(GRP_BLCE_AMT,0) - BM03_AMT	--�����ܾ�
				 WHERE CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
				   AND PRDT_GRP = I_PRDT_GRP;
			ELSE

				INSERT INTO TB_BM03M01
					(CLNT_CD, PRDT_GRP, GRP_PLDG_AMT, GRP_BLCE_AMT, USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
				VALUES
					(NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD), I_PRDT_GRP, 0, (0-BM03_AMT), 'Y', 'DB_TRIGER', 'F_CREDIT_LOAN', SYSDATE);
			END IF;
				
			SELECT COUNT(*) INTO BM04_CNT FROM TB_BM04M01 
			 WHERE CLNT_CD  = I_CLNT_CD 
	           AND CO_CD    = I_CO_CD
	           AND PRDT_GRP = I_PRDT_GRP;

			IF BM04_CNT > 0 THEN
				UPDATE TB_BM04M01
				   SET CO_BLCE_AMT    = nvl(CO_BLCE_AMT,0) - BM04_AMT		--�����ܾ�
				      ,CELL_TOT_AMT   = nvl(CELL_TOT_AMT,0) + I_AMT			--����ݾ�
				      ,CELL_REM_AMT   = nvl(CELL_REM_AMT,0) + BM04_AMT		--�����ܾ�
				      ,GRP_BLCE_AMT   = nvl(GRP_BLCE_AMT,0) + BM03_AMT		--�׷쿩�Ż��ݾ�
				      ,LAST_CELL_DTTM = I_TR_DT						--������������	
				 WHERE CLNT_CD = I_CLNT_CD
				   AND CO_CD   = I_CO_CD
		           AND PRDT_GRP = I_PRDT_GRP;
			ELSE
				INSERT INTO TB_BM04M01
					(CLNT_CD, CO_CD, PRDT_GRP,
					CO_PLDG_AMT, CO_BLCE_AMT, 
					CELL_TOT_AMT, CELL_CLMN_AMT, 
					PCHS_TOT_AMT, PCHS_CLMN_AMT, 
					CELL_REM_AMT, GRP_BLCE_AMT,
					LAST_CELL_DTTM,
					USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
				VALUES
				   (I_CLNT_CD, I_CO_CD, I_PRDT_GRP,
					0, (0-BM04_AMT),
					I_AMT, 0, 
					0, 0, 
					BM04_AMT, BM03_AMT,
					I_TR_DT,
					'Y', 'DB_TRIGER', 'F_CREDIT_LOAN', SYSDATE);
			END IF;
	
			   
--		END IF;

 	  EXCEPTION WHEN OTHERS THEN RETURN -8;
 	  END;
		commit;	
		
		RETURN 0;	/*ó���Ϸ�==>I_CD = 'P' THEN --����߻�ó��*/


/**************************************************************
* ���� ���ó�� ��
*	SELECT f_CREDIT_LOAN('D', 1, 'GGS', '20210204',-100000) FROM DUAL; �����Ҵ�ݾ� 100,000�� ����
*   1. �纰���� �ݾ��� ���� �����ϰ�
*   2. �׷쿩�� �ݾ׸�ŭ ������.  ���࿡ �׷뿩�ſ����ݾ��� �纰���ſ� �ݿ�
***************************************************************/		
	ELSIF I_CD = 'D' AND I_AMT < 0 THEN --D:���� ���  ó���� FLAG ������Ҵ� �ݾ��� Minus�� ó����
	  begin			

		  begin			
			SELECT NVL(b.GRP_BLCE_AMT,0),
			       NVL(b.CELL_REM_AMT,0),
			       NVL(b.CO_BLCE_AMT,0),
			       NVL(b.CO_PLDG_AMT,0)
			  INTO GRP_BLCE,		--���纰 �׷���뿩�� ���ݾ�
			  	   CELL_REM, 		--�����ܾ� (����-����)
			  	   CO_BLCE,
			  	   CO_PLDG
			  FROM TB_BM04M01 B
			 WHERE B.CLNT_CD  = I_CLNT_CD
	           AND B.CO_CD    = I_CO_CD
	           AND B.PRDT_GRP = I_PRDT_GRP;
		exception
		    when others then
		    	GRP_BLCE := 0;
		    	CELL_REM := 0;
		    	CO_BLCE := 0;
		    	CO_PLDG := 0;
		end;

		--�������  ���̳ʽ� �ݾ����� ���� �߻�
		RVS_AMT := I_AMT * -1;

		--1. �纰���űݾ׿��� ������ ���뿩�ſ�������ó��
		IF  NVL(CO_BLCE,0) > RVS_AMT THEN
			BM04_AMT := RVS_AMT;
			BM03_AMT := 0;
		ELSE
			BM04_AMT := CO_BLCE;
			BM03_AMT := RVS_AMT - CO_BLCE;
--*********************************************************************
			begin
				SELECT GRP_PLDG_AMT, GRP_BLCE_AMT
				  INTO OVER_GRP_PLDG, OVER_GRP_BLCE FROM TB_BM03M01 
				 WHERE CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD);
			exception
			    when others then
			    	OVER_GRP_PLDG := 0;
			    	OVER_GRP_BLCE := 0;
			end;
			--�ӱ�����ܾ��� ���������ݾ��� �����ϸ� �������� �����ݾ׿� �ݿ���
			IF NVL(OVER_GRP_BLCE,0) < BM03_AMT THEN
			-- 03�ݾ��� �ѵ������� �����ϰ� �������� 04�ݾ����� ��ȯ ó�� �ʿ��մϴ�.
				BM03_AMT := NVL(OVER_GRP_BLCE,0);
				BM04_AMT := RVS_AMT - BM03_AMT;
			END IF;
	--*********************************************************************
		END IF;

		/* ����ó���� �纰 �����Ҵ� �ݾ׸�ŭ ������ �׷쿩�űݾ� ������  */ 
		SELECT COUNT(*) INTO BM04_CNT FROM TB_BM04M01 
		 WHERE CLNT_CD = I_CLNT_CD 
           AND CO_CD   = I_CO_CD
           AND PRDT_GRP = I_PRDT_GRP;

		IF BM04_CNT > 0 THEN
			UPDATE TB_BM04M01
			   SET CO_BLCE_AMT    = NVL(CO_BLCE_AMT,0) - BM04_AMT	--�����ܾ�
				  ,CELL_REM_AMT   = NVL(CELL_REM_AMT,0) + BM04_AMT	--�ܻ�����ܾ�
				  ,CELL_CLMN_AMT  = NVL(CELL_CLMN_AMT,0) - RVS_AMT	--���ݱݾ�
				  ,GRP_BLCE_AMT   = NVL(GRP_BLCE_AMT,0) + BM03_AMT	--�׷쿩���Ҵ�ݾ�
				  ,LAST_CLMN_DTTM = I_TR_DT							--������������	
			 WHERE CLNT_CD  = I_CLNT_CD
			   AND CO_CD    = I_CO_CD
			   AND PRDT_GRP = I_PRDT_GRP;
		ELSE
			INSERT INTO TB_BM04M01
				(CLNT_CD, CO_CD, PRDT_GRP, 
				CO_PLDG_AMT, CO_BLCE_AMT, 
				CELL_TOT_AMT, CELL_CLMN_AMT, 
				PCHS_TOT_AMT, PCHS_CLMN_AMT, 
				CELL_REM_AMT, GRP_BLCE_AMT,
				LAST_CELL_DTTM,
				USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
			VALUES
			   (I_CLNT_CD, I_CO_CD, I_PRDT_GRP, 
				0, (0-BM04_AMT),
				0, (0-RVS_AMT), 
				0, 0, 
				BM04_AMT, BM03_AMT,
				I_TR_DT,
				'Y', 'DB_TRIGER', 'F_CREDIT_LOAN', SYSDATE);
		END IF;
   

		SELECT COUNT(*) INTO BM03_CNT FROM TB_BM03M01 
		 WHERE CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
		   AND PRDT_GRP = I_PRDT_GRP;

		IF BM03_CNT > 0 THEN
			UPDATE TB_BM03M01
			   SET GRP_BLCE_AMT   = NVL(GRP_BLCE_AMT,0) - BM03_AMT		--�����ܾ�
			 WHERE CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
		       AND PRDT_GRP = I_PRDT_GRP;
		ELSE

			INSERT INTO TB_BM03M01
				(CLNT_CD, PRDT_GRP, GRP_PLDG_AMT, GRP_BLCE_AMT, USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
			VALUES
				(NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD), I_PRDT_GRP, 0, (0-BM03_AMT), 'Y', 'DB_TRIGER', 'F_CREDIT_LOAN', SYSDATE);
		END IF;
			
	
		exception when others then return -9;
		
	  end;	
		commit;	
			
		RETURN 0;	/*ó���Ϸ�==>I_CD = 'D' AND I_AMT < 0 THEN --D:���� ���  ó���� FLAG ������Ҵ� �ݾ��� Minus�� ó����*/

/**************************************************************
* ����ó�� ��
*	SELECT f_CREDIT_LOAN('M', 1, 'GGS', '20210204',100000) FROM DUAL; ������� �����Ҵ�ݾ� 100,000�� ����
*	SELECT f_CREDIT_LOAN('D', 1, 'GGS', '20210204',100000) FROM DUAL; �Ա� �����Ҵ�ݾ� 100,000�� ����
*   1. �׷� ���� �ݾ��� ���� �����ϰ�
*   2. �������� ���� ó���Ѵ�.
***************************************************************/		
	ELSIF I_CD = 'M' OR I_CD = 'D' THEN --M:�������, D:���� �ݾ� ó���� FLAG
	  begin			
		  begin			
				SELECT NVL(b.GRP_BLCE_AMT,0),
				       NVL(b.CELL_REM_AMT,0),
				       NVL(b.CO_BLCE_AMT,0),
				       NVL(b.CO_PLDG_AMT,0)
				  INTO GRP_BLCE,		--���纰 �׷���뿩�� ���ݾ�
				  	   CELL_REM, 		--�����ܾ� (����-����)
				  	   CO_BLCE,
				  	   CO_PLDG
				  FROM TB_BM04M01 B
				 WHERE B.CLNT_CD  = I_CLNT_CD
		           AND B.CO_CD    = I_CO_CD
		           AND B.PRDT_GRP = I_PRDT_GRP;
			exception
			    when others then
			    	GRP_BLCE := 0;
			    	CELL_REM := 0;
			    	CO_BLCE := 0;
			    	CO_PLDG := 0;
			end;
	
		--���뿩�Ż��ݾ�(GRP_BLCE) > ���ݱݾ�(I_AMT)
		IF NVL(GRP_BLCE,0) > I_AMT THEN
			BM03_AMT := I_AMT;
			BM04_AMT := 0;
		ELSE
			BM03_AMT := GRP_BLCE;
			BM04_AMT := I_AMT - GRP_BLCE;
	END IF;

		IF  I_CD = 'D' THEN  --�ӱ�
			BM04_CELL_CLMN := I_AMT;
			BM04_CELL_REM  := BM04_AMT;
			BM04_CELL_TOT  := 0;
		ELSE --�������
			BM04_CELL_CLMN := 0;
			BM04_CELL_REM  := BM04_AMT;
			BM04_CELL_TOT  := I_AMT;
		END IF;

--*********************************************************************
		begin
			SELECT GRP_PLDG_AMT INTO OVER_GRP_PLDG FROM TB_BM03M01 
			 WHERE CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
		       AND PRDT_GRP = I_PRDT_GRP;
		exception
		    when others then OVER_GRP_PLDG := 0;
		end;

		IF NVL(OVER_GRP_PLDG,0) < BM03_AMT THEN
		-- 03�ݾ��� �ѵ������� �����ϰ� �������� 04�ݾ����� ��ȯ ó�� �ʿ��մϴ�.
			BM04_AMT := BM04_AMT + BM03_AMT - NVL(OVER_GRP_PLDG,0);
			BM03_AMT := NVL(OVER_GRP_PLDG,0);
		END IF;
--*********************************************************************
		
		/* ����ó���� �纰 �����Ҵ� �ݾ׸�ŭ ������ �׷쿩�űݾ� ������  */ 
		SELECT COUNT(*) INTO BM04_CNT FROM TB_BM04M01 
		 WHERE CLNT_CD = I_CLNT_CD 
           AND CO_CD   = I_CO_CD
		   AND PRDT_GRP = I_PRDT_GRP;

		IF BM04_CNT > 0 THEN
			UPDATE TB_BM04M01
			   SET CO_BLCE_AMT    = NVL(CO_BLCE_AMT,0) + BM04_AMT			--�����ܾ�
				  ,CELL_REM_AMT   = NVL(CELL_REM_AMT,0) - BM04_CELL_REM		--�ܻ�����ܾ�
				  ,CELL_TOT_AMT   = NVL(CELL_TOT_AMT,0) - BM04_CELL_TOT		--���ݱݾ�
				  ,CELL_CLMN_AMT  = NVL(CELL_CLMN_AMT,0) + BM04_CELL_CLMN	--���ݱݾ�
				  ,GRP_BLCE_AMT   = NVL(GRP_BLCE_AMT,0) - BM03_AMT			--�׷쿩���Ҵ�ݾ�
				  ,LAST_CLMN_DTTM = I_TR_DT									--������������	
			 WHERE CLNT_CD  = I_CLNT_CD
			   AND CO_CD    = I_CO_CD
			   AND PRDT_GRP = I_PRDT_GRP;
		ELSE
			INSERT INTO TB_BM04M01
				(CLNT_CD, CO_CD, PRDT_GRP, 
				CO_PLDG_AMT, CO_BLCE_AMT, 
				CELL_TOT_AMT, CELL_CLMN_AMT, 
				PCHS_TOT_AMT, PCHS_CLMN_AMT, 
				CELL_REM_AMT, GRP_BLCE_AMT,
				LAST_CELL_DTTM,
				USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
			VALUES
			   (I_CLNT_CD, I_CO_CD, I_PRDT_GRP,
				0, BM04_AMT,
				(0-BM04_CELL_TOT), BM04_CELL_CLMN, 
				0, 0, 
				(0-BM04_CELL_REM), (0-BM03_AMT),
				I_TR_DT,
				'Y', 'DB_TRIGER', 'F_CREDIT_LOAN', SYSDATE);
		END IF;
   

		SELECT COUNT(*) INTO BM03_CNT FROM TB_BM03M01 
		 WHERE CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
		   AND PRDT_GRP = I_PRDT_GRP;

		IF BM03_CNT > 0 THEN
			UPDATE TB_BM03M01
			   SET GRP_BLCE_AMT   = NVL(GRP_BLCE_AMT,0) + BM03_AMT		--�����ܾ�
			 WHERE CLNT_CD = NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD)
		       AND PRDT_GRP = I_PRDT_GRP;
		ELSE

			INSERT INTO TB_BM03M01
				(CLNT_CD, PRDT_GRP, GRP_PLDG_AMT, GRP_BLCE_AMT, USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
			VALUES
				(NVL(C_LINK_GRP_CLNT_CD, I_CLNT_CD), I_PRDT_GRP, 0, BM03_AMT, 'Y', 'DB_TRIGER', 'F_CREDIT_LOAN', SYSDATE);
		END IF;
			
	
		exception when others then return -9;
		
	  end;	
		commit;	
			
		RETURN 0;	/*ó���Ϸ�==> I_CD = 'M' OR I_CD = 'D' THEN --M:�������, D:���� �ݾ� ó���� FLAG*/
				
	ELSE     --I_CD �����ڵ�
		RETURN 0;	/* ó������ ���� */
	END IF;  --I_CD END
	      
	return -9998;
	
exception
    when others then
        dbms_output.put_line('exception occurred! (' || sqlcode || ') : ' || sqlerrm);
        return -9999;

END;