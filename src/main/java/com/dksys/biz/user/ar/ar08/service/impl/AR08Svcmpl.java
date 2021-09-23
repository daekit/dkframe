package com.dksys.biz.user.ar.ar08.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar08.mapper.AR08Mapper;
import com.dksys.biz.user.ar.ar08.service.AR08Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class AR08Svcmpl implements AR08Svc {
	
    @Autowired
    AR08Mapper ar08Mapper;

    @Override
	public int selectCreditCount(Map<String, String> paramMap) {
		return ar08Mapper.selectCreditCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectCreditList(Map<String, String> paramMap) {
		long basisAmt = ar08Mapper.selectBasisAmt(paramMap);
		List<Map<String, String>> resultList = ar08Mapper.selectCreditList(paramMap);
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> lastResultD = new HashMap<String, String>();
		Map<String, String> lastResultM = new HashMap<String, String>();
		Map<String, String> lastResult  = new HashMap<String, String>();
		
		String gubunCd  = "";
		String selpchCd = "";
		long pchsAmt     = 0;
		long sellAmt     = 0;
		long remaindAmt  = basisAmt;
		selpchCd = paramMap.get("selpchCd"); // 매입,매출

		String DailyNew   = "";
		String DailyOld   = "";
		String MonthlyNew = "";
		String MonthlyOld = "";

	    long  cDailyAmt = 0; // 차변누계
	    long  dDailyAmt = 0; // 대변누계
	    long  cMonthAmt = 0; // 차변누계
	    long  dMonthAmt = 0; // 대변누계
	    long  cAmt = 0; // 차변누계
	    long  dAmt = 0; // 대변누계
		
	    for(Map<String, String> detailMap : resultList) {
	     	
			if(DailyOld=="") {
				DailyOld    = detailMap.get("trstDt"); // 일계
				MonthlyOld  = detailMap.get("trstYm"); // 월계				
			} 
			DailyNew    = detailMap.get("trstDt"); // 일계
			MonthlyNew  = detailMap.get("trstYm"); // 월계
			
	    	String trstDt = detailMap.get("trstDt");
			gubunCd     = detailMap.get("gubunCd"); // 매입,매출, 수금,지급, 상계
			pchsAmt     = Long.parseLong(String.valueOf(detailMap.get("pchsAmt")));
			sellAmt     = Long.parseLong(String.valueOf(detailMap.get("sellAmt")));
	/* -------------------------------------------
	 * 일자 위치에 일계 입력, 차변, 대변 금액을 일계로 대체 후 Add
	 * 일자 위치에 월계 입력, 차변, 대변 금액을 월계로 대체 후 Add    	
	 */

			detailMap.put("REMAIND_AMT",String.valueOf(remaindAmt));
			Map<String, String> tempD = new HashMap<String, String>();
			Map<String, String> tempM = new HashMap<String, String>();
			
			if("0".equals(detailMap.get("rn"))) {
				detailMap.put("TRST_DT","");
				DailyOld    = ""; // 일계
				DailyNew    = ""; // 일계
			}
			
			//일계생성, 전기이월은 제외
			if( !DailyOld.equals(DailyNew) && "Y".equals(paramMap.get("dailySumYn")) && !"".equals(detailMap.get("trstDt"))) {
				
				tempD.put("trstDt","일  계");	
				tempD.put("pchsAmt",String.valueOf(cDailyAmt));	
				tempD.put("sellAmt",String.valueOf(dDailyAmt));
				tempD.put("remaindAmt",String.valueOf(remaindAmt));
				cDailyAmt   = 0;
				dDailyAmt   = 0;
				DailyOld    = detailMap.get("trstDt"); // 일계
				result.add(tempD);			
			}
			//월계생성
		    if(!MonthlyOld.equals(MonthlyNew)  && "Y".equals(paramMap.get("monthlySumYn"))) {
		    	tempM.put("trstDt","월  계");	
		    	tempM.put("pchsAmt",String.valueOf(cMonthAmt));	
		    	tempM.put("sellAmt",String.valueOf(dMonthAmt));	
		    	tempM.put("remaindAmt",String.valueOf(remaindAmt));
				cMonthAmt   = 0;
				dMonthAmt   = 0;
				DailyOld    = detailMap.get("trstDt"); // 일계
				MonthlyOld  = detailMap.get("trstYm"); // 월계	
				result.add(tempM);			
			}
			
			//원복
	    	detailMap.put("trstDt",trstDt);
			detailMap.put("pchsAmt",String.valueOf(pchsAmt));	
			detailMap.put("sellAmt",String.valueOf(sellAmt));	
	//---------------------------------------------------------------------------------  
			
			gubunCd     = detailMap.get("gubunCd"); // 매입,매출, 수금,지급, 상계
			pchsAmt     = Long.parseLong(String.valueOf(detailMap.get("pchsAmt")));
			sellAmt     = Long.parseLong(String.valueOf(detailMap.get("sellAmt")));
			
			cDailyAmt   += Long.parseLong(String.valueOf(detailMap.get("pchsAmt")));
			dDailyAmt   += Long.parseLong(String.valueOf(detailMap.get("sellAmt")));
			cMonthAmt   += Long.parseLong(String.valueOf(detailMap.get("pchsAmt")));
			dMonthAmt   += Long.parseLong(String.valueOf(detailMap.get("sellAmt")));
			cAmt        += Long.parseLong(String.valueOf(detailMap.get("pchsAmt")));
			dAmt        += Long.parseLong(String.valueOf(detailMap.get("sellAmt")));
			/* 당월 기초 +매출 +지급  -매입 -수금 : 잔액  
			   매입 채권 조회시 : 당월기초 + 매입-지급
			   매출 채권 조회 시 : 당월기초 + 매출 - 수금
			   당월 기초 + 매출 + 지급  -매입 -수금 : 잔액  	
			*/
			if("SELPCH1".equals(selpchCd)) { // 매입 : 매입채권에 매입 더하고, 지급을 뺀다...미지급금을 계산
				remaindAmt = remaindAmt - sellAmt + pchsAmt ; 				
			}else if ("SELPCH2".equals(selpchCd)) {//매출 : 매출채권에 매출더하고, 수금을 뺀다... 미수금계산	
				remaindAmt = remaindAmt +  sellAmt - pchsAmt; 				
			}else {//매입+매출 : 매입,매출이 합쳐진금액.. 미수금계산
				remaindAmt = remaindAmt +  sellAmt - pchsAmt; 				
			}
			detailMap.put("REMAIND_AMT",String.valueOf(remaindAmt));
		
			result.add(detailMap);		
			
		}
	    
		if( "Y".equals(paramMap.get("dailySumYn"))) {
			
			lastResultD.put("trstDt","일  계");	
			lastResultD.put("pchsAmt",String.valueOf(cDailyAmt));	
			lastResultD.put("sellAmt",String.valueOf(dDailyAmt));
			lastResultD.put("remaindAmt",String.valueOf(remaindAmt));
			result.add(lastResultD);			
		}
		//월계생성
	    if("Y".equals(paramMap.get("monthlySumYn"))) {
	    	lastResultM.put("trstDt","월  계");	
	    	lastResultM.put("pchsAmt",String.valueOf(cMonthAmt));	
	    	lastResultM.put("sellAmt",String.valueOf(dMonthAmt));
	    	lastResultM.put("remaindAmt",String.valueOf(remaindAmt));
			result.add(lastResultM);			
		}
		//누계생성
	    	lastResult.put("trstDt","누  계");	
	    	lastResult.put("pchsAmt",String.valueOf(cAmt));	
	    	lastResult.put("sellAmt",String.valueOf(dAmt));
	    	lastResult.put("remaindAmt",String.valueOf(remaindAmt));
			result.add(lastResult);	
		return result;
	}
}