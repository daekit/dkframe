CREATE OR REPLACE PROCEDURE PL_AR02_MES (
    I_WOKKS_CD        IN VARCHAR2, 
    I_DT_FROM         IN VARCHAR2, 
    I_DT_TO           IN VARCHAR2, 
    O_ERR_CODE        OUT NUMBER,      -- 에러 코드
    O_ERR_MSG         OUT VARCHAR2      -- 에러 메시지
   )
/*
* 프로시저명 : MES 매출자료 이관
* 프로시저 ID: PL_AR02_MES
* 내용 :  MES의 판매내용을 ERP의 매출로 이관한다.
* 작성자 : ABC,PIY
* 작성년월일 : 2021.03.25
* 최종수정자 :
* 최종수정일 :
* 특이 사항   :  
*/

/* 할거
TB_MES_MTRL_RSLT  전송여부

*/

IS
   CURSOR C1 IS
     SELECT * FROM KUMMUN.TB_MES_MTRL_RSLT A
     WHERE BASE_DD   BETWEEN I_DT_FROM AND  I_DT_TO
       AND MTRL_CHG_CAUSE_CD = '8'
       AND ERP_TRANS_YN      = 'N';
     
     
       L_CO_CD         VARCHAR(20) := NULL;    /* 회사  */
       L_TAXIVC_COPRT  VARCHAR(20) := NULL;    /* 계산서 발행법인 */
       L_TRST_CLNT_CD  NUMBER      := 1;       /* 거래내역 거래처 코드*/
       L_TRST_PRDT_CD  NUMBER      := 0        /* 제품코드*/
       L_OWNER_CD      VARCHAR(20) := NULL;    /* 재고 주체 */
       L_IMP_YN           VARCHAR(20) := NULL;    /* 수입여부 */
       L_TYP_CD            VARCHAR(20) := NULL;    /* 판매유형*/
       L_TRSP_TYP_CD      VARCHAR(20) :='TRSPTYP1';    /* 입출고거래유형  */
       L_PRDT_LEN         VARCHAR(20) := NULL;       /* 제품길이*/



BEGIN
    FOR  FC1 IN C1 LOOP
    
    /* 공통코드 사용방안 필요함 */
      IF    (FC1.WORKS_CD IN ('J','N')) THEN
           L_CO_CD := 'GUM'; L_TAXIVC_COPRT := 'ESTCOPRT1';
      ELSIF (FC1.WORKS_CD = 'C') THEN      
           L_CO_CD := 'GGS'; L_TAXIVC_COPRT := 'ESTCOPRT5';
      END IF;
      /* 주문연게 */
      BEGIN
         SELECT * FROM TB_SD4M01_MES, TB_SD4D01_MES  WHERE 1=1
         L_TRST_PRDT_CD
         L_OWNER_CD,    
         L_IMP_YN, 
         L_TYP_CD
         L_PRDT_LEN    
      END;
      /* 자재연게 */
      BEGIN
         SELECT * FROM TB_BM01M01_MES WHERE 1=1
         L_TRST_PRDT_CD
      END;


	   INSERT INTO TB_AR02M01_MES
	   ( MES_CERTI_NO  , MES_FTR_CD	   , CF_YN             , CO_CD
	   , SELPCH_CD       , TAXIVC_COPRT   , TRST_DT	   , TRST_CLNT_CD
	   , TRST_PRDT_CD   , OWNER_CD	   , IMP_YN	   , TYP_CD
	   , TRSP_TYP_CD     , TRST_RPRC_SEQ , TRST_DTL_SEQ    , PRDT_LEN
	   , ODR_NO          , SALES_MNG	   , PRJCT_CD	   , MAKR_CD
	   , WH_CD            , MNG_TEL	   , DLVR_DTTM	   , DIRTRS_YN
	   , PRDT_DIV	   , PRDT_STKN	   , PRDT_SIZE	   , PRDT_UNIT
	   , PRDT_UPR	   , STOCK_UPR	   , PCHS_UPR	   , SELL_UPR
	   , TRST_QTY	   , TRST_WT          , TRST_UPR	   , TRST_AMT
	   , REAL_TRST_QTY  , REAL_TRST_WT   , REAL_TRST_UPR , REAL_TRST_AMT
	   , BILG_QTY	   , BILG_WT	    , BILG_UPR	   , BILG_AMT
	   , TRST_DC_AMT    , ETC_AMT          , TRSP_RMK	   , TRANS_AMT
	   , SHIP_VEH_NO     , CLNT_NM         , BILG_CERT_NO  , LOSS_RATE
	   , ODR_CREAT_DIV  , CREAT_ID         , CREAT_PGM     , CREAT_DTTM
	   , SALES_AREA_CD  , SITE_CD           , BILG_VAT_AMT  , PRDT_SPEC)
	    VALUES
	    (  GOLDMOON.TB_AR02M01_MES_SQ01.NEXTVAL ,FC1.WORKS_CD	      ,'N'	      ,L_CO_CD 
	      ,'SELPCH'	     , L_TAXIVC_COPRT, FC2.BASE_DD,  L_TRST_PRDT_CD
	      ,L_TRST_PRDT_CD, L_OWNER_CD,     L_IMP_YN,       L_TYP_CD
	      ,L_TRSP_TYP_CD     , '' , ''   , L_PRDT_LEN
	   , ODR_NO          , SALES_MNG	   , PRJCT_CD	   , MAKR_CD
	   , WH_CD            , MNG_TEL	   , DLVR_DTTM	   , DIRTRS_YN
	   , PRDT_DIV	   , PRDT_STKN	   , PRDT_SIZE	   , PRDT_UNIT
	   , PRDT_UPR	   , STOCK_UPR	   , PCHS_UPR	   , SELL_UPR
	   , TRST_QTY	   , TRST_WT          , TRST_UPR	   , TRST_AMT
	   , REAL_TRST_QTY  , REAL_TRST_WT   , REAL_TRST_UPR , REAL_TRST_AMT
	   , BILG_QTY	   , BILG_WT	    , BILG_UPR	   , BILG_AMT
	   , TRST_DC_AMT    , ETC_AMT          , TRSP_RMK	   , TRANS_AMT
	   , SHIP_VEH_NO     , CLNT_NM         , BILG_CERT_NO  , LOSS_RATE
	   , ODR_CREAT_DIV  , CREAT_ID         , CREAT_PGM     , CREAT_DTTM
	   , SALES_AREA_CD  , SITE_CD           , BILG_VAT_AMT  , PRDT_SPEC
	     )



    UPDATE KUMMUN.TB_MES_MTRL_RSLT SET ERP_TRANS_YN = 'Y'
    WHERE TRANSACTION_ID = fC1.TRANSACTION_ID;
    
    
    
     END LOOP;
     COMMIT;
END;

/*

WORKS_CD                : 진천 J,인천 N
FAC_CD
BASE_DD
MTRL_CHG_CAUSE_CD
SUP_CUST_CD
PRODUCT_NAME_CD
DIMS_CD
MTRL_WGT
WGHT_NO
VEHL_NO
MTRL_CHG_CAUSE
CREATED_OBJECT_ID
CREATED_PROGRAM_ID
CREATION_TIMESTAMP
LAST_UPDATED_OBJECT_ID
LAST_UPDATE_PROGRAM_ID
LAST_UPDATE_TIMESTAMP
CUST_CD
MTRL_LENGTH
MTRL_QUANTITY
CATEGORY_ID
UNLOAD_AREA
SPEC_CD
SUP_MTRL_WGT
IN_MTRL_WGT
LOAD_ORG_NO
ALLOC_VEHL_NO
LOSS_RATE
LOSS_MTRL_WGT
*/
