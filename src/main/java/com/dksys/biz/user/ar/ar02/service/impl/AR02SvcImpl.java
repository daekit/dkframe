package com.dksys.biz.user.ar.ar02.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar02.mapper.AR02Mapper;
import com.dksys.biz.user.ar.ar02.service.AR02Svc;
import com.dksys.biz.user.sm.sm01.mapper.SM01Mapper;

@Service
@Transactional("erpTransactionManager")
public class AR02SvcImpl implements AR02Svc {

	@Autowired
    AR02Mapper ar02Mapper;
	
	@Autowired
    SM01Mapper sm01Mapper;
	
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
		List<Map<String, String>> detailList = (List<Map<String, String>>) paramMap.get("detailArr");
		for (Map<String, String> detailMap : detailList) {
			detailMap.put("userId", paramMap.get("userId").toString());
			detailMap.put("pgmId", paramMap.get("pgmId").toString());
			result += ar02Mapper.updatePchsSell(detailMap);
		}
		return result;
	}

	@Override
	public int deleteSell(Map<String, String> paramMap) {
		return ar02Mapper.deleteSell(paramMap);
	}

	@Override
	public int insertPchsSell(Map<String, String> paramMap) {
		int result = 0;
		int stockQty = 0;
		result = ar02Mapper.insertPchsSell(paramMap);
		// 재고정보 update
		Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
		paramMap.put("pchsUpr", paramMap.get("realTrstUpr"));
		paramMap.put("sellUpr", paramMap.get("realTrstUpr"));
		paramMap.put("stockUpr", paramMap.get("realTrstUpr"));
		paramMap.put("stdUpr", paramMap.get("realTrstUpr"));
		if(stockInfo == null) {
			paramMap.put("stockQty", paramMap.get("realTrstQty"));
		} else {
			if("SELPCH2".equals(paramMap.get("selpchCd"))) {
				paramMap.put("sellUpr", paramMap.get("realTrstUpr"));
				paramMap.put("pchsUpr", stockInfo.get("pchsUpr"));
				stockQty = Integer.parseInt(stockInfo.get("stockQty")) - Integer.parseInt(paramMap.get("realTrstQty"));
			} else {
				paramMap.put("pchsUpr", paramMap.get("realTrstUpr"));
				paramMap.put("sellUpr", stockInfo.get("sellUpr"));
				stockQty = Integer.parseInt(stockInfo.get("stockQty")) + Integer.parseInt(paramMap.get("realTrstQty"));
			}
			paramMap.put("stockUpr", stockInfo.get("stockUpr"));
			paramMap.put("stdUpr", stockInfo.get("stdUpr"));
			paramMap.put("stockQty", String.valueOf(stockQty));
		}
		sm01Mapper.updateStockSell(paramMap);
		return result;
	}

	@Override
	public Map<String, String> selectSellInfo(Map<String, String> paramMap) {
		return ar02Mapper.selectSellInfo(paramMap);
	}

	public boolean checkLoan(Map<String, String> paramMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loanCd", 'C');
		map.put("clntCd", paramMap.get("clntCd"));
		map.put("coCd", paramMap.get("coCd"));
		map.put("amt", paramMap.get("realTrstAmt"));
		long result = ar02Mapper.callCreditLoan(map);
		if(result < Integer.parseInt(paramMap.get("realTrstAmt"))) {
			return true;
		} else {
			map.put("loanCd", 'P');
			ar02Mapper.callCreditLoan(map);
		}
		return false;
	}

	@Override
	public List<Map<String, String>> selectSellSumList(Map<String, String> paramMap) {
		return ar02Mapper.selectSellSumList(paramMap);
	}
}