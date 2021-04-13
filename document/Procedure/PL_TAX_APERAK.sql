CREATE OR REPLACE PROCEDURE PL_TAX_APERAK
/*
* 프로시저명 : 전자세금계산서 응답문서 처리
* 프로시저 ID: PL_TAX_APERAK
* 내용 :  전자세금계산서 전송처리에 대한 결과를 응답문서로 수신받아 처리하는 프로시져이다
* 작성자 : ABC
* 작성년월일 : 2021.04.06
* 최종수정자 :
* 최종수정일 :
* 특이 사항   : 수신테이블  mapin_tbl의 CUR_STATUS 값이 'Y' 아닌것만 처리 한다.
*         처리완료시     CUR_STATUS를 'Y' 설정한다.
*/
 
IS
   C_CSEQ_RCV_YN VARCHAR(01) := '';
   C_TAX_ADMS_YN VARCHAR(01) := '';
   C_RCV_YN      VARCHAR(01) := '';
   C_RCV_PROC_ID VARCHAR(20) := '';
	
   CURSOR C1 IS
		SELECT * 
		FROM KLUSER01.MAPIN_TBL a 
		       LEFT OUTER JOIN KLUSER01.APERAK_HD b
		            ON a.XML_MSG_ID = b.XML_MSG_ID 
		           AND a.XMLDOC_SEQ = b.XMLDOC_SEQ 
		       LEFT OUTER JOIN KLUSER01.APERAK_DTL c 
		            ON a.XML_MSG_ID = c.XML_MSG_ID 
		           AND a.XMLDOC_SEQ = c.XMLDOC_SEQ
		WHERE a.DOC_NAME = 'APERAK'
		  AND a.DOC_CODE = '294'
		  AND a.ORIGINATOR = 'KLLOGISBIL'
		  AND a.RECIPIENT = 'KLDKSYL001'
		  AND A.CUR_STATUS IS null ;
		         
/* 1. 응답문서를 읽어온다
 * 2. 문서의 종류에 따라 해당 문서의 결과를 Update 한다.
 * CUR1.DOC_1001   : '938':세금계산서에 대한 응답
 *                   '780':거래명세서에 대한 응답
 *                   '2'  :입금쵸에 대한 응답
 *
 * CUR1.RFFZZZ1154 : '1' : 시스템응답
 *                   '2' : 담당자응답
 *                   '3' : 국세청응답(세금계산서일 경우만 전송됨)   
 *
 * CUR1.ERC_9321   : 'OK' : 정상
 *                   'ER' : 오류
 *
 * CUR1.ERC___1131 : 1) 시스템 응답
 *                      'O' : 시스템 정상
 *                      'E' : 시스템 에러
 *                   2) 담당자 응답
 *                      'C' : 승인
 *                      'W' : 거절
 *                      'L' : 삭제(공급자 액션)
 *                   3) 국세청 응답
 *                      'NPO' : 국세청 처리 정상
 *                      'NPE' : 국세청 처리 오류
 *    
 * CUR1.FTXAAP_440 : 에러메세지 혹은 거절사유
*/

BEGIN
    FOR CUR1 IN C1 LOOP    

		
     BEGIN
     
     	SELECT CSEQ_RCV_YN,   TAX_ADMS_YN ,  RCV_YN,   RCV_PROC_ID
		  into C_CSEQ_RCV_YN, C_TAX_ADMS_YN, C_RCV_YN, C_RCV_PROC_ID
		  FROM goldmoon.TB_AR04M01
		 WHERE TAX_BILG_NO = CUR1.RFFACE1154;
		
     	IF (CUR1.DOC_1001 = '938') THEN  --전자세금계산서

			--RFFZZZ1154  = 1:시스템 응답, 2:담당자응답,  3:국세청응답
     		IF (CUR1.ERC_9321 = 'OK')  THEN --정상 (OK:정상, ER:오류)		
				IF (CUR1.RFFZZZ1154 = '1' AND CUR1.ERC___1131 = 'O') THEN		--시스템응답
					UPDATE goldmoon.TB_AR04M01
					SET  CSEQ_RCV_YN	= 'Y'	-- 계산서 처리결과
						,CSEQ_RCV_DTTM	= CUR1.TIMESTAMP	-- 처리결과회신일자
					WHERE TAX_BILG_NO = CUR1.RFFACE1154;		--계산서발행번호
						
				ELSIF (CUR1.RFFZZZ1154 = '2' AND CUR1.ERC___1131 = 'C') THEN	--담당자응답
					UPDATE goldmoon.TB_AR04M01
					SET  TAX_ADMS_YN	= 'Y'	--승인여부 RFFZZZ1154  = 1:시스템 응답, 2:담당자응답,  3:국세청응답
						,TAX_ADMS_DTTM	= CUR1.TIMESTAMP	-- 처리결과회신일자
					WHERE TAX_BILG_NO = CUR1.RFFACE1154;	--계산서발행번호

				ELSIF (CUR1.RFFZZZ1154 = '3' AND CUR1.ERC___1131 = 'NPO') THEN	--국세청응답
					IF C_RCV_PROC_ID IS NOT NULL THEN
						UPDATE goldmoon.TB_AR04M01
						SET  RCV_YN			= 'Y'	-- 국세청수신여부
							,RCV_DTTM		= CUR1.TIMESTAMP	-- 처리결과회신일자
							,RCV_PROC_ID	= CUR1.RFFSZ_1154	-- 국세청승인번호
						WHERE TAX_BILG_NO   = CUR1.RFFACE1154;	--계산서발행번호
					END IF;	
				END IF;

			ELSE -- (CUR1.ERC_9321 = 'ER')  THEN --정상 (OK:정상, ER:오류)
					
			
				IF (CUR1.RFFZZZ1154 = '1') THEN		--시스템응답
					IF C_CSEQ_RCV_YN != 'Y' THEN
						UPDATE goldmoon.TB_AR04M01
						SET  CSEQ_RCV_YN	= 'E'	-- 계산서 처리결과
							,CSEQ_RCV_DTTM	= CUR1.TIMESTAMP	-- 처리결과회신일자
							,ERR_CD         = CUR1.ERC___1131	--오류코드
							,ERR_CNTS       = CUR1.FTXAAP_440 	--오류메세지
						WHERE TAX_BILG_NO = CUR1.RFFACE1154;	--계산서발행번호
					END IF;
				ELSIF (CUR1.RFFZZZ1154 = '2') THEN	--담당자응답
					IF C_TAX_ADMS_YN != 'Y' THEN
						UPDATE goldmoon.TB_AR04M01
						SET  TAX_ADMS_YN	= 'E'	--승인여부 RFFZZZ1154  = 1:시스템 응답, 2:담당자응답,  3:국세청응답
							,TAX_ADMS_DTTM	= CUR1.TIMESTAMP	-- 처리결과회신일자
							,ERR_CD         = CUR1.ERC___1131	--오류코드
							,ERR_CNTS       = CUR1.FTXAAP_440 	--오류메세지
						WHERE TAX_BILG_NO   = CUR1.RFFACE1154;	--계산서발행번호
					END IF;
	
				ELSIF (CUR1.RFFZZZ1154 = '3') THEN	--국세청응답
					IF C_RCV_YN != 'Y' THEN
						UPDATE goldmoon.TB_AR04M01
						SET  RCV_YN			= 'E'				-- 국세청수신여부
							,RCV_DTTM		= CUR1.TIMESTAMP	-- 처리결과회신일자
							,RCV_PROC_ID	= ''				-- 국세청승인번호
							,ERR_CD         = CUR1.ERC___1131	--오류코드
							,ERR_CNTS       = CUR1.FTXAAP_440 	--오류메세지
						WHERE TAX_BILG_NO   = CUR1.RFFACE1154;	--계산서발행번호
					END IF;
	
				END IF;
			
			END IF  ;   	
     	     		
/*     	ELSIF (CUR1.DOC_1001 = '780') THEN  --거래명세서
     		UPDATE TB_AR04M01
*/     		
		END IF;     				

		EXCEPTION WHEN OTHERS THEN NULL;
	         
	    -- 응답문서 처리완료 설정     
		UPDATE KLUSER01.MAPIN_TBL
		   SET CUR_STATUS = 'Y'				-- 처리완료
		 WHERE DOC_MSG_ID = CUR1.DOC_MSG_ID
		   AND XMLDOC_SEQ = CUR1.XMLDOC_SEQ;
 
 	    COMMIT;
		
--		EXCEPTION WHEN OTHERS THEN NULL;
	         
      END;
     END LOOP;
      

END;
