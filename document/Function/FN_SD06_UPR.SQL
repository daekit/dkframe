CREATE OR REPLACE FUNCTION FN_SD06_UPR (
I_PRDT_DT      IN  VARCHAR,   -- 기준일자
I_SELPCH_CD    IN  VARCHAR,   -- 매입/매출구분
I_PRDT_IMP_YN  IN  VARCHAR,   -- 일반/수입구분 
I_PRDT_DIV     IN  VARCHAR,   -- 제품구분
I_PRDT_STKN    IN  VARCHAR,   -- 강종
I_PRDT_ERQK_CD IN  VARCHAR,   -- 내진설계 여부
I_PRDT_SIZE    IN  VARCHAR    -- SIZE
)
/******************************************************************************
* TYPE			: FUNCTION (Tibero)
* NAME			: FN_SD06_UPR
* DEVELOPER		: abc
* DESCRIPTION	: 입력받은 정보로 단가를 계산한다.
*	SELECT FN_SD06_UPR('20210102',.....) FROM DUAL; -- '매출' CODE_NM 추출
*
******************************************************************************/
RETURN NUMBER 
IS
    O_PRDT_UPR        NUMBER(15,2) := 0;
    P_PRDT_UPR        NUMBER(15,2) := 0;
    P_BASIS_PRICE_SEQ NUMBER(15,2) := 0;
    P_SC_AMT_STKN     NUMBER(15,2) := 0;
    P_SC_AMT_SIZE     NUMBER(15,2) := 0 ;
    I_OWNER_CD        VARCHAR(20)  := 'OWNER1' ; -- 자사단가만 조회한다.
    
 
BEGIN
    BEGIN
	    SELECT PRDT_UPR,BASIS_PRICE_SEQ INTO P_PRDT_UPR,P_BASIS_PRICE_SEQ
	    FROM   TB_SD06M01 
	    WHERE  PRDT_DT = (SELECT MAX(PRDT_DT) FROM TB_SD06M01 
	                          WHERE  PRDT_DT     <= I_PRDT_DT
	                          AND    SELPCH_CD    = I_SELPCH_CD
	                          AND    PRDT_IMP_YN  = I_PRDT_IMP_YN 
	                          AND    PRDT_DIV     = I_PRDT_DIV )
	    AND    SELPCH_CD    = I_SELPCH_CD
	    AND    PRDT_IMP_YN  = I_PRDT_IMP_YN 
	    AND    PRDT_DIV     = I_PRDT_DIV;    
	    EXCEPTION WHEN OTHERS THEN P_PRDT_UPR := 0;P_BASIS_PRICE_SEQ := NULL;
    END;
    IF P_BASIS_PRICE_SEQ IS NOT NULL THEN
	    BEGIN
		    SELECT SC_AMT INTO P_SC_AMT_STKN FROM TB_SD06D01 
		    WHERE  BASIS_PRICE_SEQ = P_BASIS_PRICE_SEQ
		    AND    PRDT_STKN       = I_PRDT_STKN
		    AND    PRDT_ERQK_CD    = I_PRDT_ERQK_CD;
	    EXCEPTION WHEN OTHERS THEN P_SC_AMT_STKN := 0;
	    END;
	    BEGIN
		    SELECT SC_AMT INTO P_SC_AMT_SIZE FROM TB_SD06D02 
		    WHERE  BASIS_PRICE_SEQ = P_BASIS_PRICE_SEQ
		    AND    PRDT_SIZE       = I_PRDT_SIZE
		    AND    PRDT_ERQK_CD    = I_PRDT_ERQK_CD;   
	    EXCEPTION WHEN OTHERS THEN P_SC_AMT_SIZE := 0;
        END;
    END IF;
    
    SELECT NVL(P_PRDT_UPR,0) + NVL(P_SC_AMT_STKN,0) + NVL(P_SC_AMT_SIZE,0)
    INTO   O_PRDT_UPR
    FROM   DUAL;
       
    RETURN O_PRDT_UPR;
END;