CREATE OR REPLACE FUNCTION FN_SD06_UPR_FROM_PRDT_CD (
I_PRDT_DT      IN  VARCHAR,  -- 기준일자
I_SELPCH_CD    IN  VARCHAR,  -- 매입/매출구분
I_PRDT_IMP_YN  IN  VARCHAR,  -- 일반/수입구분
I_PRDT_CD      IN  VARCHAR,  -- 자재코드
I_CLNT_CD      IN  VARCHAR   -- 거래처
)
/******************************************************************************
* TYPE			: FUNCTION (Tibero)
* NAME			: FN_SD06_UPR_FROM_PRDT_CD
* DEVELOPER		: abc
* DESCRIPTION	: 입력받은 자재코드 정보로 단가를 계산한다.
*	SELECT FN_SD06_UPR_FROM_PRDT_CD('20210102',.....) FROM DUAL; -- '매출' CODE_NM 추출
*
******************************************************************************/
RETURN NUMBER 
IS
    O_PRDT_UPR        NUMBER(15,2) := 0;
    P_PRDT_UPR        NUMBER(15,2) := 0;  /* 매입단가 */
    P_SELL_UPR        NUMBER(15,2) := 0;  /* 매출단가 */
    T_PRDT_UPR        NUMBER(15,2) := 0;  /* 매입단가 + 힐증 */
    T_SELL_UPR        NUMBER(15,2) := 0;  /* 매출단가 + 할중 */
    P_BASIS_PRICE_SEQ NUMBER(15,2) := 0;
    P_SC_AMT_STKN     NUMBER(15,2) := 0;
    P_SC_AMT_SIZE     NUMBER(15,2) := 0 ;
	I_PRDT_DIV        VARCHAR(20) := NULL;   /* 제품구분  */
	I_PRDT_STKN       VARCHAR(20) := NULL;   -- 강종
	I_PRDT_ERQK_CD    VARCHAR(20) := NULL;   -- 내진설계 여부
	I_PRDT_SIZE       VARCHAR(30) := NULL;   -- SIZE
	I_PCHS_UPR        NUMBER(15,2) := 0 ;   -- 최종 매입단가
	I_SELL_UPR        NUMBER(15,2) := 0 ;   -- 최종 매출단가
 
BEGIN
   BEGIN
        SELECT PRDT_DIV,
               PRDT_STKN,
               PRDT_ERQK_YN,
               PRDT_SIZE,
               PCHS_UPR,
               SELL_UPR
        INTO   I_PRDT_DIV,
	           I_PRDT_STKN,
	           I_PRDT_ERQK_CD,
	           I_PRDT_SIZE,
	           I_PCHS_UPR,
	           I_SELL_UPR
        FROM   TB_BM01M01
        WHERE  PRDT_CD  = I_PRDT_CD;
   
   END;
   /* 커플러일 경우 거래처별 단가를 조회한다. */
   IF (I_PRDT_DIV = 'PRDTDIV22' AND I_CLNT_CD IS NOT NULL) THEN 
      BEGIN
         SELECT PRDT_UPR INTO P_PRDT_UPR
         FROM   TB_SD08M01
         WHERE  CLNT_CD = I_CLNT_CD
           AND  PRDT_CD = I_PRDT_CD
           AND  PRDT_DT = (SELECT MAX(PRDT_DT) FROM TB_SD08M01
                           WHERE  CLNT_CD    = I_CLNT_CD
                             AND  PRDT_DT    <= I_PRDT_DT 
                             AND  PRDT_CD    = I_PRDT_CD
		                     AND  SELPCH_CD  = I_SELPCH_CD  )
		   AND  SELPCH_CD  = I_SELPCH_CD ;
          EXCEPTION WHEN OTHERS THEN P_PRDT_UPR := 0;
           
      END;
      
   ELSE 
/* 매입,매출구분이 없어지고, 매입,매출항목을 추가함 추후 관리 방법이 다를경우 재활용 할 것  I_SELPCH_CD = 1(매입) */
	    BEGIN
		    SELECT PRDT_UPR,SELL_UPR,BASIS_PRICE_SEQ INTO P_PRDT_UPR,P_SELL_UPR,P_BASIS_PRICE_SEQ
		    FROM   TB_SD06M01 
		    WHERE  PRDT_DT = (SELECT MAX(PRDT_DT) FROM TB_SD06M01 
		                          WHERE  PRDT_DT      < I_PRDT_DT
		                          AND    SELPCH_CD    = 'SELPCH2' -- I_SELPCH_CD
		                          AND    PRDT_IMP_YN  = I_PRDT_IMP_YN 
		                          AND    PRDT_DIV     = I_PRDT_DIV )
		    AND    SELPCH_CD    = 'SELPCH2' -- I_SELPCH_CD
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
	END IF;       
    
    SELECT NVL(P_PRDT_UPR,0) + NVL(P_SC_AMT_STKN,0) + NVL(P_SC_AMT_SIZE,0),
           NVL(P_SELL_UPR,0) + NVL(P_SC_AMT_STKN,0) + NVL(P_SC_AMT_SIZE,0)
    INTO   T_PRDT_UPR, T_SELL_UPR
    FROM   DUAL;
    
    IF (I_SELPCH_CD = 'SELPCH1') THEN O_PRDT_UPR := T_PRDT_UPR;  -- 매입단가
                                 ELSE O_PRDT_UPR := T_SELL_UPR;  -- 매출단가
    END IF;
    
    
  /* 가격표에 단가가 없는 경우 자재 Master에 있는 최근 매입/매출단가를 활용한다.커플러 제외 */
   IF(O_PRDT_UPR = 0  AND I_PRDT_DIV != 'PRDTDIV22') THEN
       IF (I_SELPCH_CD = 'SELPCH1') THEN O_PRDT_UPR := I_PCHS_UPR;  -- 매입단가
                                    ELSE O_PRDT_UPR := I_SELL_UPR;  -- 매출단가
       END IF;
    END IF;   
  

    RETURN O_PRDT_UPR;
END;