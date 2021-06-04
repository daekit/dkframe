CREATE OR REPLACE TRIGGER TR_BM02M01_BASIS_CDTLN_AMT
AFTER INSERT OR UPDATE OR DELETE 	-- 행이 추가되거나 값이 변경되었을 때
ON TB_BM02M01 						-- 거래처(TB_BM02M01)
FOR EACH ROW 						-- 모든 변경건에 대해 트리거 실행

/******************************************************************************
* TYPE			: TRIGGER (Tibero)
* NAME			: TR_BM02M01_BASIS_CDTLN_AMT
* DEVELOPER		: Jangsub.Nam
* DESCRIPTION	: 거래처마스터 기본여신금액 변동시
* 	            1.거래처별여신금액(TB_BM03M01) 테이블에 그룹여신금액 변경처리
*				2.공통코드(TB_CM05M01)에서 code_KIND = 'CO'의 법인별 담보여신금액 초기자료 생성 
*
******************************************************************************/
DECLARE


BEGIN
	IF inserting THEN
		--그룹여신금액 생성		
		INSERT INTO TB_BM03M01
			(CLNT_CD, GRP_PLDG_AMT, GRP_BLCE_AMT, USE_YN, CREAT_ID, CREAT_PGM, CREAT_DTTM)
		VALUES
			(:NEW.CLNT_CD, :NEW.BASIS_CDTLN_AMT, 0, 'Y', :NEW.CREAT_ID, :NEW.CREAT_PGM, :NEW.CREAT_DTTM);

		
		--사별여신  초기 생성		
		INSERT INTO TB_BM04M01
			(CLNT_CD, CO_CD, 
			CO_PLDG_AMT, CO_BLCE_AMT, CELL_TOT_AMT, CELL_CLMN_AMT, PCHS_TOT_AMT, PCHS_CLMN_AMT, 
			CELL_REM_AMT, GRP_BLCE_AMT, USE_YN, 
			CREAT_ID, CREAT_PGM, CREAT_DTTM)
		SELECT :NEW.CLNT_CD, CODE_ID, 
			0, 0, 0, 0, 0, 0, 
			0, 0, 'Y', 
			:NEW.CREAT_ID, :NEW.CREAT_PGM, :NEW.CREAT_DTTM
		FROM TB_CM05M01 WHERE code_KIND = 'CO' ;
		
			

	ELSIF updating THEN
		--그룹여신금액 조정		

		UPDATE TB_BM03M01 
		SET GRP_PLDG_AMT = :NEW.BASIS_CDTLN_AMT ,  
			USE_YN = 'Y', 
			UDT_ID = :NEW.UDT_ID , 
			UDT_PGM = :NEW.UDT_PGM , 
			UDT_DTTM = :NEW.UDT_DTTM 
		WHERE CLNT_CD = :NEW.CLNT_CD ;

               
	ELSIF deleting THEN
		--그룹여신금액 삭제		
		DELETE TB_BM03M01
		WHERE CLNT_CD = :OLD.CLNT_CD;
		
		--사별여신  초기 생성	
		DELETE TB_BM04M01
		WHERE CLNT_CD = :OLD.CLNT_CD;
		
	END IF;

END;	
