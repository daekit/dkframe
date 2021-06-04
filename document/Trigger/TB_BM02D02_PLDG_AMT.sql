CREATE OR REPLACE TRIGGER TB_BM02D02_PLDG_AMT
AFTER INSERT OR UPDATE OR DELETE 	-- 행이 추가되거나 값이 변경되었을 때
ON TB_BM02D02 						-- 거래처(TB_BM02D02)
FOR EACH ROW 						-- 모든 변경건에 대해 트리거 실행

--거래처마스터 담보금액 변동시
--거래처별여신금액(TB_BM03M01) 테이블에 사별여신금액(CO_PLDG_AMT)변경처리
--공통코드에 있는 그룹회사 전체를 일과 수정함
BEGIN
	IF inserting THEN
		--사별여신금액 = 합계(담보금액 * 담보읹증비율)
		UPDATE TB_BM03M01
		SET CO_PLDG_AMT = 
			(select nvl(sum(PLDG_AMT*PLDG_RCOGN_RATE/100),0) from TB_BM02D02
			where CLNT_CD = :NEW.CLNT_CD
			and  CO_CD = :NEW.CO_CD)
		WHERE CLNT_CD = :NEW.CLNT_CD
		AND CO_CD = :NEW.CO_CD;

	ELSIF updating THEN
		--사별여신금액 = 합계(담보금액 * 담보읹증비율)
		UPDATE TB_BM03M01
		SET CO_PLDG_AMT = 
			(select nvl(sum(PLDG_AMT*PLDG_RCOGN_RATE/100),0) from TB_BM02D02
			where CLNT_CD = :NEW.CLNT_CD
			and  CO_CD = :NEW.CO_CD)
		WHERE CLNT_CD = :NEW.CLNT_CD
		AND CO_CD = :NEW.CO_CD;
               
	ELSIF deleting THEN
		--사별여신금액 = 합계(담보금액 * 담보읹증비율)
		UPDATE TB_BM03M01
		SET CO_PLDG_AMT = 
			(select nvl(sum(PLDG_AMT*PLDG_RCOGN_RATE/100),0) from TB_BM02D02
			where CLNT_CD = :OLD.CLNT_CD
			and  CO_CD = :OLD.CO_CD)
		WHERE CLNT_CD = :OLD.CLNT_CD
		AND CO_CD = :OLD.CO_CD;
	END IF;
END;
