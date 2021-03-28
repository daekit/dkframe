CREATE OR REPLACE PROCEDURE PL_SD04_MES
/*
* 프로시저명 : MES 주문연계 이관
* 프로시저 ID: PL_SD04_MES
* 내용 :  MES의 주문과 ERP의 주문을 연결한다.  기존의 프로젝트(현장)에 대해서 하나의 주문이라도 연결되어 있으면 나머지는 자동으로 연결된다
                단 자재는 연결되어 있어야 한다.
* 작성자 : ABC
* 작성년월일 : 2021.03.26
* 최종수정자 :
* 최종수정일 :
* 특이 사항   :  
*/

IS
   CURSOR C1 IS
     SELECT * FROM TB_CM05M01 A
     WHERE CODE_KIND  = 'MESFTR';
BEGIN
    FOR CUR1 IN C1 LOOP    
/* 1. MES의 오더 MST를 읽어온다
 * 2. MES의 오더MST의 현장이 ERP의 프로젝트와 연결이 되는지 확인한다.
 * 3. ERP의 동일한 프로젝트를 가지는 주문 아무꺼나 가져온다.-- 동일 프로젝트는 동일 현장이고 동일 거래처 이다.
 * 4. MES의 오더가 기존 연결 테이블에 없어야 한다.
 */
     BEGIN
		INSERT INTO TB_SD04M01_MES
		(CO_CD
		,REQ_DT
		,CLNT_CD
		,ODR_SEQ
		,MES_FTR_CD
		,MES_ORD_NO
		,CREAT_ID
		,CREAT_PGM
		,CREAT_DTTM 
		)
		SELECT SD05.CO_CD
		      ,A.ORD_DD
		      ,SD04.CLNT_CD
		      ,SD04.ODR_SEQ 
		      ,A.WORKS_CD
		      ,A.ORD_NO
		      ,'I/F'
		      ,'IF MES'
		      ,SYSDATE 
		FROM KUMMUN.TB_MES_ORD_MST A
		INNER JOIN TB_SD05M01_MES SD05
		        ON SD05.MES_FTR_CD   = A.WORKS_CD 
		       AND SD05.PRJCT_MES_CD = A.CUST_CD
		INNER JOIN TB_SD04M01 SD04
		        ON SD04.CO_CD    = SD05.CO_CD
		       AND SD04.PRJCT_CD = SD05.PRJCT_CD
		       AND ROWNUM = 1
		WHERE A.ORD_NO NOT IN (SELECT MES_ORD_NO 
		                         FROM TB_SD04M01_MES O 
		                        WHERE O.CO_CD      = SD05.CO_CD 
		                          AND O.CLNT_CD    = SD04.CLNT_CD
		                          AND O.MES_FTR_CD = A.WORKS_CD
		                          AND O.MES_ORD_NO = A.ORD_NO) 
	         AND A.WORKS_CD = CUR1.CODE_ETC;
	         
	       EXCEPTION WHEN OTHERS THEN NULL;
       END;
     END LOOP;
      
      COMMIT;
      
      FOR CUR2 IN C1 LOOP
/* 1. MES의 오더상세를 읽어온다
 * 2. MES의 오더상세의 자재가 ERP의 자재와 연결이 되는지 확인한다.
 * 3. MES의 오더가 ERP의 오더와 연결되어 있는지 확인한다.
 * 4. MES의 상세자재가 ERP의 상세 오더로 동일한 자재가 있는지 확인한다.
 * 5. 기존 MES오더 상세 연결에 없어야 한다.
 */
        BEGIN
			INSERT INTO TB_SD04D01_MES
				(ODR_SEQ
				,ODR_DTL_SEQ
				,MES_FTR_CD
				,MES_ORD_NO
				,MES_ORD_LINE_NO
				,CREAT_ID
				,CREAT_PGM
				,CREAT_DTTM)	
			SELECT  SD04D.ODR_SEQ
			      , SD04D.ODR_DTL_SEQ
			      , D.WORKS_CD
			      , D.ORD_NO
			      , D.ORD_LINE_NO
			      ,'I/F'
			      ,'IF MES'
			      ,SYSDATE
			FROM       KUMMUN.TB_MES_ORD_DTL D 
			INNER JOIN KUMMUN.TB_MES_ORD_MST MO  
			        ON MO.ORD_NO = D.ORD_NO 
			INNER JOIN TB_BM01M01_MES BM01
			        ON BM01.MES_FTR_CD = D.WORKS_CD AND  BM01.MES_DIMS_CD = D.DIMS_CD
			INNER JOIN TB_SD04M01_MES SD04
			        ON SD04.MES_ORD_NO = D.ORD_NO 
			       AND SD04.MES_FTR_CD = D.WORKS_CD 
			INNER JOIN  TB_SD04D01 SD04D
			        ON SD04D.ODR_SEQ  = SD04.ODR_SEQ
			       AND SD04D.PRDT_CD  = BM01.PRDT_CD
			       AND ROWNUM = 1
			WHERE D.ORD_LINE_NO NOT IN (SELECT MES_ORD_LINE_NO
			                              FROM TB_SD04D01_MES O 
			                             WHERE O.ODR_SEQ     = SD04D.ODR_SEQ 
			                               AND O.ODR_DTL_SEQ = SD04D.ODR_DTL_SEQ
			                               AND O.MES_FTR_CD  = D.WORKS_CD
			                               AND O.MES_ORD_NO  = D.ORD_NO)                             
	          AND D.WORKS_CD = CUR2.CODE_ETC;
           EXCEPTION WHEN OTHERS THEN NULL; 
       END;                   
     END LOOP;

     COMMIT;
END;
