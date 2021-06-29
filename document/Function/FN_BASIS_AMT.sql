CREATE OR REPLACE FUNCTION FN_BASIS_AMT (
I_CO_CD      IN  VARCHAR,   -- 회사
I_CLNT_CD    IN  VARCHAR,  -- 거래처
I_BASIS_DT   IN  VARCHAR,   -- 기준일자
I_SELPCH     IN  VARCHAR,   -- 매입(SELPCH1)/매출(SELPCH2)구분
I_BILG_YN    IN  VARCHAR,   -- 계산서 발행 여부구분
I_TAXIVC_COPRT    IN  VARCHAR    -- 계산서 발행 법인
)
/******************************************************************************
* TYPE			: FUNCTION (Tibero)
* NAME			: FN_BASIS_AMT
* DEVELOPER		: abc
* DESCRIPTION	: 입력받은 일자 거래처를 이용 기준일 전일까지의 채권금액을 계산한다.
*	SELECT FN_BASIS_AMT('20210102',1 ) FROM DUAL;
*
*
* 작업순서
* 1. 시작일이 포함된 월의 기초금액을 가져오다.
* 2. 시작월 1일 ~ 시작일-1일까지의 매출/매입/수금자료를 가져와서 계산한다.
* 3. 당월 기초 +매출 +지급  -매입 -수금 : 잔액
******************************************************************************/
RETURN NUMBER 
IS
    O_CREDIT_AMT      NUMBER(15,2) := 0;
   
    I_OWNER_CD        VARCHAR(20)  := 'OWNER1' ; -- 자사단가만 조회한다.
    P_CLOSE_YM        VARCHAR(6) := '';  -- 기준월
    P_BASIS_SELL_AMT  NUMBER(15,2) := 0;  -- 외상매입 기초
    P_BASIS_PCHS_AMT  NUMBER(15,2) := 0;  -- 외상매출 기초
    P_PCHS_AMT_AR02   NUMBER(15,2) := 0;  -- 외상매입
    P_SELL_AMT_AR02   NUMBER(15,2) := 0;  -- 외상매출
    P_PCHS_AMT_AR04   NUMBER(15,2) := 0;  -- 외상매입
    P_SELL_AMT_AR04   NUMBER(15,2) := 0;  -- 외상매출
    P_PAY_AMT         NUMBER(15,2) := 0;  -- 지급 + 상계
    P_COLLECT_AMT     NUMBER(15,2) := 0;  -- 수금 + 상계
    
BEGIN
   --  시작일자가 포함된 월의 기초금액을 가져온다.
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
   -- 마감한 이력이 없으면 기준일 이전의 최초 매출발생일을 기준으로 전월 계산.  
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
   
   -- 매출/매입자료를 계산한다.
   -- 1. 매출/매입확정이 안되것은 매출테이블에서 가져온다. 
   --    매출확정 여부가 Y인경우에는 제외한다. 매출확정(계산사 발행)이 안되었음
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
    -- 2. 확정이 된 건은 확정테이블에서 가져온다.
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
    
    
   -- 수금자료를 계산한다.
   -- ETRDPS01	수금
   -- ETRDPS02	지급
   -- ETRDPS03	상계
   -- ETRDPS04	상계(지급보증), 상계(지급보증)-매입/보험  , 매입과 보험으로 상계함으로 매입채권만 차감됨.
   -- ETRDPS05	상계(지급보증 보전),상계(지급보증 보전)-매출/보험, 매출과 보험으로 상계함으로 매출채권만 차감 됨.
   IF(I_SELPCH = 'SELPCH1')
   THEN
		BEGIN
		    SELECT  -- SUM(DECODE(A.ETRDPS_TYP, 'ETRDPS02', A.ETRDPS_AMT, 'ETRDPS03', A.ETRDPS_AMT, 'ETRDPS04', A.ETRDPS_AMT)) 
				  -- , SUM(DECODE(A.ETRDPS_TYP, 'ETRDPS01', A.ETRDPS_AMT, 'ETRDPS03', A.ETRDPS_AMT, 'ETRDPS05', A.ETRDPS_AMT))
				   SUM(DECODE(NVL(CM05.CODE_RPRC,I_SELPCH),'SELPCH1', 0,NVL(A.ETRDPS_AMT,0))) -- 수급,     상계 지급보증 보전 
  				 , SUM(DECODE(NVL(CM05.CODE_RPRC,I_SELPCH),'SELPCH1', NVL(A.ETRDPS_AMT,0),0))   -- 지급, 상계, 상계 지급보증 
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
				   SUM(DECODE(NVL(CM05.CODE_RPRC,I_SELPCH),'SELPCH2', NVL(A.ETRDPS_AMT,0),0))  -- 수급,     상계 지급보증 보전 
  				 , SUM(DECODE(NVL(CM05.CODE_RPRC,I_SELPCH),'SELPCH2', 0,NVL(A.ETRDPS_AMT,0)))  -- 지급, 상계, 상계 지급보증 
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
 --당월 기초 +매출 +지급  -매입 -수금 : 잔액  
   -- 매입 채권 조회시 : 당월기초 - 매입 +지급
   -- 매출 채권 조회 시 : 당월기초 + 매출 - 수금
   -- 당월 기초 +매출 +지급  -매입 -수금 : 잔액  
--- 차변 : 매출, 지급
--- 대변 : 매입, 수금

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