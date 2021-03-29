CREATE OR REPLACE FUNCTION F_CREDIT_LOAN(I_CD CHAR,I_CLNT_CD number, I_CO_CD varchar, I_TR_DT VARCHAR, I_AMT number)
    RETURN number

/******************************************************************************
* TYPE			: FUNCTION (Tibero)
* NAME			: F_CREDIT_LOAN
* DEVELOPER		: Jangsub.Nam
* DESCRIPTION	: 입력받은 코드, 거래처번호, 법인코드,  금액, -9999:Exception 발생)
* 	 구분 : C = 여신 잔액 체크, 잔액  Return
*		  M = 여신할당금액증가, 처리금액
*		  P = 여신할당금액감소, 처리금액
*
*		여신 잔액부족이나 오류 발생시 -9999 Return
*
*	SELECT f_CREDIT_LOAN('C', 1, 'GGS','20210101',0) FROM DUAL; -- 여신잔액
*	SELECT f_CREDIT_LOAN('M', 1, 'GGS','20210101', 100000) FROM DUAL; 여신할당금액 100,000원 증가
*	SELECT f_CREDIT_LOAN('P', 1, 'GGS','20210101', 100000) FROM DUAL; 여신할당금액 100,000원 감소
*
*  I_CD 구분
*  I_CLNT_CD 거래처 
*  I_CO_CD 회사
*  I_TR_DT 기준일자( 매출일자(P)/수금일자(M)/조회일자(C) )
*  I_AMT  금액
******************************************************************************/

AS
    O_BLCE_AMT 	number := 0;  
	O_GRP_AMT 	number := 0;	/*그룹 한도 금액*/
    O_CO_AMT 	number := 0;	/*사별 한도 금액*/
    
    C_GRP_AMT 	number := 0;	/*그룹여신 잔여 금액*/
    C_CO_AMT 	number := 0;	/*사별여신 잔여 금액*/
    
    REM_AMT 	number := 0;	/* 잔여 금액*/
    
    O_GRP_PLDG_AMT number := 0;
    O_GRP_BLCE_AMT number := 0;
    O_CO_PLDG_AMT number := 0;
    O_CO_BLCE_AMT number := 0;
    
BEGIN
/**************************************************************
* -여신 잔액 체크
*  SELECT f_CREDIT_LOAN('C', 1, 'GGS','20210131',0) FROM DUAL; -- 여신잔액
*   1. 그룹 여신 금액을 먼저 할당하고
*   2. 그룹여신이 없으면 사별 담보여신금액에 할당한다
***************************************************************/
	IF I_CD = 'C' THEN
	--거래처 여신금액 관리 테이블(그룹여신금액 - 그룹여신할당금액,
	--                 사별여신금액 - 사별여신할당금액)
		SELECT GRP_PLDG_AMT, GRP_BLCE_AMT ,  CO_PLDG_AMT, CO_BLCE_AMT
		  into O_GRP_PLDG_AMT, O_GRP_BLCE_AMT, O_CO_PLDG_AMT, O_CO_BLCE_AMT
		FROM TB_BM03M01 a FULL OUTER join  TB_BM04M01 b
		ON a.CLNT_CD = b.CLNT_CD 
		WHERE a.CLNT_CD = I_CLNT_CD
		AND b.CO_CD = I_CO_CD ;
		
		O_BLCE_AMT := 0;
		--그룹여신 잔액이 남아 있는지 확인
		C_GRP_AMT := O_GRP_PLDG_AMT - O_GRP_BLCE_AMT;
		if C_GRP_AMT > 0 then
			O_BLCE_AMT := C_GRP_AMT;
		end if;
		
		--사별담보여신 잔액이 남아 있는지 확인
		C_CO_AMT := O_CO_PLDG_AMT - O_CO_BLCE_AMT;		
		if C_CO_AMT > 0 then
			O_BLCE_AMT := O_BLCE_AMT + C_CO_AMT;
		end if;
		
	    RETURN O_BLCE_AMT;  --거래가능 금액 Return (

/**************************************************************
* 여신 차감
*   SELECT f_CREDIT_LOAN('M', 1, 'GGS', '20210203', 100000) FROM DUAL; 여신할당금액 100,000원 증가
*   1. 그룹 여신 금액을 먼저 추가하고
*   2. 그룹여신을 초과하면 초과한 금액만큼 사별 담보여신금액에 추가한다
***************************************************************/		
	ELSIF I_CD = 'M' THEN

		SELECT GRP_PLDG_AMT, GRP_BLCE_AMT ,  CO_PLDG_AMT, CO_BLCE_AMT
		  into O_GRP_PLDG_AMT, O_GRP_BLCE_AMT, O_CO_PLDG_AMT, O_CO_BLCE_AMT
		FROM TB_BM03M01 a,  TB_BM04M01 b
		WHERE a.CLNT_CD = b.CLNT_CD 
		AND a.CLNT_CD = I_CLNT_CD
		AND b.CO_CD = I_CO_CD ;
		
		C_GRP_AMT := O_GRP_PLDG_AMT - O_GRP_BLCE_AMT;
		C_CO_AMT := O_CO_PLDG_AMT - O_CO_BLCE_AMT;
		
		IF (C_GRP_AMT + C_CO_AMT) < I_AMT THEN 
			RETURN -9999;	/*잔액 부족 오류*/
		END IF;	
						
		--그룹여신 잔액이 남아 있는지 확인
		if C_GRP_AMT > 0 then  --그룹잔액이 남아있는지
			IF C_GRP_AMT  >= I_AMT THEN  --그룹여신 잔액이 더많이 남았음
				--S_GRP_BLCE_AMT :=  O_GRP_BLCE_AMT + I_AMT ;
				
				UPDATE TB_BM03M01
				SET    GRP_BLCE_AMT = O_GRP_BLCE_AMT + I_AMT 
				WHERE  CLNT_CD = I_CLNT_CD;
				commit;
				
				RETURN 0;	/*처리완료*/
				
			ELSE

				REM_AMT := I_AMT -  C_GRP_AMT;  --그룹할당하고 남은 금액
 
				C_CO_AMT := O_CO_PLDG_AMT - O_CO_BLCE_AMT;		
				
				IF C_CO_AMT < REM_AMT THEN
					RETURN -9999;	/*잔액 부족 오류*/
				ELSE
					UPDATE TB_BM03M01
					SET GRP_BLCE_AMT = O_GRP_PLDG_AMT  --그룹할당금액 Full
					WHERE CLNT_CD = I_CLNT_CD;
					
					UPDATE TB_BM04M01
					SET CO_BLCE_AMT = O_CO_BLCE_AMT + REM_AMT
					WHERE CLNT_CD = I_CLNT_CD
					AND CO_CD = I_CO_CD;
					commit;	
					
					RETURN 0;	/*처리완료*/
				END IF;
						
    		END IF;
			O_BLCE_AMT := O_GRP_AMT;
			
		else --그룹금액이 없으면
				REM_AMT := I_AMT;  --그룹할당하고 남은 금액
 
				C_CO_AMT := O_CO_PLDG_AMT - O_CO_BLCE_AMT;		
				
				IF C_CO_AMT >= REM_AMT THEN
					UPDATE TB_BM04M01
					SET CO_BLCE_AMT = O_CO_BLCE_AMT + REM_AMT
					WHERE CLNT_CD = I_CLNT_CD
					AND CO_CD = I_CO_CD;
					commit;	
					
					RETURN 0;	/*처리완료*/
				ELSE
					RETURN -9999;	/*잔액 부족 오류*/
				END IF;
		end if;

/**************************************************************
* 숙처리 시
*	SELECT f_CREDIT_LOAN('P', 1, 'GGS', '20210204',100000) FROM DUAL; 여신할당금액 100,000원 감소
*   1. 그룹 여신 금액을 먼저 할당하고
*   2. 그룹여신이 없으면 사별 담보여신금액에 할당한다
***************************************************************/		
	ELSIF I_CD = 'P' THEN
		SELECT GRP_PLDG_AMT, GRP_BLCE_AMT ,  CO_PLDG_AMT, CO_BLCE_AMT
		  into O_GRP_PLDG_AMT, O_GRP_BLCE_AMT, O_CO_PLDG_AMT, O_CO_BLCE_AMT
		FROM TB_BM03M01 a,  TB_BM04M01 b
		WHERE a.CLNT_CD = b.CLNT_CD 
		AND a.CLNT_CD = I_CLNT_CD
		AND b.CO_CD = I_CO_CD ;
		
		C_GRP_AMT := O_GRP_PLDG_AMT - O_GRP_BLCE_AMT;
		C_CO_AMT := O_CO_PLDG_AMT - O_CO_BLCE_AMT;
		
		IF O_GRP_BLCE_AMT >= I_AMT THEN  /* 수금액이 그룹할당금액보다 작으면 */
			UPDATE TB_BM03M01
			SET GRP_BLCE_AMT = O_GRP_BLCE_AMT - I_AMT 
			WHERE CLNT_CD = I_CLNT_CD;
			commit;
		ELSE	/* 수금금액이 할당금액보다 큰경우 */
			REM_AMT := I_AMT - O_GRP_BLCE_AMT;
			--그룹여신 잔액이 남아 있는지 확인
/*			IF O_CO_BLCE_AMT  >= REM_AMT THEN */
				
				UPDATE TB_BM03M01
				SET GRP_BLCE_AMT = 0 
				WHERE CLNT_CD = I_CLNT_CD;

				UPDATE TB_BM04M01
				SET CO_BLCE_AMT = O_CO_BLCE_AMT - REM_AMT
				WHERE CLNT_CD = I_CLNT_CD
				AND CO_CD = I_CO_CD;
				commit;	
/*			END IF;*/
		END IF;	
		RETURN 0;	/*처리완료*/
	ELSE 
		RETURN -9999;	/* 처리구분 오류 */
	END IF;  --I_CD END
	      
	return -9999;
	
exception
    when others then
        dbms_output.put_line('exception occurred! (' || sqlcode || ') : ' || sqlerrm);
        return -9999;

END;