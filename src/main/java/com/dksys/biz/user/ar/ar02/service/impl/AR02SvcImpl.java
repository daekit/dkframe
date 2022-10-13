package com.dksys.biz.user.ar.ar02.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.bm.bm01.mapper.BM01Mapper;
import com.dksys.biz.user.ar.ar02.mapper.AR02Mapper;
import com.dksys.biz.user.ar.ar02.service.AR02Svc;
import com.dksys.biz.user.sd.sd07.mapper.SD07Mapper;
import com.dksys.biz.user.sm.sm01.mapper.SM01Mapper;
import com.dksys.biz.util.DateUtil;
import com.dksys.biz.util.ExceptionThrower;
import com.dksys.biz.util.ObjectUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class AR02SvcImpl implements AR02Svc {

	private Logger logger = LoggerFactory.getLogger(AR02SvcImpl.class);
	
	@Autowired
    AR02Mapper ar02Mapper;
	
	@Autowired
	SD07Mapper sd07Mapper;
	
	@Autowired
    SM01Mapper sm01Mapper;
	
	@Autowired
    BM01Mapper bm01Mapper;
	
	@Autowired
    AR02Svc ar02Svc;
	
    @Autowired
    ExceptionThrower thrower;
	
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
		
		String dtTo = paramMap.get("dtTo");
		String dtFrom = paramMap.get("dtFrom");
		String dtKind = paramMap.get("dtKind");
		
		//매출리스트에서 일자 선택을 매출일자로 했을때
		if("sellDate".equals(dtKind)) {
			paramMap.put("reqDtTo", "");
			paramMap.put("reqDtFrom", "");
			
			paramMap.put("reqDtTo", dtTo);
			paramMap.put("reqDtFrom", dtFrom);
		}
		
		//매출리스트에서 일자 선택을 운송일자로 했을때
		if("dlvrDate".equals(dtKind)) {
			paramMap.put("realDlvrDtTo", "");
			paramMap.put("realDlvrDtFrom", "");
			
			paramMap.put("realDlvrDtTo", dtTo);
			paramMap.put("realDlvrDtFrom", dtFrom);
		}
		
		return ar02Mapper.selectSellCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectSellList(Map<String, String> paramMap) {
		
		String dtTo = paramMap.get("dtTo");
		String dtFrom = paramMap.get("dtFrom");
		String dtKind = paramMap.get("dtKind");
		
		//매출리스트에서 일자 선택을 매출일자로 했을때
		if("sellDate".equals(dtKind)) {
			paramMap.put("reqDtTo", "");
			paramMap.put("reqDtFrom", "");
			
			paramMap.put("reqDtTo", dtTo);
			paramMap.put("reqDtFrom", dtFrom);
		}
		
		//매출리스트에서 일자 선택을 운송일자로 했을때
		if("dlvrDate".equals(dtKind)) {
			paramMap.put("realDlvrDtTo", "");
			paramMap.put("realDlvrDtFrom", "");
			
			paramMap.put("realDlvrDtTo", dtTo);
			paramMap.put("realDlvrDtFrom", dtFrom);
		}
		
		return ar02Mapper.selectSellList(paramMap);
	}

	@Override
	public int selectSellPchSumCount(Map<String, String> paramMap) {
		return ar02Mapper.selectSellPchSumCount(paramMap);
	}
	
	@Override
	public List<Map<String, String>> selectSellPchSumList(Map<String, String> paramMap) {
		return ar02Mapper.selectSellPchSumList(paramMap);
	}
	
	@Override
	@SuppressWarnings("all")
	// 매입리스트, 매출리스트 정산
	public void updatePchsSell(Map<String, Object> paramMap) throws Exception{
		// 매입 매출 구분
		String selpchCd = paramMap.get("selpchCd").toString();
		// 정산후 변경되어서 넘어온 부가세 포함 총 금액
		long totAmt = 0;
		// 거래일자
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		String maxTrstDt = null;
		String trstDt = null;
		
		List<Map<String, String>> detailList = (List<Map<String, String>>) paramMap.get("detailArr");
		for (Map<String, String> detailMap : detailList) {
			// for문을 순회하며 개별 마감 체크
			Map<String, String> closeChkMap = new HashMap<String, String>();
			closeChkMap.put("trstDt", detailMap.get("trstDt").toString());
			closeChkMap.put("coCd", detailMap.get("coCd").toString());
		
	        if("SELPCH1".equals(selpchCd)) {
				if(!ar02Svc.checkPchsClose(closeChkMap)) {
		    		thrower.throwCommonException("pchsClose");
				}
	        }
	        
	        if("SELPCH2".equals(selpchCd)) {
	        	if(!ar02Svc.checkSellClose(closeChkMap)) {
            		thrower.throwCommonException("sellClose");
        		}
	        }
	        
	        // 매입/매출 업데이트
			detailMap.put("userId", paramMap.get("userId").toString());
			detailMap.put("pgmId", paramMap.get("pgmId").toString());
			
			///// 매출정산 UPDATE 
			/*
			Map<String, String> deUpMap = new HashMap<String, String>();
			deUpMap.put("trstCertiNo", MapUtils.getString(paramMap, "trstCertiNo"));
			deUpMap.put("clntCd", MapUtils.getString(paramMap, "clntCd"));
			deUpMap.put("clntNm", MapUtils.getString(paramMap, "clntNm"));
			deUpMap.put("realTrstQty", MapUtils.getString(paramMap, "realTrstQty"));
			deUpMap.put("realTrstWt", MapUtils.getString(paramMap, "realTrstWt"));
			deUpMap.put("realTrstAmt", MapUtils.getString(paramMap, "realTrstAmt"));
			deUpMap.put("bilgQty", MapUtils.getString(paramMap, "bilgQty"));
			deUpMap.put("bilgWt", MapUtils.getString(paramMap, "bilgWt"));
			deUpMap.put("bilgUpr", MapUtils.getString(paramMap, "bilgUpr"));
			deUpMap.put("bilgAmt", MapUtils.getString(paramMap, "bilgAmt"));
			deUpMap.put("bilgVatAmt", MapUtils.getString(paramMap, "bilgVatAmt"));
			deUpMap.put("trstDcAmt", MapUtils.getString(paramMap, "trstDcAmt"));
			deUpMap.put("etcAmt", MapUtils.getString(paramMap, "etcAmt"));
			deUpMap.put("trspRmk", MapUtils.getString(paramMap, "trspRmk"));
			deUpMap.put("shipVehNo", MapUtils.getString(paramMap, "shipVehNo"));
			deUpMap.put("transAmt", MapUtils.getString(paramMap, "transAmt"));
			deUpMap.put("lossRate", MapUtils.getString(paramMap, "lossRate"));
			deUpMap.put("userId", MapUtils.getString(paramMap, "userId"));
			deUpMap.put("updatePgmId", MapUtils.getString(paramMap, "updatePgmId"));
			deUpMap.put("pgmId", MapUtils.getString(paramMap, "pgmId"));
			*/
			
			
			
			
			List<Map<String, String>> countSellList = ar02Mapper.countSellList(detailMap);
			
			if(countSellList.size() != 0) {
				for(int i=0; i<countSellList.size(); i++) {
					detailMap.put("etrdpsSeqFind", MapUtils.getString(countSellList.get(i), "ETRDPS_SEQ"));
					int countSell = ar02Mapper.countSellFind(detailMap);
					detailMap.put("countSell", String.valueOf(countSell));
					ar02Mapper.updatePchsSell(detailMap);
					// ar02Mapper.deleteSell(deUpMap);
				}
			}
			
			// 기존에는 매핑된 리스트에 따라서 for문을 돌게되어있었지만, 매핑되지 않은 매입과 매출리스트 건에 대한 분할 및 정산이 이루어질 수 있어서 수정
			ar02Mapper.updatePchsSellPart2(detailMap);
			
			ar02Mapper.deleteSell05D(detailMap);
			
			ar02Mapper.callSaleMatch(detailMap);
			
			
			
			
			
			// for문을 순회하며 가장 큰 trstDt 계산
			if(trstDt == null) {
				maxTrstDt = detailMap.get("trstDt").toString();
			}else{
				Date tempDate1 = dateFormat.parse(maxTrstDt);
				Date tempDate2 = dateFormat.parse(detailMap.get("trstDt").toString());
				if(tempDate2.compareTo(tempDate1) > 0) {
					maxTrstDt = dateFormat.format(tempDate2);
				}
			}
			
			// 비교를 위한 현재 인덱스의 거래일자 set
			trstDt = detailMap.get("trstDt").toString();
			// 총금액 계산
			totAmt += Long.parseLong(String.valueOf(detailMap.get("totAmt")));
		}
		
		// 여신맵 초기화
		Map<String, Object> loanMap = new HashMap<String, Object>();
		loanMap.put("coCd", paramMap.get("coCd"));
		loanMap.put("clntCd", paramMap.get("clntCd"));
		loanMap.put("prdtGrp", detailList.get(0).get("prdtGrp"));
		loanMap.put("trstDt", maxTrstDt.replace("-", ""));
		
		if("SELPCH2".equals(selpchCd) || "SELPCH4".equals(selpchCd)){
			// 부가세를 포함한 원본 총 금액
			long orgTotAmt = Long.parseLong(paramMap.get("orgTotAmt").toString());
			// 차이금액(원본 총 금액 - 정산 총 금액)
			long exceedAmt = totAmt - orgTotAmt;
			if(exceedAmt > 0) {
				loanMap.put("totAmt", exceedAmt);
				
				long diffLoan = ar02Svc.checkLoan(loanMap);
				if(diffLoan < 0) {
		        	thrower.throwCreditLoanException("", diffLoan);
		        }else {
		        	// 여신 차감후 음수 return시 롤백 
		        	long loanPrcsResult = ar02Svc.deductLoan(loanMap);
		        	if(loanPrcsResult < 0) {
		    			throw new Exception();
		    		}
		        }
			}else if(exceedAmt < 0){
				loanMap.put("totAmt", exceedAmt);
				// 여신 누적후 음수 return시 롤백 
	        	long loanPrcsResult = ar02Svc.depositLoan(loanMap);
	        	if(loanPrcsResult < 0) {
	    			throw new Exception();
	    		}
			}
		}
	}
	
	@Override
	@SuppressWarnings("all")
	// 매입리스트, 매출리스트 정산
	public void updatePchsSellPart(Map<String, String> paramMap) throws Exception{
		// 매입 매출 구분
		String selpchCd = paramMap.get("selpchCd").toString();
		//원본
		Map<String, String> pchsSellInfo = ar02Mapper.selectSellInfo(paramMap);

		// 매입/매출 업데이트
		ar02Mapper.updatePchsSellPart(paramMap);
		
		String oldPrjcrCd = pchsSellInfo.get("prjctCd");

		
		// 프로젝트가 바뀌면 기존 프로젝트의 재고는 차감하고, 신규 프로젝트는 추가한다.
		if(!"oldPrjcrCd".equals(paramMap.get("prjctCd"))) {
			// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
			if("OWNER1".equals(paramMap.get("ownerCd").toString())) {		
				paramMap.put("clntCd",  ar02Mapper.selectOwner1ClntCd(paramMap));		
			}
			// 신규 추가
			Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);

			BigDecimal stockQty = ObjectUtil.defaultBigDecimalNotNull(paramMap.get("realTrstQty"));
			BigDecimal stockWt  = ObjectUtil.defaultBigDecimalNotNull(paramMap.get("realTrstWt"));
			
			String stockChgCd = "STOCKCHG09";
			if("SELPCH2".equals(paramMap.get("selpchCd"))) 
			{
				stockChgCd = "STOCKCHG09";
			}
			else {
				stockChgCd = "STOCKCHG08";
			}
			if(stockInfo == null) {
				paramMap.put("pchsUpr", paramMap.get("realTrstUpr"));
				paramMap.put("sellUpr", paramMap.get("realTrstUpr"));
				paramMap.put("stockUpr", paramMap.get("realTrstUpr"));
				paramMap.put("stdUpr", paramMap.get("realTrstUpr"));
				stockQty = "SELPCH1".equals(paramMap.get("selpchCd")) ? stockQty : stockQty.multiply(new BigDecimal("-1"));
				stockWt  = "SELPCH1".equals(paramMap.get("selpchCd")) ? stockWt : stockWt.multiply(new BigDecimal("-1"));
				paramMap.put("stockQty", String.valueOf(stockQty));
				paramMap.put("stockWt", String.valueOf(stockWt));
				
//				paramMap.put("stockQty", paramMap.get("realTrstQty"));
//				paramMap.put("stockWt", paramMap.get("realTrstWt"));
			
			} else {
				if("SELPCH2".equals(paramMap.get("selpchCd"))) 
				{
					stockQty = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockQty")).subtract(stockQty);
					stockWt  = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockWt")).subtract(stockWt);
				} else 
				{
					stockQty = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockQty")).add(stockQty);
					stockWt  = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockWt")).add(stockWt);
				}
				paramMap.put("stockQty", String.valueOf(stockQty));
				paramMap.put("stockWt",  String.valueOf(stockWt));
				paramMap.put("stockUpr", stockInfo.get("stockUpr"));
				paramMap.put("stdUpr", stockInfo.get("stdUpr"));
				paramMap.put("pchsUpr", stockInfo.get("pchsUpr"));
				paramMap.put("sellUpr", paramMap.get("bilgUpr"));
				paramMap.put("stockChgCd", stockChgCd);
			}
			sm01Mapper.updateStockSell(paramMap);						
			// 기존 제거
			paramMap.put("prjctCd",oldPrjcrCd);
			Map<String, String> stockInfoOld = sm01Mapper.selectStockInfo(paramMap);
			if(stockInfoOld != null) {
				stockQty = ObjectUtil.defaultBigDecimalNotNull(paramMap.get("realTrstQty"));
				stockWt  = ObjectUtil.defaultBigDecimalNotNull(paramMap.get("realTrstWt"));
				if("SELPCH2".equals(paramMap.get("selpchCd"))) 
				{
					stockChgCd = "STOCKCHG09";
					stockQty = ObjectUtil.defaultBigDecimalNotNull(stockInfoOld.get("stockQty")).add(stockQty);
					stockWt  = ObjectUtil.defaultBigDecimalNotNull(stockInfoOld.get("stockWt")).add(stockWt);
				} else 
				{
					stockChgCd = "STOCKCHG08";
					stockQty = ObjectUtil.defaultBigDecimalNotNull(stockInfoOld.get("stockQty")).subtract(stockQty);
					stockWt  = ObjectUtil.defaultBigDecimalNotNull(stockInfoOld.get("stockWt")).subtract(stockWt);
				}
				paramMap.put("stockQty", String.valueOf(stockQty));
				paramMap.put("stockWt",  String.valueOf(stockWt));
				paramMap.put("stockUpr", stockInfoOld.get("stockUpr"));
				paramMap.put("stdUpr", stockInfoOld.get("stdUpr"));
				paramMap.put("pchsUpr", stockInfoOld.get("pchsUpr"));
				paramMap.put("sellUpr", paramMap.get("bilgUpr"));
				paramMap.put("stockChgCd", stockChgCd);
				sm01Mapper.updateStockSell(paramMap);						
			}
			
		}
	}
	@Override
	// 매입 / 매출 삭제
	public void deleteSell(Map<String, String> paramMap) throws Exception{
		Map<String, String> sellInfoMap = ar02Mapper.selectSellInfo(paramMap);
		paramMap.putAll(sellInfoMap);
		
		long totAmt = 0;
    	long bilgAmt = Long.parseLong(paramMap.get("bilgAmt"));
    	int bilgVatPer = ar02Mapper.selectBilgVatPer(paramMap);
    	long bilgVatAmt = (long) Math.floor(bilgAmt * bilgVatPer / 100);
    	totAmt = bilgAmt + bilgVatAmt;
    	
    	// 여신맵 초기화
    	Map<String, Object> loanMap = new HashMap<String, Object>();
		loanMap.put("coCd", paramMap.get("coCd"));
		loanMap.put("clntCd", paramMap.get("clntCd"));
		loanMap.put("prdtGrp", paramMap.get("prdtGrp"));
		loanMap.put("trstDt", paramMap.get("trstDt"));
		
		if("SELPCH1".equals(paramMap.get("selpchCd"))) {
        // 매입
			if(!ar02Svc.checkPchsClose(paramMap)) {
			// 마감 체크
				thrower.throwCommonException("pchsClose");
			}
        }else if("SELPCH2".equals(paramMap.get("selpchCd"))){
        // 매출
        	if(!ar02Svc.checkSellClose(paramMap)) {
    		// 마감 체크
        		thrower.throwCommonException("sellClose");
			}
        	if(bilgAmt < 0) {
    		// 매출리스트에서 반품건 삭제
        		// 여신 체크
        		loanMap.put("totAmt", -1 * totAmt);
        		long diffLoan = ar02Svc.checkLoan(loanMap);
        		if(diffLoan < 0) {
                	thrower.throwCreditLoanException("", diffLoan);
                }
        	}
        }
        
		// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
		if("OWNER1".equals(paramMap.get("ownerCd").toString())) {		
			paramMap.put("clntCd",  ar02Mapper.selectOwner1ClntCd(paramMap));		
		}
		
		Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
		if(stockInfo != null) {
			BigDecimal stockQty = new BigDecimal("0");
			BigDecimal stockWt = new BigDecimal("0");
			String stockChgCd = "STOCKCHG09";
			if("SELPCH2".equals(paramMap.get("selpchCd"))) 
			{
				stockChgCd = "STOCKCHG09";
				stockQty = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockQty")).add(ObjectUtil.defaultBigDecimalNotNull(paramMap.get("realTrstQty")));
				stockWt  = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockWt")).add(ObjectUtil.defaultBigDecimalNotNull(paramMap.get("realTrstWt")));
			} else 
			{
				stockChgCd = "STOCKCHG08";
				stockQty = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockQty")).subtract(ObjectUtil.defaultBigDecimalNotNull(paramMap.get("realTrstQty")));
				stockWt  = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockWt")).subtract(ObjectUtil.defaultBigDecimalNotNull(paramMap.get("realTrstWt")));
			}
			
			paramMap.put("stockQty", String.valueOf(stockQty));
			paramMap.put("stockWt",  String.valueOf(stockWt));
			paramMap.put("stockUpr", stockInfo.get("stockUpr"));
			paramMap.put("stdUpr", stockInfo.get("stdUpr"));
			paramMap.put("pchsUpr", stockInfo.get("pchsUpr"));
			paramMap.put("sellUpr", paramMap.get("bilgUpr"));
			paramMap.put("stockChgCd", stockChgCd);
			paramMap.put("userId", paramMap.get("userId"));
			paramMap.put("pgmId", paramMap.get("pgmId"));
			
			sm01Mapper.updateStockSell(paramMap);						
		}
		
		
		List<Map<String, String>> countSellList = ar02Mapper.countSellList(paramMap);
		
		if(countSellList.size() != 0) {
			for(int i=0; i<countSellList.size(); i++) {
				paramMap.put("etrdpsSeqFind", MapUtils.getString(countSellList.get(i), "ETRDPS_SEQ"));
				int countSell = ar02Mapper.countSellFind(paramMap);
				paramMap.put("countSell", String.valueOf(countSell));
				ar02Mapper.deleteSell(paramMap);
			}
		}
		
		ar02Mapper.deleteSellReal(paramMap);
		
		// 여신 체크후 차감 / 여신 증가
		long loanPrcsResult = 0;
		if("SELPCH2".equals(paramMap.get("selpchCd"))){
        // 매출
        	if(bilgAmt < 0) {
    		// 매출리스트에서 반품건 삭제
        		// 여신 체크
        		loanMap.put("totAmt", -1 * totAmt);
        		long diffLoan = ar02Svc.checkLoan(loanMap);
        		if(diffLoan < 0) {
                	thrower.throwCreditLoanException("", diffLoan);
                }else {
                	// 여신 차감
                	loanPrcsResult = ar02Svc.deductLoan(loanMap);
                }
        	}else if(bilgAmt > 0){
        	// 매출리스트에서 매출건 삭제
        		loanMap.put("totAmt", totAmt);
        		// 여신 누적
        		loanPrcsResult = ar02Svc.depositLoan(loanMap);
        	}
        }
		
		// 여신 차감 / 여신 증가후 음수 return시 롤백
		if(loanPrcsResult < 0) {
			throw new Exception();
		}
		
		ar02Mapper.callSaleMatch(sellInfoMap);
		
	}

	@Override
	// 매입 / 반입 / 매출 / 반품
	public void insertPchsSell(Map<String, String> paramMap) throws Exception{
		long totAmt = 0;
    	long bilgAmt = Long.parseLong(paramMap.get("bilgAmt"));
    	int bilgVatPer = ar02Mapper.selectBilgVatPer(paramMap);
    	long bilgVatAmt = (long) Math.floor(bilgAmt * bilgVatPer / 100);
    	totAmt = bilgAmt + bilgVatAmt;
    	
    	// 여신맵 초기화
    	Map<String, Object> loanMap = new HashMap<String, Object>();
		loanMap.put("coCd", paramMap.get("coCd"));
		loanMap.put("clntCd", paramMap.get("clntCd"));
		loanMap.put("prdtGrp", paramMap.get("prdtGrp"));
		loanMap.put("trstDt", paramMap.get("trstDt"));
    	
        if("SELPCH1".equals(paramMap.get("selpchCd"))) {
        // 매입 / 반입
			if(!ar02Svc.checkPchsClose(paramMap)) {
			// 마감 체크
				thrower.throwCommonException("pchsClose");
			}
        }else if("SELPCH2".equals(paramMap.get("selpchCd"))){
        // 매출 / 반품
        	if(!ar02Svc.checkSellClose(paramMap)) {
    		// 마감 체크
        		thrower.throwCommonException("sellClose");
			}
        	if(bilgAmt > 0) {
    		// 매출 / 반품의 반품
        		loanMap.put("totAmt", totAmt);
        		// 여신 체크
        		long diffLoan = ar02Svc.checkLoan(loanMap);
        		if(diffLoan < 0) {
                	thrower.throwCreditLoanException("", diffLoan);
                }
        	}
        }
        
		BigDecimal stockQty = ObjectUtil.defaultBigDecimalNotNull(paramMap.get("realTrstQty"));
		BigDecimal stockWt  = ObjectUtil.defaultBigDecimalNotNull(paramMap.get("realTrstWt"));
		// 파라미터로부터 전달받은 거래처 backup
		String clntCd = paramMap.get("clntCd");
		
		if(paramMap.containsKey("prdtStockCd") && "Y".equals(paramMap.get("prdtStockCd").toString())) 
		{
			// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
			if("OWNER1".equals(paramMap.get("ownerCd").toString())) {		
				paramMap.put("clntCd",  ar02Mapper.selectOwner1ClntCd(paramMap));		
			}
		}
		Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
		
		if(stockInfo == null) {
			paramMap.put("pchsUpr", paramMap.get("realTrstUpr"));
			paramMap.put("sellUpr", paramMap.get("realTrstUpr"));
			paramMap.put("stockUpr", paramMap.get("realTrstUpr"));
			paramMap.put("stdUpr", paramMap.get("realTrstUpr"));
			stockQty = "SELPCH1".equals(paramMap.get("selpchCd")) ? stockQty : stockQty.multiply(new BigDecimal("-1"));
			stockWt  = "SELPCH1".equals(paramMap.get("selpchCd")) ? stockWt : stockWt.multiply(new BigDecimal("-1"));
			paramMap.put("stockQty", String.valueOf(stockQty));
			paramMap.put("stockWt", String.valueOf(stockWt));
		} else {
			//매출일때
			if("SELPCH2".equals(paramMap.get("selpchCd"))) 
			{
				paramMap.put("sellUpr", paramMap.get("realTrstUpr"));
				paramMap.put("pchsUpr", stockInfo.get("pchsUpr"));
				stockQty = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockQty")).subtract(stockQty);
				stockWt  = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockWt")).subtract(stockWt);
			}
			//매입일때
			else 
			{
				paramMap.put("pchsUpr", paramMap.get("realTrstUpr"));
				paramMap.put("sellUpr", stockInfo.get("sellUpr"));
				stockQty = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockQty")).add(stockQty);
				stockWt  = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockWt")).add(stockWt);
			}
			paramMap.put("stockUpr", stockInfo.get("stockUpr"));
			paramMap.put("stdUpr", stockInfo.get("stdUpr"));
			paramMap.put("stockQty", String.valueOf(stockQty));
			paramMap.put("stockWt",  String.valueOf(stockWt));
		}
		
		if(paramMap.containsKey("prdtStockCd") && "Y".equals(paramMap.get("prdtStockCd").toString())) 
		{
			// 재고정보 update
			sm01Mapper.updateStockSell(paramMap);
		}
		
		// 부가세 put
        paramMap.put("bilgVatAmt", String.valueOf(bilgVatAmt));
        // 납품일자 put
        paramMap.put("dlvrDttm", paramMap.get("trstDt"));
		// 거래처 원복
		paramMap.put("clntCd", clntCd);
		// insert
		ar02Mapper.insertPchsSell(paramMap);
		
		// 여신 체크후 차감 / 여신 증가
		long loanPrcsResult = 0;
		if("SELPCH2".equals(paramMap.get("selpchCd"))){
        // 매출 / 반품
        	if(bilgAmt > 0) {
    		// 매출 / 반품의 반품
        		loanMap.put("totAmt", totAmt);
        		// 여신 체크
        		long diffLoan = ar02Svc.checkLoan(loanMap);
        		if(diffLoan < 0) {
                	thrower.throwCreditLoanException("", diffLoan);
                }else {
                	// 여신 차감
                	loanPrcsResult = ar02Svc.deductLoan(loanMap);
                }
        	}else if(bilgAmt < 0){
        	// 반품
        		loanMap.put("totAmt", -1 * totAmt);
        		// 여신 누적
        		loanPrcsResult = ar02Svc.depositLoan(loanMap);
        	}
        }
		
		// 여신 차감 / 여신 증가후 음수 return시 롤백
		if(loanPrcsResult < 0) {
			throw new Exception();
		}
		
		ar02Mapper.callSaleMatch(paramMap);
	}
	
	@Override
	public void insertSalesDivision(List<Map<String, String>> paramList) throws Exception{
		
		Map<String, String> baseMap = paramList.get(0);
		// 마감 체크
		if(!ar02Svc.checkSellClose(baseMap)) {
    		thrower.throwCommonException("sellClose");
		}
		
		/* 여신체크 start */
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
		/* 여신체크 end */
		
		for(Map<String, String> paramMap : paramList) {
			
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
			
			// 기존에는 매핑된 리스트에 따라서 for문을 돌게되어있었지만, 매핑되지 않은 매입과 매출리스트 건에 대한 분할 및 정산이 이루어질 수 있어서 수정
			ar02Mapper.updatePchsSellPart2(paramMap);
			
			ar02Mapper.deleteSell05D(paramMap);
			
			// paramList 순회하며 넘어온값 그대로 update
			// ar02Mapper.updatePchsSell(paramMap);
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
		
		Map<String, String> matchMap = new HashMap<String, String>();
		matchMap.put("coCd", paramList.get(0).get("coCd"));
		matchMap.put("clntCd", paramList.get(0).get("clntCd"));
		matchMap.put("prdtGrp", paramList.get(0).get("prdtGrp"));
		
		ar02Mapper.callSaleMatch(matchMap);
		
		matchMap.put("clntCd", paramList.get(0).get("divClntCd"));
		
		ar02Mapper.callSaleMatch(matchMap);
		
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

	// 여신체크
	@Override
	public long checkLoan(Map<String, Object> paramMap) {
		Map<String, Object> loanMap = new HashMap<String, Object>();
		loanMap.put("loanCd", 'C');
		loanMap.put("coCd", paramMap.get("coCd"));
		loanMap.put("clntCd", paramMap.get("clntCd"));
		loanMap.put("iPrdtGrp", paramMap.get("prdtGrp"));
		loanMap.put("iTrDt", paramMap.get("trstDt").toString().replace("-", ""));
		loanMap.put("amt", paramMap.get("totAmt"));
		long creditLoan  = ar02Mapper.callCreditLoan2(loanMap);
		long diffLoan = creditLoan - (Long)paramMap.get("totAmt");
		
		/* 여신초과 확인을 위한 로그 시작 */
		long pldgAmt = ar02Mapper.checkPldgAmt(loanMap);
		logger.info("=====> pldgAmt : " + pldgAmt);
		long creditNonRecvAmt  = ar02Mapper.callCreditNonRecvAmt(loanMap);
		logger.info("=====> creditNonRecvAmt : " + creditNonRecvAmt);
		long creditNonPayAmt  = ar02Mapper.callCreditNonPayAmt(loanMap);
		logger.info("=====> creditNonPayAmt : " + creditNonPayAmt);
		long creditUnsetlBilAmt  = ar02Mapper.callCreditUnsetlBilAmt(loanMap);
		logger.info("=====> creditUnsetlBilAmt : " + creditUnsetlBilAmt);
		
		logger.info("=====> creditLoan : " + creditLoan);
		logger.info("=====> totAmt : " + (Long)paramMap.get("totAmt"));
		logger.info("=====> diffLoan : " + diffLoan);
		/* 여신초과 확인을 위한 로그 끝 */
		
		return diffLoan;
	}
	
	// 여신차감
	@Override
	public long deductLoan(Map<String, Object> paramMap) {
		Map<String, Object> loanMap = new HashMap<String, Object>();
		loanMap.put("loanCd", 'P');
		loanMap.put("coCd", paramMap.get("coCd"));
		loanMap.put("clntCd", paramMap.get("clntCd"));
		loanMap.put("iPrdtGrp", paramMap.get("prdtGrp"));
		loanMap.put("iTrDt", paramMap.get("trstDt").toString().replace("-", ""));
		loanMap.put("amt", paramMap.get("totAmt"));
		return ar02Mapper.callCreditLoan2(loanMap);
	}
	
	// 여신누적
	@Override
	public long depositLoan(Map<String, Object> paramMap) {
		Map<String, Object> loanMap = new HashMap<String, Object>();
		loanMap.put("loanCd", 'M');
		loanMap.put("coCd", paramMap.get("coCd"));
		loanMap.put("clntCd", paramMap.get("clntCd"));
		loanMap.put("iPrdtGrp", paramMap.get("prdtGrp"));
		loanMap.put("iTrDt", paramMap.get("trstDt").toString().replace("-", ""));
		loanMap.put("amt", paramMap.get("totAmt"));
		return ar02Mapper.callCreditLoan2(loanMap);
	}
	
	@Override
	public List<Map<String, String>> selectSellSumList(Map<String, String> paramMap) {
		return ar02Mapper.selectSellSumList(paramMap);
	}
	
	@Override
	public boolean checkSellClose(Map<String, String> paramMap) {
		String trstDt = null;
		if(paramMap.containsKey("dlvrDttm")) {
			trstDt = paramMap.get("dlvrDttm").replace("-", "");
		}else if(paramMap.containsKey("trstDt")) {
			trstDt = paramMap.get("trstDt").replace("-", "");
		}
		paramMap.put("closeYm", trstDt.substring(0,6));
		Map<String, String> sd07result = sd07Mapper.selectClose(paramMap);
		Map<String, String> sd07resultMax = sd07Mapper.selectMaxCloseDay(paramMap);
		if(sd07result != null) {
			int today = Integer.parseInt(DateUtil.getCurrentYyyymmdd());
			int closeDay = Integer.parseInt(sd07result.get("sellCloseDttm").replace("-", ""));
			if("Y".equals(sd07result.get("sellCloseYn")) && closeDay < today) {
				return false;
			}
		}
		if(sd07resultMax != null) {
			if(sd07resultMax.containsKey("maxSellCloseDay")){
				int maxSellCloseDay = Integer.parseInt(sd07resultMax.get("maxSellCloseDay").replace("-", ""));
				if(Integer.parseInt(trstDt) < maxSellCloseDay) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean checkPchsClose(Map<String, String> paramMap) {
		/*
		 * 1. 기준월에 대한 수정 가능 여부 확인 
		 * 2. 수정하고자 하는 자료의 일자가 마감이 된 MAX월 이전인지 확인 
		 */
		String trstDt = null;
		if(paramMap.containsKey("dlvrDttm")) {
			trstDt = paramMap.get("dlvrDttm").replace("-", "");
		}else if(paramMap.containsKey("trstDt")) {
			trstDt = paramMap.get("trstDt").replace("-", "");
		}
		
		paramMap.put("closeYm", trstDt.substring(0,6));
		Map<String, String> sd07result = sd07Mapper.selectClose(paramMap);
		Map<String, String> sd07resultMax = sd07Mapper.selectMaxCloseDay(paramMap);
		if(sd07result != null) {
			int today       = Integer.parseInt(DateUtil.getCurrentYyyymmdd());
			int closeDay    = Integer.parseInt(sd07result.get("pchsCloseDttm").replace("-", ""));
			if("Y".equals(sd07result.get("pchsCloseYn")) && closeDay < today) {
				return false;
			}
		}
		if(sd07resultMax != null) {
			if(sd07resultMax.containsKey("maxPchsCloseDay")){
				int maxPchsCloseDay = Integer.parseInt(sd07resultMax.get("maxPchsCloseDay").replace("-", ""));
				if(Integer.parseInt(trstDt) < maxPchsCloseDay) {
					return false;
				}
			}
		}
		return true;
	}
	
	

	@Override
	// 매입 / 반입 / 매출 / 반품
	public void insertDeletePchsSell(Map<String, String> paramMap) throws Exception{
		Map<String, String> paramMapBackUp = new HashMap<>();
		paramMapBackUp.putAll(paramMap);
		
		Map<String, String> sellInfoMap = ar02Mapper.selectSellInfo(paramMap);
		paramMap.putAll(sellInfoMap);
		
		if("Y".equals(sellInfoMap.get("bilgYn"))) {
			thrower.throwCommonException("fail");
		}else {

			long totAmt = 0;
	    	long bilgAmt = Long.parseLong(paramMap.get("bilgAmt"));
	    	int bilgVatPer = ar02Mapper.selectBilgVatPer(paramMap);
	    	long bilgVatAmt = (long) Math.floor(bilgAmt * bilgVatPer / 100);
	    	totAmt = bilgAmt + bilgVatAmt;
	    	
	    	// 여신맵 초기화
	    	Map<String, Object> loanMap = new HashMap<String, Object>();
			loanMap.put("coCd", paramMap.get("coCd"));
			loanMap.put("clntCd", paramMap.get("clntCd"));
			loanMap.put("prdtGrp", paramMap.get("prdtGrp"));
			loanMap.put("trstDt", paramMap.get("trstDt"));
			
			if("SELPCH1".equals(paramMap.get("selpchCd"))) {
	        // 매입
				if(!ar02Svc.checkPchsClose(paramMap)) {
				// 마감 체크
					thrower.throwCommonException("pchsClose");
				}
	        }else if("SELPCH2".equals(paramMap.get("selpchCd"))){
	        // 매출
	        	if(!ar02Svc.checkSellClose(paramMap)) {
	    		// 마감 체크
	        		thrower.throwCommonException("sellClose");
				}
	        	if(bilgAmt < 0) {
	    		// 매출리스트에서 반품건 삭제
	        		// 여신 체크
	        		loanMap.put("totAmt", -1 * totAmt);
	        		long diffLoan = ar02Svc.checkLoan(loanMap);
	        		if(diffLoan < 0) {
	                	thrower.throwCreditLoanException("", diffLoan);
	                }
	        	}
	        }
	        
			// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
			if("OWNER1".equals(paramMap.get("ownerCd").toString())) {		
				paramMap.put("clntCd",  ar02Mapper.selectOwner1ClntCd(paramMap));		
			}
			
			Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
			
			if(stockInfo != null) {
				BigDecimal stockQty = new BigDecimal("0");
				BigDecimal stockWt = new BigDecimal("0");
				String stockChgCd = "STOCKCHG09";
				if("SELPCH2".equals(paramMap.get("selpchCd"))) 
				{
					stockChgCd = "STOCKCHG09";
					stockQty = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockQty")).add(ObjectUtil.defaultBigDecimalNotNull(paramMap.get("realTrstQty")));
					stockWt  = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockWt")).add(ObjectUtil.defaultBigDecimalNotNull(paramMap.get("realTrstWt")));
				} else 
				{
					stockChgCd = "STOCKCHG08";
					stockQty = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockQty")).subtract(ObjectUtil.defaultBigDecimalNotNull(paramMap.get("realTrstQty")));
					stockWt  = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockWt")).subtract(ObjectUtil.defaultBigDecimalNotNull(paramMap.get("realTrstWt")));
				}
				paramMap.put("stockQty", String.valueOf(stockQty));
				paramMap.put("stockWt",  String.valueOf(stockWt));
				paramMap.put("stockUpr", stockInfo.get("stockUpr"));
				paramMap.put("stdUpr", stockInfo.get("stdUpr"));
				paramMap.put("pchsUpr", stockInfo.get("pchsUpr"));
				paramMap.put("sellUpr", paramMap.get("bilgUpr"));
				paramMap.put("stockChgCd", stockChgCd);
				paramMap.put("userId", paramMap.get("userId"));
				paramMap.put("pgmId", paramMap.get("pgmId"));
				sm01Mapper.updateStockSell(paramMap);						
			}
			
			
			List<Map<String, String>> countSellList = ar02Mapper.countSellList(paramMap);
			
			if(countSellList.size() != 0) {
				for(int i=0; i<countSellList.size(); i++) {
					paramMap.put("etrdpsSeqFind", MapUtils.getString(countSellList.get(i), "ETRDPS_SEQ"));
					int countSell = ar02Mapper.countSellFind(paramMap);
					paramMap.put("countSell", String.valueOf(countSell));
					ar02Mapper.deleteSell(paramMap);
				}
			}
			
			ar02Mapper.deleteSellReal(paramMap);
			/*
			int countSell = ar02Mapper.countSell(paramMap);
			paramMap.put("countSell", String.valueOf(countSell));
			
			ar02Mapper.deleteSell(paramMap);
			*/
			
			// 여신 체크후 차감 / 여신 증가
			long loanPrcsResult = 0;
			if("SELPCH2".equals(paramMap.get("selpchCd"))){
	        // 매출
	        	if(bilgAmt < 0) {
	    		// 매출리스트에서 반품건 삭제
	        		// 여신 체크
	        		loanMap.put("totAmt", -1 * totAmt);
	        		long diffLoan = ar02Svc.checkLoan(loanMap);
	        		if(diffLoan < 0) {
	                	thrower.throwCreditLoanException("", diffLoan);
	                }else {
	                	// 여신 차감
	                	loanPrcsResult = ar02Svc.deductLoan(loanMap);
	                }
	        	}else if(bilgAmt > 0){
	        	// 매출리스트에서 매출건 삭제
	        		loanMap.put("totAmt", totAmt);
	        		// 여신 누적
	        		loanPrcsResult = ar02Svc.depositLoan(loanMap);
	        	}
	        }
			
			// 여신 차감 / 여신 증가후 음수 return시 롤백
			if(loanPrcsResult < 0) {
				throw new Exception();
			}
			
			// 수정 로직 시작
			
			paramMap.putAll(paramMapBackUp);
			
			long totAmtBackUp = totAmt;
			totAmt = 0;
	    	bilgAmt = Long.parseLong(paramMap.get("bilgAmt"));
	    	bilgVatPer = ar02Mapper.selectBilgVatPer(paramMap);
	    	bilgVatAmt = (long) Math.floor(bilgAmt * bilgVatPer / 100);
	    	totAmt = bilgAmt + bilgVatAmt;
	    	
	    	// 여신맵 초기화
	    	loanMap = new HashMap<String, Object>();
			loanMap.put("coCd", paramMap.get("coCd"));
			loanMap.put("clntCd", paramMap.get("clntCd"));
			loanMap.put("prdtGrp", paramMap.get("prdtGrp"));
			loanMap.put("trstDt", paramMap.get("trstDt"));
	    	
	        if("SELPCH1".equals(paramMap.get("selpchCd"))) {
	        // 매입 / 반입
				if(!ar02Svc.checkPchsClose(paramMap)) {
				// 마감 체크
					thrower.throwCommonException("pchsClose");
				}
	        }else if("SELPCH2".equals(paramMap.get("selpchCd"))){
	        // 매출 / 반품
	        	if(!ar02Svc.checkSellClose(paramMap)) {
	    		// 마감 체크
	        		thrower.throwCommonException("sellClose");
				}
	        	if(bilgAmt > 0) {
	    		// 매출 / 반품의 반품
	        		loanMap.put("totAmt", totAmt);
	        		// 여신 체크
	        		long diffLoan = ar02Svc.checkLoan(loanMap) + totAmtBackUp;
	        		if(diffLoan < 0) {
	                	thrower.throwCreditLoanException("", diffLoan);
	                }
	        	}
	        }
	        
			BigDecimal stockQty = ObjectUtil.defaultBigDecimalNotNull(paramMap.get("realTrstQty"));
			BigDecimal stockWt  = ObjectUtil.defaultBigDecimalNotNull(paramMap.get("realTrstWt"));
			// 파라미터로부터 전달받은 거래처 backup
			String clntCd = paramMap.get("clntCd");
			
			if(paramMap.containsKey("prdtStockCd") && "Y".equals(paramMap.get("prdtStockCd").toString())) 
			{
				// 구분이 자사의 경우 재고추체=거래처는 금문으로 변경
				if("OWNER1".equals(paramMap.get("ownerCd").toString())) {		
					paramMap.put("clntCd",  ar02Mapper.selectOwner1ClntCd(paramMap));		
				}
			}
			stockInfo = sm01Mapper.selectStockInfo(paramMap);
			
			if(stockInfo == null) {
				paramMap.put("pchsUpr", paramMap.get("realTrstUpr"));
				paramMap.put("sellUpr", paramMap.get("realTrstUpr"));
				paramMap.put("stockUpr", paramMap.get("realTrstUpr"));
				paramMap.put("stdUpr", paramMap.get("realTrstUpr"));
				stockQty = "SELPCH1".equals(paramMap.get("selpchCd")) ? stockQty : stockQty.multiply(new BigDecimal("-1"));
				stockWt  = "SELPCH1".equals(paramMap.get("selpchCd")) ? stockWt : stockWt.multiply(new BigDecimal("-1"));
				paramMap.put("stockQty", String.valueOf(stockQty));
				paramMap.put("stockWt", String.valueOf(stockWt));
			} else {
				//매출일때
				if("SELPCH2".equals(paramMap.get("selpchCd"))) 
				{
					paramMap.put("sellUpr", paramMap.get("realTrstUpr"));
					paramMap.put("pchsUpr", stockInfo.get("pchsUpr"));
					stockQty = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockQty")).subtract(stockQty);
					stockWt  = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockWt")).subtract(stockWt);
				}
				//매입일때
				else 
				{
					paramMap.put("pchsUpr", paramMap.get("realTrstUpr"));
					paramMap.put("sellUpr", stockInfo.get("sellUpr"));
					stockQty = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockQty")).add(stockQty);
					stockWt  = ObjectUtil.defaultBigDecimalNotNull(stockInfo.get("stockWt")).add(stockWt);
				}
				paramMap.put("stockUpr", stockInfo.get("stockUpr"));
				paramMap.put("stdUpr", stockInfo.get("stdUpr"));
				paramMap.put("stockQty", String.valueOf(stockQty));
				paramMap.put("stockWt",  String.valueOf(stockWt));
			}
			
			if(paramMap.containsKey("prdtStockCd") && "Y".equals(paramMap.get("prdtStockCd").toString())) 
			{
				// 재고정보 update
				sm01Mapper.updateStockSell(paramMap);
			}
			
			// 부가세 put
	        paramMap.put("bilgVatAmt", String.valueOf(bilgVatAmt));
	        // 납품일자 put
	        paramMap.put("dlvrDttm", paramMap.get("trstDt"));
			// 거래처 원복
			paramMap.put("clntCd", clntCd);
			// insert
			ar02Mapper.insertPchsSell(paramMap);
			
			// 여신 체크후 차감 / 여신 증가
			loanPrcsResult = 0;
			if("SELPCH2".equals(paramMap.get("selpchCd"))){
	        // 매출 / 반품
	        	if(bilgAmt > 0) {
	    		// 매출 / 반품의 반품
	        		loanMap.put("totAmt", totAmt);
	        		// 여신 체크
	        		long diffLoan = ar02Svc.checkLoan(loanMap) + totAmtBackUp;
	        		if(diffLoan < 0) {
	                	thrower.throwCreditLoanException("", diffLoan);
	                }else {
	                	// 여신 차감
	                	loanPrcsResult = ar02Svc.deductLoan(loanMap);
	                }
	        	}else if(bilgAmt < 0){
	        	// 반품
	        		loanMap.put("totAmt", -1 * totAmt);
	        		// 여신 누적
	        		loanPrcsResult = ar02Svc.depositLoan(loanMap);
	        	}
	        }
			
			// 여신 차감 / 여신 증가후 음수 return시 롤백
			if(loanPrcsResult < 0) {
				throw new Exception();
			}
			
			ar02Mapper.callSaleMatch(paramMap);
			
		}
		
	}
	
	

	@Override
	// 마감 체크
	public void checkClose(Map<String, String> paramMap) throws Exception{
		
			if("P".equals(paramMap.get("comfirmType"))) {
				if(!ar02Svc.checkPchsClose(paramMap)) {
				// 마감 체크
			 		thrower.throwCommonException("pchsClose");
				}
				
			}
			
        	if ("S".equals(paramMap.get("comfirmType"))) {
	        	if(!ar02Svc.checkSellClose(paramMap)) {
	        		// 마감 체크
	        		thrower.throwCommonException("sellClose");
	        	}
        		
        	}
        	
        	if ("A".equals(paramMap.get("comfirmType")) || paramMap.get("comfirmType") == null) {
				if(!ar02Svc.checkPchsClose(paramMap)) {
			 		thrower.throwCommonException("pchsClose");
				}
        		
	        	if(!ar02Svc.checkSellClose(paramMap)) {
	        		thrower.throwCommonException("sellClose");
	        	}
        	}
        	
	};

}