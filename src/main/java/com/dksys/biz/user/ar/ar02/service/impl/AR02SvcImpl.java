package com.dksys.biz.user.ar.ar02.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.dksys.biz.user.ar.ar02.mapper.AR02Mapper;
import com.dksys.biz.user.ar.ar02.service.AR02Svc;
import com.dksys.biz.user.sd.sd07.mapper.SD07Mapper;
import com.dksys.biz.user.sm.sm01.mapper.SM01Mapper;
import com.dksys.biz.util.DateUtil;

@Service
@Transactional("erpTransactionManager")
public class AR02SvcImpl implements AR02Svc {

	@Autowired
    AR02Mapper ar02Mapper;
	
	@Autowired
	SD07Mapper sd07Mapper;
	
	@Autowired
    SM01Mapper sm01Mapper;
	
	@Autowired
    AR02Svc ar02Svc;
	
	@Override
	public int selectSellMainCount(Map<String, String> paramMap) {
		return ar02Mapper.selectSellMainCount(paramMap);
	}
	
	@Override
	public List<Map<String, String>> selectSellMainList(Map<String, String> paramMap) {
		return ar02Mapper.selectSellMainList(paramMap);
	}
	
	@Override
	public int selectSellCount(Map<String, String> paramMap) {
		return ar02Mapper.selectSellCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectSellList(Map<String, String> paramMap) {
		return ar02Mapper.selectSellList(paramMap);
	}

	@Override
	@SuppressWarnings("all")
	public int updatePchsSell(Map<String, Object> paramMap) {
		int result = 0;
		int bilgAmt = 0;
		String selpchCd = "";
		List<Map<String, String>> detailList = (List<Map<String, String>>) paramMap.get("detailArr");
		for (Map<String, String> detailMap : detailList) {
			selpchCd = detailMap.get("selpchCd");
			detailMap.put("userId", paramMap.get("userId").toString());
			detailMap.put("pgmId", paramMap.get("pgmId").toString());
			result += ar02Mapper.updatePchsSell(detailMap);
			bilgAmt += Integer.parseInt(String.valueOf(detailMap.get("totAmt")));

			//마감 체크
			Map<String, String> closeChkMap = new HashMap<String, String>();
			closeChkMap.put("dlvrDttm",detailMap.get("trstDt").toString());
			closeChkMap.put("coCd",detailMap.get("coCd").toString());
	        if("SELPCH1".equals(paramMap.get("selpchCd"))) {
				if(ar02Svc.checkPchsClose(closeChkMap)) {
					return 500;				
				}
	        }
	        if("SELPCH2".equals(paramMap.get("selpchCd"))) {
				if(ar02Svc.checkSellClose(closeChkMap)) {
					return 501;
				}	
	        }
		}
		if("SELPCH2".equals(selpchCd) || "SELPCH4".equals(selpchCd)) {
			int creditAmt = Integer.parseInt(paramMap.get("creditAmt").toString());
			int exceedAmt = bilgAmt - creditAmt;
			if(exceedAmt > 0) {
				Map<String, String> param = new HashMap<String, String>();
				param.put("clntCd", paramMap.get("clntCd").toString());
				param.put("coCd", paramMap.get("coCd").toString());
				param.put("realTotTrstAmt", String.valueOf(exceedAmt));
				param.put("dlvrDttm", detailList.get(0).get("trstDt").replace("-", ""));
				if(checkLoan(param)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return 0;
				}
			} else {
				paramMap.put("creditAmt", String.valueOf(exceedAmt));
				paramMap.put("dlvrDttm", detailList.get(0).get("trstDt").replace("-", ""));
				if(creditDeposit(paramMap)) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return 0;
				}
			}
		}
		return result;
	}

	@Override
	public int deleteSell(Map<String, String> paramMap) {
		
		//마감 체크
        if("SELPCH1".equals(paramMap.get("selpchCd"))) {
        	paramMap.put("dlvrDttm",paramMap.get("trstDt").toString());
			if(ar02Svc.checkPchsClose(paramMap)) {
				return 500;				
			}
        }
        if("SELPCH2".equals(paramMap.get("selpchCd"))) {
        	paramMap.put("dlvrDttm",paramMap.get("trstDt").toString());
			if(ar02Svc.checkSellClose(paramMap)) {
				return 501;
			}	
        }
        
		Map<String, String> resultMap = ar02Mapper.selectSellInfo(paramMap);
		// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
		if("OWNER1".equals(resultMap.get("ownerCd").toString())) {		
			resultMap.put("CLNT_CD",  ar02Mapper.selectOwner1ClntCd(paramMap));		
		}		
		
		Map<String, String> stockInfo = sm01Mapper.selectStockInfo(resultMap);
		if(stockInfo != null) {
			int stockQty = 0;
			int stockWt = 0;
			String stockChgCd = "STOCKCHG09";
			if("SELPCH2".equals(resultMap.get("selpchCd"))) 
			{
				stockChgCd = "STOCKCHG09";
				stockQty = Integer.parseInt(stockInfo.get("stockQty")) + Integer.parseInt(resultMap.get("realTrstQty"));
				stockWt  = Integer.parseInt(stockInfo.get("stockWt"))  + Integer.parseInt(resultMap.get("realTrstWt"));
			} else 
			{
				stockChgCd = "STOCKCHG08";
				stockQty = Integer.parseInt(stockInfo.get("stockQty")) - Integer.parseInt(resultMap.get("realTrstQty"));
				stockWt  = Integer.parseInt(stockInfo.get("stockWt"))  - Integer.parseInt(resultMap.get("realTrstWt"));
			}
			resultMap.put("STOCK_QTY", String.valueOf(stockQty));
			resultMap.put("STOCK_WT",  String.valueOf(stockWt));
			resultMap.put("STOCK_UPR", stockInfo.get("stockUpr"));
			resultMap.put("STD_UPR", stockInfo.get("stdUpr"));
			resultMap.put("STOCK_CHG_CD", stockChgCd);
			resultMap.put("USER_ID", paramMap.get("userId"));
			resultMap.put("PGM_ID", paramMap.get("pgmId"));
			sm01Mapper.updateStockSell(resultMap);						
		}
		
		return ar02Mapper.deleteSell(paramMap);
	}

	@Override
	public int insertPchsSell(Map<String, String> paramMap) {
		//마감 체크
        if("SELPCH1".equals(paramMap.get("selpchCd"))) {
        	paramMap.put("dlvrDttm",paramMap.get("trstDt").toString());
			if(ar02Svc.checkPchsClose(paramMap)) {
				return 500;				
			}
        }
        if("SELPCH2".equals(paramMap.get("selpchCd"))) {
        	paramMap.put("dlvrDttm",paramMap.get("trstDt").toString());
			if(ar02Svc.checkSellClose(paramMap)) {
				return 501;
			}	
        }
		int result = 0;
		long realTotTrstAmt = 0;
		int stockQty = Integer.parseInt(paramMap.get("realTrstQty"));
		int stockWt  = Integer.parseInt(paramMap.get("realTrstWt"));
		String clntCd = paramMap.get("clntCd");
		
		if(paramMap.containsKey("prdtStockCd") && "Y".equals(paramMap.get("prdtStockCd").toString())) 
		{
			// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
			if("OWNER1".equals(paramMap.get("ownerCd").toString())) {		
				paramMap.put("clntCd",  ar02Mapper.selectOwner1ClntCd(paramMap));		
			}
		}
		// 재고정보 update
		Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
		// 거래처 원복
		paramMap.put("clntCd", clntCd);
		
		if(stockInfo == null) {
			paramMap.put("pchsUpr", paramMap.get("realTrstUpr"));
			paramMap.put("sellUpr", paramMap.get("realTrstUpr"));
			paramMap.put("stockUpr", paramMap.get("realTrstUpr"));
			paramMap.put("stdUpr", paramMap.get("realTrstUpr"));
			stockQty = "SELPCH1".equals(paramMap.get("selpchCd")) ? stockQty : stockQty*-1;
			stockWt  = "SELPCH1".equals(paramMap.get("selpchCd")) ? stockWt : stockWt*-1;
			paramMap.put("stockQty", String.valueOf(stockQty));
			paramMap.put("stockWt", String.valueOf(stockWt));
		} else {
			//매출일 떄 
			if("SELPCH2".equals(paramMap.get("selpchCd"))) 
			{
				paramMap.put("sellUpr", paramMap.get("realTrstUpr"));
				paramMap.put("pchsUpr", stockInfo.get("pchsUpr"));
				stockQty = Integer.parseInt(stockInfo.get("stockQty")) - stockQty;
				stockWt  = Integer.parseInt(stockInfo.get("stockWt")) - stockWt;
			}
			//매입일 떄
			else 
			{
				paramMap.put("pchsUpr", paramMap.get("realTrstUpr"));
				paramMap.put("sellUpr", stockInfo.get("sellUpr"));
				stockQty = Integer.parseInt(stockInfo.get("stockQty")) + stockQty;
				stockWt  = Integer.parseInt(stockInfo.get("stockWt")) + stockWt;
			}
			paramMap.put("stockUpr", stockInfo.get("stockUpr"));
			paramMap.put("stdUpr", stockInfo.get("stdUpr"));
			paramMap.put("stockQty", String.valueOf(stockQty));
			paramMap.put("stockWt",  String.valueOf(stockWt));
		}
		
		paramMap.put("taxivcCoprt", paramMap.get("estCoprt"));
		long bilgAmt    = Long.parseLong(paramMap.get("bilgAmt"));
		long bilgVatAmt = ar02Mapper.selectBilgVatAmt(paramMap);
		paramMap.put("bilgVatAmt", String.valueOf(bilgVatAmt));
		realTotTrstAmt = realTotTrstAmt + bilgAmt + bilgVatAmt;
		result = ar02Mapper.insertPchsSell(paramMap);
		if(paramMap.containsKey("prdtStockCd") && "Y".equals(paramMap.get("prdtStockCd").toString())) 
		{
			// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
			if("OWNER1".equals(paramMap.get("ownerCd").toString())) {		
				paramMap.put("clntCd",  ar02Mapper.selectOwner1ClntCd(paramMap));		
			}
			sm01Mapper.updateStockSell(paramMap);
		}
		// 여신 체크
		paramMap.put("realTotTrstAmt", String.valueOf(realTotTrstAmt));
		paramMap.put("clntCd", clntCd);
		paramMap.put("dlvrDttm", paramMap.get("trstDt"));
		if("SELPCH2".equals(paramMap.get("selpchCd"))) {
			if(ar02Svc.checkLoan(paramMap)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return 0;
			}	
		}
		return result;
	}
	
	@Override
	public void insertSalesDivision(List<Map<String, String>> paramList) {
		// 여신체크
		long divTotAmt = 0;
		for(Map<String, String> paramMap : paramList) {
			divTotAmt += Long.parseLong(paramMap.get("divTotAmt"));
		}
		
		for(Map<String, String> paramMap : paramList) {
			// paramList 순회하며 넘어온값 그대로 update
//			ar02Mapper.updatePchsSell(paramMap);
			// paramList 순회하며 분할된 데이터를 map에 update후 insert
			Map<String, String> divMap = new HashMap<String, String>();
			divMap.putAll(paramMap);
			// 거래처
			divMap.put("clntCd", paramMap.get("divClntCd"));
			divMap.put("clntNm", paramMap.get("divClntNm"));
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
			// 사용자 아이디
			divMap.put("userId", paramMap.get("userId"));
			// 생성 프로그램 아이디: 분할 매출 생성시 기존데이터와 동일하게 유지
			divMap.put("pgmId", paramMap.get("creatPgm"));
			// 수정 프로그램 아이디: 분할 화면ID
			divMap.put("updatePgmId", paramMap.get("updatePgmId"));
			// insert
//			ar02Mapper.insertPchsSell(divMap);
		}
	}
	
	@Override
	public void updateSalesClnt(List<Map<String, String>> paramList) {
		for(Map<String, String> paramMap : paramList) {
			ar02Mapper.updateSalesClnt(paramMap);
		}
	}

	@Override
	public Map<String, String> selectSellInfo(Map<String, String> paramMap) {
		return ar02Mapper.selectSellInfo(paramMap);
	}

	public boolean creditDeposit(Map<String, Object> paramMap) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("loanCd", 'M');
			map.put("clntCd", paramMap.get("clntCd"));
			map.put("coCd", paramMap.get("coCd"));
			map.put("iTrDt", paramMap.get("dlvrDttm").toString().replace("-", ""));
			map.put("amt", Integer.parseInt((String) paramMap.get("creditAmt")));
			ar02Mapper.callCreditLoan(map);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return true;
		}
		return false;
	}
	
	public boolean checkLoan(Map<String, String> paramMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loanCd", 'C');
		map.put("clntCd", paramMap.get("clntCd"));
		map.put("coCd", paramMap.get("coCd"));
		map.put("iTrDt", paramMap.get("dlvrDttm").replace("-", ""));
		map.put("amt", paramMap.get("realTotTrstAmt"));
		long result = ar02Mapper.callCreditLoan(map);
		if(result < Long.parseLong(paramMap.get("realTotTrstAmt"))) {
			return true;
		} else {
			map.put("loanCd", 'P');
			map.put("iTrDt", paramMap.get("dlvrDttm").replace("-", ""));
			ar02Mapper.callCreditLoan(map);
		}
		return false;
	}
	
	@Override
	public List<Map<String, String>> selectSellSumList(Map<String, String> paramMap) {
		return ar02Mapper.selectSellSumList(paramMap);
	}
	
	@Override
	public boolean checkSellClose(Map<String, String> paramMap) {
		String trstDt = paramMap.get("dlvrDttm").replace("-", "");
		paramMap.put("closeYm", trstDt.substring(0,6));
		Map<String, String> sd07result = sd07Mapper.selectClose(paramMap);
		Map<String, String> sd07resultMax = sd07Mapper.selectMaxCloseDay(paramMap);
		if(sd07result != null) {
			int today = Integer.parseInt(DateUtil.getCurrentYyyymmdd());
			int closeDay = Integer.parseInt(sd07result.get("sellCloseDttm").replace("-", ""));
			if("Y".equals(sd07result.get("sellCloseYn")) && today > closeDay) {
				return true;
			}
		}
		if(sd07resultMax != null) {
			int maxSellCloseDay = Integer.parseInt(sd07resultMax.get("maxSellCloseDay").replace("-", ""));
			if(maxSellCloseDay > Integer.parseInt(trstDt)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean checkPchsClose(Map<String, String> paramMap) {
		/*
		 * 1. 기준월에 대한 수정 가능 여부 확인 
		 * 2. 수정하고자 하는 자료의 일자가 마감이 된 MAX월 이전인지 확인 
		 */
		String trstDt = paramMap.get("dlvrDttm").replace("-", "");
		paramMap.put("closeYm", trstDt.substring(0,6));
		Map<String, String> sd07result = sd07Mapper.selectClose(paramMap);
		Map<String, String> sd07resultMax = sd07Mapper.selectMaxCloseDay(paramMap);
		if(sd07result != null) {
			int today       = Integer.parseInt(DateUtil.getCurrentYyyymmdd());
			int closeDay    = Integer.parseInt(sd07result.get("pchsCloseDttm").replace("-", ""));
			if("Y".equals(sd07result.get("pchsCloseYn")) && today > closeDay) {
				return true;
			}
		}
		if(sd07resultMax != null) {
			int maxPchsCloseDay = Integer.parseInt(sd07resultMax.get("maxPchsCloseDay").replace("-", ""));
			if(maxPchsCloseDay > Integer.parseInt(trstDt)) {
				return true;
			}
		}
		return false;
	}

}