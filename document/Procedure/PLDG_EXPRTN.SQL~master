CREATE OR REPLACE PROCEDURE PLDG_EXPRTN 
/*
* 프로시저명 : PLDG_EXPRTN
* 프로시저 ID:
* 내용 :  담보의 만기일이 지날경우 담보한도에서 차감함
* 작성자 : ABC
* 작성년월일 : 2021.03.22
* 최종수정자 :
* 최종수정일 :
* 특이 사항   :  현재 여신금액이 (-)일 경우는 음수가 발생한다.
                        스케줄을 돌려서 매일 배치작업 한다.
*/
IS
   CURSOR C1 IS
     SELECT * FROM TB_BM02D02
     WHERE EXPRTN_DT = TO_CHAR(SYSDATE -1,'YYYYMMDD')
       AND USE_YN    = 'Y';
       
       L_PLDG_AMT  NUMBER(15,2) := 0;
BEGIN
    FOR  FC1 IN C1 LOOP    
        L_PLDG_AMT := FC1.PLDG_AMT * FC1.PLDG_RCOGN_RATE /100; /* 여신금액 = 금액 * 비율 /100 */

/*  여신금액 재산정은 TB_BM02D02변경시  TB_BM02D02_HISTORY 트리거에서 자동 산정처리됩니다.
        UPDATE TB_BM04M01
		   SET CO_PLDG_AMT = CO_PLDG_AMT - L_PLDG_AMT
		 WHERE CLNT_CD     = FC1.CLNT_CD
		   AND CO_CD       = FC1.CO_CD;
----------------------------------------------------------------*/		 
		 UPDATE  TB_BM02D02
		    SET  USE_YN  = 'N'  /* 만기가 되었음으로 사용안함 */
		  WHERE  PLDG_SN = FC1.PLDG_SN
            AND  CLNT_CD = FC1.CLNT_CD;
		   
     END LOOP;
     COMMIT;
END;