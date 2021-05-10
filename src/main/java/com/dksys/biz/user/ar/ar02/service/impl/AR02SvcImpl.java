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
	public void insertSalesDivision(List<Map<String, String>> paramList) {
		Map<String, String> originSales = ar02Mapper.selectSellInfo(paramList.get(0));
		// 거래일자 하이픈 제거
		originSales.put("TRST_DT", originSales.get("trstDt").toString().replaceAll("-", ""));
		
		for(int i=0;i<paramList.size();i++) {
			Map<String, String> salesMap = paramList.get(i);
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.putAll(originSales);
			// 거래처
			paramMap.put("clntCd", salesMap.get("clntCd"));
			paramMap.put("clntNm", salesMap.get("clntNm"));
			// 금액
			paramMap.put("realTrstAmt", salesMap.get("realTrstAmt"));
			// 청구금액
			paramMap.put("bilgAmt", salesMap.get("bilgAmt"));
			// 부가세
			paramMap.put("bilgVatAmt", salesMap.get("bilgVatAmt"));
			// 수량
			paramMap.put("realTrstQty", salesMap.get("realTrstQty"));
			// 중량
			paramMap.put("realTrstWt", salesMap.get("realTrstWt"));
			// 청구수량
			paramMap.put("bilgQty", salesMap.get("bilgQty"));
			// 청구중량
			paramMap.put("bilgWt", salesMap.get("bilgWt"));
			// 할인금액
			paramMap.put("trstDcAmt", salesMap.get("trstDcAmt"));
			// 사용자 아이디
			paramMap.put("userId", salesMap.get("userId"));
			// 프로그램 아이디: 분할 매출 생성시 기존데이터와 동일하게 유지
			paramMap.put("pgmId", originSales.get("creatPgm"));
			if(i == 0) {
			// 원본 update
				// 거래처가 변경되었을경우
				if(!originSales.get("clntCd").toString().equals(salesMap.get("clntCd"))) {
					// 원본계산서번호 제거
					paramMap.put("orgnTaxBilgNo", "");
					// 세금계산서 수정사유 제거
					paramMap.put("rffAea", "");
				}
				ar02Mapper.updatePchsSell(paramMap);
			} else {
			// 신규 insert
				// 원본계산서번호 제거
				paramMap.put("orgnTaxBilgNo", "");
				// 세금계산서 수정사유 제거
				paramMap.put("rffAea", "");
				// insert
 				ar02Mapper.insertPchsSell(paramMap);
			}
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
	
	@Override
	public boolean checkSellClose(Map<String, String> paramMap) {
		String trstDt = paramMap.get("dlvrDttm").replace("-", "");
		paramMap.put("closeYm", trstDt.substring(0,6));
		Map<String, String> sd07result = sd07Mapper.selectClose(paramMap);
		if(sd07result != null) {
			int today = Integer.parseInt(DateUtil.getCurrentYyyymmdd());
			int closeDay = Integer.parseInt(sd07result.get("sellCloseDttm").replace("-", ""));
			if("Y".equals(sd07result.get("sellCloseYn")) && today > closeDay) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean checkPchsClose(Map<String, String> paramMap) {
		String trstDt = paramMap.get("dlvrDttm").replace("-", "");
		paramMap.put("closeYm", trstDt.substring(0,6));
		Map<String, String> sd07result = sd07Mapper.selectClose(paramMap);
		if(sd07result != null) {
			int today = Integer.parseInt(DateUtil.getCurrentYyyymmdd());
			int closeDay = Integer.parseInt(sd07result.get("sellCloseDttm").replace("-", ""));
			if("Y".equals(sd07result.get("sellCloseYn")) && today > closeDay) {
				return true;
			}
		}
		return false;
	}
}