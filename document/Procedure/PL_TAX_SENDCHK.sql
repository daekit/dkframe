CREATE OR REPLACE PROCEDURE PL_TAX_SENDCHK
/*
* 프로시저명 : 전자세금계산서 전송 오류 체크 처리
* 프로시저 ID: PL_TAX_SENDCHK
* 내용 :  전자세금계산서 전송처리시 문서생성 오류 발생시 처리하는  프로시져이다
* 작성자 : Jangsub.nam
* 작성년월일 : 2021.09.02
* 최종수정자 : Jangsub.nam
* 최종수정일 : 2021.09.02
* 특이 사항   : 수신테이블  MANAGE_TBL에서  ERROR_CODE 값이 '00000000' 아닌것중에 세금계산서에 해당하는 것만 처리 한다.
*         처리완료시     UPDATE_IND 'Y' 설정한다.
*         처리오류시     UPDATE_IND 'E' 설정한다.
*/
 
IS
   C_CSEQ_RCV_YN VARCHAR(01) := '';
   C_SND_YN      VARCHAR(01) := '';  
   
   CURSOR C1 IS
		SELECT A.DOC_MSG_ID, A.XML_MSG_ID , A.ERROR_CODE,A.XML_FLOW ,A.UPDATE_IND ,A.CUR_STATUS ,
               B.BGM_1001, B.BGM_1004,  A.ERROR_MSG
		  FROM KLUSER01.MANAGE_TBL a  INNER JOIN KLUSER01.TAX_HD B 
		                                      ON A.XML_MSG_ID = B.XML_MSG_ID 
		 WHERE  A.ERROR_CODE > '00000000'
		   AND A.XML_FLOW     = 'S'
		   AND A.UPDATE_IND   = 'N'
		   AND A.CUR_STATUS   = 'ER'
		 ORDER BY A.DOC_MSG_ID;
				         
/* 1. edi 무선관리 테이블에서 오류문서를 읽어온다
 * 2. 문서의 종류에 따라 해당 문서의 결과를 Update 한다.
*/

BEGIN
    FOR CUR1 IN C1 LOOP    
		
	    BEGIN	
	   		SELECT nvl(CSEQ_RCV_YN,'N'), nvl(SND_YN,'N')
			  into C_CSEQ_RCV_YN, C_SND_YN
			  FROM goldmoon.TB_AR04M01
			 WHERE TAX_BILG_NO = CUR1.BGM_1004;
	
			IF (C_SND_YN = 'Y') THEN		  --전송완료
				IF C_CSEQ_RCV_YN != 'Y' THEN  --시스템응답없음
					UPDATE goldmoon.TB_AR04M01
					SET  CSEQ_RCV_YN	= 'E'	-- 계산서 처리결과
						,CSEQ_RCV_DTTM	= sysdate	-- 처리결과회신일자
						,ERR_CD         = CUR1.ERROR_CODE	--오류코드
						,ERR_CNTS       = CUR1.ERROR_MSG 	--오류메세지
					WHERE TAX_BILG_NO = CUR1.BGM_1004;	--계산서발행번호
				END IF;    		
			END IF;     				
	
		    -- 응답문서 처리완료 설정     
			UPDATE KLUSER01.MANAGE_TBL
			   SET UPDATE_IND = 'Y'				-- 처리완료
			 WHERE DOC_MSG_ID = CUR1.DOC_MSG_ID
			   AND XML_MSG_ID = CUR1.XML_MSG_ID;
	 
	 	    COMMIT;
			
	     EXCEPTION WHEN OTHERS THEN
	        	BEGIN
		            DBMS_OUTPUT.PUT_LINE('에러 발생 계산서번호 : ' || CUR1.BGM_1004 ||'-'||CUR1.DOC_MSG_ID);
			   	    -- 응답문서 오류 설정     
					UPDATE KLUSER01.MANAGE_TBL
					   SET UPDATE_IND = 'E'				-- 오류
					 WHERE DOC_MSG_ID = CUR1.DOC_MSG_ID
					   AND XML_MSG_ID = CUR1.XML_MSG_ID;
					COMMIT;
				END;
	     END;
     END LOOP;
      
END;
