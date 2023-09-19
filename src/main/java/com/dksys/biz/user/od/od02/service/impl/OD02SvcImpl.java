package com.dksys.biz.user.od.od02.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar02.mapper.AR02Mapper;
import com.dksys.biz.user.ar.ar02.service.AR02Svc;
import com.dksys.biz.user.od.od02.mapper.OD02Mapper;
import com.dksys.biz.user.od.od02.service.OD02Svc;
import com.dksys.biz.util.ExceptionThrower;

@Service
@Transactional(rollbackFor = Exception.class)
public class OD02SvcImpl implements OD02Svc {
    
	@Autowired
    OD02Mapper od02Mapper;
	
	@Autowired
    AR02Mapper ar02Mapper;

	@Autowired
    AR02Svc ar02Svc;
	
    @Autowired
    ExceptionThrower thrower;
	
	@Override
	public int selectPurchaseCount(Map<String, String> paramMap) {
		return od02Mapper.selectPurchaseCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectPurchaseList(Map<String, String> paramMap) {
		return od02Mapper.selectPurchaseList(paramMap);
	}
	
	@Override
	public void insertSalesDivision(List<Map<String, String>> paramList) throws Exception{
		
		Map<String, String> baseMap = paramList.get(0);
		// 마감 체크
		if(!ar02Svc.checkPchsClose(baseMap)) {
    		thrower.throwCommonException("pchsClose");
		}
		
		/* 여신체크 start */
		/*
		Map<String, Object> loanMap = new HashMap<String, Object>();
		loanMap.put("coCd", baseMap.get("coCd"));
		loanMap.put("clntCd", baseMap.get("divClntCd"));
		loanMap.put("prdtGrp", baseMap.get("prdtGrp"));
		loanMap.put("trstDt", baseMap.get("trstDt"));
		
		// 분할 총금액 계산
		long totAmt = 0;
		for(int i=0;i<paramList.size();i++) {
			Map<String, String> paramMap = paramList.get(i);
			totAmt += Long.parseLong(paramMap.get("divTotAmt"));
		}
		loanMap.put("totAmt", totAmt);
		
		
		long diffLoan =  checkLoan(loanMap);
		if(diffLoan < 0) {
			thrower.throwCreditLoanException("", diffLoan);
		}
		*/
		/* 여신체크 end */
		
		for(Map<String, String> paramMap : paramList) {
			
			/*
			List<Map<String, String>> countSellList = ar02Mapper.countSellList(paramMap);
			
			if(countSellList.size() != 0) {
				for(int i=0; i<countSellList.size(); i++) {
					paramMap.put("etrdpsSeqFind", MapUtils.getString(countSellList.get(i), "ETRDPS_SEQ"));
					int countSell = ar02Mapper.countSellFind(paramMap);
					paramMap.put("countSell", String.valueOf(countSell));
					ar02Mapper.updatePchsSell(paramMap);
					// ar02Mapper.deleteSell(deUpMap);
				}
			}
			*/
			
			// 기존에는 매핑된 리스트에 따라서 for문을 돌게되어있었지만, 매핑되지 않은 매입과 매출리스트 건에 대한 분할 및 정산이 이루어질 수 있어서 수정
			ar02Mapper.updatePchsSellPart2(paramMap);
			
			// ar02Mapper.deleteSell05D(paramMap);
			
			// paramList 순회하며 넘어온값 그대로 update
			// ar02Mapper.updatePchsSell(paramMap);
			// paramList 순회하며 분할된 데이터를 map에 update후 insert
			Map<String, String> divMap = new HashMap<String, String>();
			divMap.putAll(paramMap);
			// 거래처
			divMap.put("clntCd", paramMap.get("divClntCd"));
			divMap.put("clntNm", paramMap.get("divClntNm"));
			// 번들/본수
			divMap.put("bdCnt", paramMap.get("divBdCnt"));
			// 수량
			divMap.put("realTrstQty", paramMap.get("divRealTrstQty"));
			// 중량
			divMap.put("realTrstWt", paramMap.get("divRealTrstWt"));
			// 금액
			divMap.put("realTrstAmt", paramMap.get("divRealTrstAmt"));
			// 청구수량
			divMap.put("bilgQty", paramMap.get("divBilgQty"));
			// 청구중량
			divMap.put("bilgWt", paramMap.get("divBilgWt"));
			// 청구금액
			divMap.put("bilgAmt", paramMap.get("divBilgAmt"));
			// 부가세
			divMap.put("bilgVatAmt", paramMap.get("divBilgVatAmt"));
			// 할인금액
			divMap.put("trstDcAmt", paramMap.get("divTrstDcAmt"));
			// 비고
			divMap.put("trspRmk", paramMap.get("divTrspRmk"));
			// 사용자 아이디
			divMap.put("userId", paramMap.get("userId"));
			// 생성 프로그램 아이디: 분할 매출 생성시 기존데이터와 동일하게 유지
			divMap.put("pgmId", paramMap.get("creatPgm"));
			// 수정 프로그램 아이디: 분할 화면ID
			divMap.put("updatePgmId", paramMap.get("updatePgmId"));
			if(paramMap.get("subClntCd") != null) {
				// 매출분할 전 매입거래처코드
				divMap.put("subClntCd", paramMap.get("subClntCd"));
				// 매출분할 전 매입거래처명
				divMap.put("subClntNm", paramMap.get("subClntNm"));
			} else {
				// 매출분할 전 매입거래처코드
				divMap.put("subClntCd", "");
				// 매출분할 전 매입거래처명
				divMap.put("subClntNm", "");
			}
			
			// insert
			ar02Mapper.insertPchsSell(divMap);
		}
		
		/*
		Map<String, String> matchMap = new HashMap<String, String>();
		matchMap.put("coCd", paramList.get(0).get("coCd"));
		matchMap.put("clntCd", paramList.get(0).get("clntCd"));
		matchMap.put("prdtGrp", paramList.get(0).get("prdtGrp"));
		
		ar02Mapper.callSaleMatch(matchMap);
		
		matchMap.put("clntCd", paramList.get(0).get("divClntCd"));
		
		ar02Mapper.callSaleMatch(matchMap);
		*/
		
		/*
		// 최종 여신 체크 / 여신 차감
		diffLoan = ar02Svc.checkLoan(loanMap);
		if(diffLoan < 0) {
        	thrower.throwCreditLoanException("", diffLoan);
        }else {
        	// 여신 차감후 음수 return시 롤백 
        	long loanPrcsResult = ar02Svc.deductLoan(loanMap);
        	if(loanPrcsResult < 0) {
    			throw new Exception();
    		}
        }
        */
	}

}