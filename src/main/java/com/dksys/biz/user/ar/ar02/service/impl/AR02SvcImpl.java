package com.dksys.biz.user.ar.ar02.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		return ar02Mapper.selectSellCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectSellList(Map<String, String> paramMap) {
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
			ar02Mapper.updatePchsSell(detailMap);
			
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
			int stockQty = 0;
			int stockWt = 0;
			String stockChgCd = "STOCKCHG09";
			if("SELPCH2".equals(paramMap.get("selpchCd"))) 
			{
				stockChgCd = "STOCKCHG09";
				stockQty = Integer.parseInt(stockInfo.get("stockQty")) + Integer.parseInt(paramMap.get("realTrstQty"));
				stockWt  = Integer.parseInt(stockInfo.get("stockWt"))  + Integer.parseInt(paramMap.get("realTrstWt"));
			} else 
			{
				stockChgCd = "STOCKCHG08";
				stockQty = Integer.parseInt(stockInfo.get("stockQty")) - Integer.parseInt(paramMap.get("realTrstQty"));
				stockWt  = Integer.parseInt(stockInfo.get("stockWt"))  - Integer.parseInt(paramMap.get("realTrstWt"));
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
		
		ar02Mapper.deleteSell(paramMap);
		
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
        
		int stockQty = Integer.parseInt(paramMap.get("realTrstQty"));
		int stockWt  = Integer.parseInt(paramMap.get("realTrstWt"));
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
			stockQty = "SELPCH1".equals(paramMap.get("selpchCd")) ? stockQty : stockQty*-1;
			stockWt  = "SELPCH1".equals(paramMap.get("selpchCd")) ? stockWt : stockWt*-1;
			paramMap.put("stockQty", String.valueOf(stockQty));
			paramMap.put("stockWt", String.valueOf(stockWt));
		} else {
			//매출일때
			if("SELPCH2".equals(paramMap.get("selpchCd"))) 
			{
				paramMap.put("sellUpr", paramMap.get("realTrstUpr"));
				paramMap.put("pchsUpr", stockInfo.get("pchsUpr"));
				stockQty = Integer.parseInt(stockInfo.get("stockQty")) - stockQty;
				stockWt  = Integer.parseInt(stockInfo.get("stockWt")) - stockWt;
			}
			//매입일때
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
	}
	
	@Override
	public void insertSalesDivision(List<Map<String, String>> paramList) throws Exception{
		// 여신체크
		long totAmt = 0;
		Map<String, Object> loanMap = new HashMap<String, Object>();
		for(int i=0;i<paramList.size();i++) {
			Map<String, String> paramMap = paramList.get(i);
			if(i == 0) {
				loanMap.put("coCd", paramMap.get("coCd"));
				loanMap.put("clntCd", paramMap.get("divClntCd"));
				loanMap.put("prdtGrp", paramMap.get("prdtGrp"));
				loanMap.put("trstDt", paramMap.get("trstDt"));
			}
			totAmt += Long.parseLong(paramMap.get("divTotAmt"));
		}
		loanMap.put("totAmt", totAmt);
		
		long diffLoan =  checkLoan(loanMap);
		if(diffLoan < 0) {
			thrower.throwCreditLoanException("", diffLoan);
		}
		
		for(Map<String, String> paramMap : paramList) {
			// paramList 순회하며 넘어온값 그대로 update
			ar02Mapper.updatePchsSell(paramMap);
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
			// insert
			ar02Mapper.insertPchsSell(divMap);
		}
		
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

}