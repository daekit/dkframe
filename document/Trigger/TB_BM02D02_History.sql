CREATE OR REPLACE TRIGGER TB_BM02D02_HISTORY
AFTER INSERT OR UPDATE OR DELETE 	-- 행이 추가되거나 값이 변경되었을 때
ON TB_BM02D02 						-- 담보마스터(TB_BM02D02)
FOR EACH ROW 						-- 모든 변경건에 대해 트리거 실행
-- 담보마스터(TB_BM02D02)  변동시
--담보마스터이력(TB_BM02H02) 생성
DECLARE 
    -- 사용할 변수선언
    to_SEQ BINARY_INTEGER :=0;
    
    C_GRP_CNT 	    number := 0;	/*그룹여신 존재여부 체크*/
    C_GRP_AMT 	    number := 0;	/*그룹여신 잔여 금액*/
    C_CO_AMT 	    number := 0;	/*사별여신 잔여 금액*/
    O_CO_AMT 	    number := 0;	/*사별여신 담보 금액*/
    C_CLNT_CD 	    number := 0;
    C_LINK_GRP_YN 	VARCHAR(20) := '';
    C_LINK_GRP_CLNT_CD 	number  := 0;
    C_CO_CD	 		VARCHAR(20) := '';
    C_PRDT_GRP      VARCHAR(20) := '';
    

BEGIN
	SELECT TB_BM02H02_SQ01.NEXTVAL into to_SEQ FROM DUAL  ;
	
	IF inserting THEN
	    C_PRDT_GRP := :NEW.PRDT_GRP;
		C_CLNT_CD := :NEW.CLNT_CD;
		C_CO_CD   := :NEW.CO_CD;
		
		INSERT INTO TB_BM02H02
		(	 TRST_NO
			,PROC_DTTM
			,CHG_CD
			,PLDG_SN
			,CLNT_CD
			,CO_CD
			,SELPCH_CD
			,PLDG_DIV_CD
			,PLDG_AMT
			,PLDG_RCOGN_RATE
			,EXPRTN_DT
			,SETUP_DT
			,PLDG_RMK
			,GRNTY_INSTT_CD
			,USE_YN
			,CREAT_ID
			,CREAT_PGM
			,CREAT_DTTM
			,UDT_ID
			,UDT_PGM
			,UDT_DTTM
			,PLDG_CHG_CD
			,PRDT_GRP
		)
 		VALUES (
 			to_SEQ
			, SYSDATE							--변동일자
			,'CHG01'							--변동사유
			,:NEW.PLDG_SN
			,:NEW.CLNT_CD
			,:NEW.CO_CD
			,:NEW.SELPCH_CD
			,:NEW.PLDG_DIV_CD
			,:NEW.PLDG_AMT
			,:NEW.PLDG_RCOGN_RATE
			,:NEW.EXPRTN_DT
			,:NEW.SETUP_DT
			,:NEW.PLDG_RMK
			,:NEW.GRNTY_INSTT_CD
			,:NEW.USE_YN
			,:NEW.CREAT_ID
			,:NEW.CREAT_PGM
			,:NEW.CREAT_DTTM
			,:NEW.UDT_ID
			,:NEW.UDT_PGM
			,:NEW.UDT_DTTM
			,:NEW.PLDG_CHG_CD	
			,:NEW.PRDT_GRP
								
	 );

	ELSIF updating THEN
	    C_PRDT_GRP := :NEW.PRDT_GRP;
		C_CLNT_CD := :NEW.CLNT_CD;
		C_CO_CD   := :NEW.CO_CD;
		
		INSERT INTO TB_BM02H02
		(
			TRST_NO
			,PROC_DTTM
			,CHG_CD
			,PLDG_SN
			,CLNT_CD
			,CO_CD
			,SELPCH_CD
			,PLDG_DIV_CD
			,PLDG_AMT
			,PLDG_RCOGN_RATE
			,EXPRTN_DT
			,SETUP_DT
			,PLDG_RMK
			,GRNTY_INSTT_CD
			,USE_YN
			,CREAT_ID
			,CREAT_PGM
			,CREAT_DTTM
			,UDT_ID
			,UDT_PGM
			,UDT_DTTM
			,PRE_PLDG_AMT
			,PRE_PLDG_RCOGN_RATE
			,PLDG_CHG_CD
			,PRDT_GRP
		)
 		VALUES (
 			to_SEQ
			, SYSDATE							--변동일자
			, 'CHG02'							--변동사유
			,:NEW.PLDG_SN
			,:NEW.CLNT_CD
			,:NEW.CO_CD
			,:NEW.SELPCH_CD
			,:NEW.PLDG_DIV_CD
			,:NEW.PLDG_AMT
			,:NEW.PLDG_RCOGN_RATE
			,:NEW.EXPRTN_DT
			,:NEW.SETUP_DT
			,:NEW.PLDG_RMK
			,:NEW.GRNTY_INSTT_CD
			,:NEW.USE_YN
			,:NEW.CREAT_ID
			,:NEW.CREAT_PGM
			,:NEW.CREAT_DTTM
			,:NEW.UDT_ID
			,:NEW.UDT_PGM
			,:NEW.UDT_DTTM
			,:OLD.PLDG_AMT
			,:OLD.PLDG_RCOGN_RATE
			,:NEW.PLDG_CHG_CD							
			,:NEW.PRDT_GRP
	 );
               
	ELSIF deleting THEN
	    C_PRDT_GRP := :OLD.PRDT_GRP;
		C_CLNT_CD := :OLD.CLNT_CD;
		C_CO_CD   := :OLD.CO_CD;
	
		INSERT INTO TB_BM02H02
		(
			TRST_NO
			,PROC_DTTM
			,CHG_CD
			,PLDG_SN
			,CLNT_CD
			,CO_CD
			,SELPCH_CD
			,PLDG_DIV_CD
			,PLDG_AMT
			,PLDG_RCOGN_RATE
			,EXPRTN_DT
			,SETUP_DT
			,PLDG_RMK
			,GRNTY_INSTT_CD
			,USE_YN
			,CREAT_ID
			,CREAT_PGM
			,CREAT_DTTM
			,UDT_ID
			,UDT_PGM
			,UDT_DTTM
			,PLDG_CHG_CD
			,PRDT_GRP
		)
 		VALUES (
			to_SEQ
			, SYSDATE				--변동일자
			, 'CHG03'				--변동사유
			,:OLD.PLDG_SN
			,:OLD.CLNT_CD
			,:OLD.CO_CD
			,:OLD.SELPCH_CD
			,:OLD.PLDG_DIV_CD
			,:OLD.PLDG_AMT
			,:OLD.PLDG_RCOGN_RATE
			,:OLD.EXPRTN_DT
			,:OLD.SETUP_DT
			,:OLD.PLDG_RMK
			,:OLD.GRNTY_INSTT_CD
			,:OLD.USE_YN
			,:OLD.CREAT_ID
			,:OLD.CREAT_PGM
			,:OLD.CREAT_DTTM
			,:OLD.UDT_ID
			,:OLD.UDT_PGM
			,:OLD.UDT_DTTM
			,:OLD.PLDG_CHG_CD						
			,:OLD.PRDT_GRP
	 );
	END IF;
	
	
	/*----------------------------------------------------------------------------*/
	/*  담보내역 CRUD 발생시 그룹공통 여신금액 산정 Start                                                  */
	/*----------------------------------------------------------------------------*/
	/* 기본 신용은 마스터금액과 Detail 금액  2곳에 있음
	       	마스터 그룹여신 금액 : TB_BM02M01.BASIS_CDTLN_AMT
	        Detail 금액 : TB_BM02D02.PLDG_AMT * TB_BM02D02.PLDG_RCOGN_RATE / 100
	       거래처 여신금액 관리 테이블 사별여신금액 (담보금액 * 답보인정비율 ) */

		SELECT 
			   SUM(nvl(A.BASIS_CDTLN_AMT,0)) AS C_GRP_AMT,                --그룹공통여신-사용안함
			   SUM(nvl(b.PLDG_AMT*b.PLDG_RCOGN_RATE/100,0)) AS C_CO_AMT,  --그룹공통여신금액
				   MAX(a.LINK_GRP_YN )AS C_LINK_GRP_YN, 
				   MAX(a.LINK_GRP_CLNT_CD )AS C_LINK_GRP_CLNT_CD 
	          into 
	               C_GRP_AMT, 
	               C_CO_AMT, 
	               C_LINK_GRP_YN, 
	               C_LINK_GRP_CLNT_CD 	 
		FROM TB_BM02M01 a LEFT OUTER JOIN TB_BM02D02 b 
							 ON a.CLNT_CD = b.CLNT_CD
							AND b.PLDG_DIV_CD = 'PLDG03'
							AND b.USE_YN = 'Y' 
							AND b.SELPCH_CD = 'SELPCH2'
			  				AND nvl(B.SETUP_DT,00010101) <= TO_CHAR(sysdate, 'YYYYMMDD') 
							AND nvl(B.EXPRTN_DT,99999999) >= TO_CHAR(sysdate, 'YYYYMMDD') 
							AND b.PRDT_GRP = C_PRDT_GRP
		WHERE a.USE_YN  = 'Y'
		  AND a.CLNT_CD = C_CLNT_CD;
	
	
		IF C_LINK_GRP_YN = 'Y' THEN  -- 연계 여신관리 대상
	
	       C_GRP_AMT := 0;  --헤더의 공통 금액 사용안함
	       C_CO_AMT  := 0;
			
			/* 연계 거래처 금액 산정 --> 기본 신용은 마스터금액과 Detail 금액  2곳에 있음
			       	마스터 그룹여신 금액 : TB_BM02M01.BASIS_CDTLN_AMT
			        Detail 금액 : TB_BM02D02.PLDG_AMT * TB_BM02D02.PLDG_RCOGN_RATE / 100
					*/
			SELECT 
			       SUM(nvl(A.BASIS_CDTLN_AMT,0)) AS C_GRP_AMT,              --그룹공통여신-사용안함
			       SUM(nvl(b.PLDG_AMT*b.PLDG_RCOGN_RATE/100,0)) AS C_CO_AMT --그룹공통여신금액
	               into C_GRP_AMT, C_CO_AMT
			  FROM TB_BM02M01 a LEFT OUTER JOIN TB_BM02D02 b 
									 ON a.CLNT_CD = b.CLNT_CD
									AND b.PLDG_DIV_CD = 'PLDG03'
									AND b.USE_YN = 'Y' 
									AND b.SELPCH_CD = 'SELPCH2'
									AND nvl(B.SETUP_DT,00010101) <= TO_CHAR(sysdate, 'YYYYMMDD') 
									AND nvl(B.EXPRTN_DT,99999999) >= TO_CHAR(sysdate, 'YYYYMMDD') 
									AND b.PRDT_GRP = C_PRDT_GRP
			  WHERE a.LINK_GRP_CLNT_CD = C_LINK_GRP_CLNT_CD
			    AND a.LINK_GRP_YN      = 'Y' 
				AND a.USE_YN           = 'Y';
				
		END IF;  --C_LINK_GRP_YN
	/*----------------------------------------------------------------------------*/
	/*  담보내역 CRUD 발생시 그룹공통 여신금액 산정 END                                             */
	/*----------------------------------------------------------------------------*/	
	/* TB_BM03M01 자료 생성은  TB_BM02M01 트리거  TR_BM02M01_BASIS_CDTLN_AMT에서 자동 생성됨                       */
	/*  처음 설계시    TB_BM02M01에서 그룹공통여신금액 관리하였으나 TB_BM02D02에서 공통여신금액 관리하는것으로 로 변경됨            */
	/*----------------------------------------------------------------------------*/	

		SELECT COUNT(*) INTO C_GRP_CNT 
		  FROM TB_BM03M01 
		 WHERE CLNT_CD  = NVL(C_LINK_GRP_CLNT_CD, C_CLNT_CD)
		   AND PRDT_GRP = C_PRDT_GRP;
	
		IF C_GRP_CNT > 0 THEN
			UPDATE TB_BM03M01
			   SET GRP_PLDG_AMT    = C_CO_AMT,	--그룹담보금액
			       GRP_BLCE_AMT    = C_CO_AMT	--그룹여신사용금액
			 WHERE CLNT_CD  = NVL(C_LINK_GRP_CLNT_CD, C_CLNT_CD)
		       AND PRDT_GRP = C_PRDT_GRP;

		--거래처 여신금액 관리 테이블 사별여신금액 (담보금액 * 답보인정비율 )
	 		SELECT nvl(sum(PLDG_AMT*PLDG_RCOGN_RATE/100),0)
             into O_CO_AMT
			FROM TB_BM02D02
			WHERE CLNT_CD  = C_CLNT_CD
		      AND CO_CD    = C_CO_CD
			  AND PLDG_DIV_CD NOT IN ('PLDG03')
			  AND SELPCH_CD = 'SELPCH2'
			  AND nvl(SETUP_DT,00010101) <= TO_CHAR(sysdate, 'YYYYMMDD') 
			  AND nvl(EXPRTN_DT,99999999) >= TO_CHAR(sysdate, 'YYYYMMDD') 
			  AND PRDT_GRP = C_PRDT_GRP
			  AND USE_YN    = 'Y';

			UPDATE TB_BM04M01
			   SET CO_PLDG_AMT    = O_CO_AMT,							--사별담보금액
			       CO_BLCE_AMT    = O_CO_AMT - NVL(CELL_REM_AMT,0)		--사별담보잔액
			 WHERE CLNT_CD  = C_CLNT_CD
			   AND CO_CD    = C_CO_CD
			   AND PRDT_GRP = C_PRDT_GRP;

		ELSE
			INSERT INTO TB_BM03M01
				(CLNT_CD,  PRDT_GRP, GRP_PLDG_AMT, GRP_BLCE_AMT, USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
			VALUES
				(NVL(C_LINK_GRP_CLNT_CD, C_CLNT_CD), C_PRDT_GRP, C_CO_AMT, C_CO_AMT, 'Y', 'DB_TRIGER', 'TB_BM02D02_HISTORY', SYSDATE);
		END IF;
	
END;