CREATE OR REPLACE TRIGGER TR_BM02D02_PLDG_AMT
AFTER INSERT OR UPDATE OR DELETE 	-- 행이 추가되거나 값이 변경되었을 때
ON TB_BM02D02 						-- 거래처(TB_BM02D02)
FOR EACH ROW 						-- 모든 변경건에 대해 트리거 실행

/******************************************************************************
* TYPE			: TRIGGER (Tibero)
* NAME			: TR_BM02D02_PLDG_AMT
* DEVELOPER		: Jangsub.Nam
* DESCRIPTION	: 거래처마스터 담보금액 변동시
* 	              거래처별여신금액(TB_BM04M01) 테이블에 사별여신금액(CO_PLDG_AMT)변경처리
******************************************************************************/
DECLARE 
    -- 사용할 변수선언
    CNT NUMBER :=0;
    TOT_AMT NUMBER :=0;
BEGIN

	SELECT COUNT(*)INTO CNT 
	FROM TB_BM04M01
	WHERE CLNT_CD = :NEW.CLNT_CD
	AND CO_CD = :NEW.CO_CD;
	
	IF inserting or updating THEN
		IF CNT > 0 THEN
		--사별여신금액 = 합계(담보금액 * 담보인증비율)
			UPDATE TB_BM04M01
			SET CO_PLDG_AMT = 
				(select nvl(sum(PLDG_AMT*PLDG_RCOGN_RATE/100),0) from TB_BM02D02
				 where CLNT_CD = :NEW.CLNT_CD
				 and  CO_CD = :NEW.CO_CD), 
				UDT_ID = :NEW.CREAT_ID, 
				UDT_PGM = :NEW.CREAT_PGM, 
				UDT_DTTM = :NEW.CREAT_DTTM
								
			WHERE CLNT_CD = :NEW.CLNT_CD
			AND CO_CD = :NEW.CO_CD;
		ELSE
			INSERT INTO TB_BM04M01
				(CLNT_CD, CO_CD, 
				CO_PLDG_AMT, CO_BLCE_AMT, CELL_TOT_AMT, CELL_CLMN_AMT, PCHS_TOT_AMT, PCHS_CLMN_AMT, 
				USE_YN, 
				CREAT_ID, CREAT_PGM, CREAT_DTTM)
			VALUES(:NEW.CLNT_CD, :NEW.CO_CD, 
				:NEW.PLDG_AMT * :NEW.PLDG_RCOGN_RATE / 100, 0, 0, 0, 0, 0, 
				'Y', 
				:NEW.CREAT_ID, :NEW.CREAT_PGM, :NEW.CREAT_DTTM);
				
		END IF;
					 
	ELSIF deleting THEN
		--사별여신금액 = 합계(담보금액 * 담보인증비율)
			UPDATE TB_BM04M01
			SET CO_PLDG_AMT = 
				(select nvl(sum(PLDG_AMT*PLDG_RCOGN_RATE/100),0) from TB_BM02D02
				 where CLNT_CD = :OLD.CLNT_CD
				 and  CO_CD = :OLD.CO_CD), 
				UDT_ID = :OLD.CREAT_ID, 
				UDT_PGM = :OLD.CREAT_PGM, 
				UDT_DTTM = :OLD.CREAT_DTTM								
			WHERE CLNT_CD = :OLD.CLNT_CD
			AND CO_CD = :OLD.CO_CD;
			
	END IF;
END;
