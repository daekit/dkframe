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
import com.dksys.biz.user.sm.sm01.mapper.SM01Mapper;
import com.dksys.biz.util.DateUtil;

@Service
@Transactional("erpTransactionManager")
public class AR02SvcImpl implements AR02Svc {

	@Autowired
    AR02Mapper ar02Mapper;
	
	@Autowired
    SM01Mapper sm01Mapper;
	
	@Autowired
    AR02Svc ar02Svc;
	
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
		List<Map<String, String>> detailList = (List<Map<String, String>>) paramMap.get("detailArr");
		for (Map<String, String> detailMap : detailList) {
			detailMap.put("userId", paramMap.get("userId").toString());
			detailMap.put("pgmId", paramMap.get("pgmId").toString());
			result += ar02Mapper.updatePchsSell(detailMap);
			bilgAmt += Integer.parseInt(String.valueOf(detailMap.get("bilgAmt")));
		}
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
			if(creditDeposit(paramMap)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return 0;
			}
		}
		return result;
	}

	@Override
	public int deleteSell(Map<String, String> paramMap) {
		Map<String, String> resultMap = ar02Mapper.selectSellInfo(paramMap);
		// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
		if("OWNER1".equals(resultMap.get("ownerCd").toString())) {		
			resultMap.put("CLNT_CD",  ar02Mapper.selectOwner1ClntCd(paramMap));		
		}		
		
		Map<String, String> stockInfo = sm01Mapper.selectStockInfo(resultMap);
		if(stockInfo != null) {
			int stockQty = 0;
			String stockChgCd = "STOCKCHG09";
			if("SELPCH2".equals(resultMap.get("selpchCd"))) 
			{
				stockChgCd = "STOCKCHG09";
				stockQty = Integer.parseInt(stockInfo.get("stockQty")) + Integer.parseInt(resultMap.get("realTrstQty"));
			} else 
			{
				stockChgCd = "STOCKCHG08";
				stockQty = Integer.parseInt(stockInfo.get("stockQty")) - Integer.parseInt(resultMap.get("realTrstQty"));
			}
			resultMap.put("STOCK_QTY", String.valueOf(stockQty));
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
		int result = 0;
		int realTotTrstAmt = 0;
		int stockQty = Integer.parseInt(paramMap.get("realTrstQty"));
		String clntCd = paramMap.get("clntCd");
		// 재고정보 update
		Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
		if(stockInfo == null) {
			paramMap.put("pchsUpr", paramMap.get("realTrstUpr"));
			paramMap.put("sellUpr", paramMap.get("realTrstUpr"));
			paramMap.put("stockUpr", paramMap.get("realTrstUpr"));
			paramMap.put("stdUpr", paramMap.get("realTrstUpr"));
			stockQty = "SELPCH1".equals(paramMap.get("selpchCd")) ? stockQty : stockQty*-1;
			paramMap.put("stockQty", String.valueOf(stockQty));
		} else {
			//매출일 떄 
			if("SELPCH2".equals(paramMap.get("selpchCd"))) 
			{
				paramMap.put("sellUpr", paramMap.get("realTrstUpr"));
				paramMap.put("pchsUpr", stockInfo.get("pchsUpr"));
				stockQty = Integer.parseInt(stockInfo.get("stockQty")) - stockQty;
			} 
			//매입일 떄
			else 
			{
				paramMap.put("pchsUpr", paramMap.get("realTrstUpr"));
				paramMap.put("sellUpr", stockInfo.get("sellUpr"));
				stockQty = Integer.parseInt(stockInfo.get("stockQty")) + stockQty;
			}
			paramMap.put("stockUpr", stockInfo.get("stockUpr"));
			paramMap.put("stdUpr", stockInfo.get("stdUpr"));
			paramMap.put("stockQty", String.valueOf(stockQty));
		}
		long bilgVatAmt = ar02Mapper.selectBilgVatAmt(paramMap);
		paramMap.put("bilgVatAmt", String.valueOf(bilgVatAmt));
		realTotTrstAmt += bilgVatAmt;
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
		if(ar02Svc.checkLoan(paramMap)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 0;
		}
		return result;
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
			map.put("iTrDt", DateUtil.getCurrentYyyymmdd());
			map.put("amt", Integer.parseInt((String) paramMap.get("creditAmt")));
			ar02Mapper.callCreditLoan(map);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return true;
		}
		return false;
	}
	
	public void creditWithdraw(Map<String, Object> paramMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loanCd", 'P');
		map.put("clntCd", paramMap.get("clntCd"));
		map.put("coCd", paramMap.get("coCd"));
		map.put("iTrDt", DateUtil.getCurrentYyyymmdd());
		map.put("amt", Integer.parseInt((String) paramMap.get("creditAmt"))*-1);
		ar02Mapper.callCreditLoan(map);
	}
	
	public boolean checkLoan(Map<String, String> paramMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loanCd", 'C');
		map.put("clntCd", paramMap.get("clntCd"));
		map.put("coCd", paramMap.get("coCd"));
		map.put("iTrDt", DateUtil.getCurrentYyyymmdd());
		map.put("amt", paramMap.get("realTotTrstAmt"));
		long result = ar02Mapper.callCreditLoan(map);
		if(result < Integer.parseInt(paramMap.get("realTotTrstAmt"))) {
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
}