package com.dksys.biz.user.ar.ar08.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.user.ar.ar08.mapper.AR08Mapper;
import com.dksys.biz.user.ar.ar08.service.AR08Svc;

@Service
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
		
		
		String gubunCd  = "";
		String selpchCd = "";
		long pchsAmt     = 0;
		long sellAmt     = 0;
		long remaindAmt  = basisAmt;
		selpchCd = paramMap.get("selpchCd"); // 매입,매출
		
		
		for (int i=0; i < resultList.size();i++ ) {

			gubunCd  = resultList.get(i).get("gubunCd"); // 매입,매출, 수금,지급, 상계
			pchsAmt  = Long.parseLong(String.valueOf(resultList.get(i).get("pchsAmt")));
			sellAmt  = Long.parseLong(String.valueOf(resultList.get(i).get("sellAmt")));
			/* 당월 기초 +매출 +지급  -매입 -수금 : 잔액  
			   매입 채권 조회시 : 당월기초 + 매입-지급
			   매출 채권 조회 시 : 당월기초 + 매출 - 수금
			   당월 기초 + 매출 + 지급  -매입 -수금 : 잔액  	
			*/
			if(selpchCd == "SELPCH1") { // 매입 : 매입채권에 매입 더하고, 지급을 뺀다...미지급금을 계산
				remaindAmt = remaindAmt + pchsAmt - sellAmt; 				
			}else if (selpchCd == "SELPCH2") {//매출 : 매출채권에 매출더하고, 수금을 뺀다... 미수금계산	
				remaindAmt = remaindAmt +  sellAmt - pchsAmt; 				
			}else {//매입+매출 : 매입,매출이 합쳐진금액.. 미수금계산
				remaindAmt = remaindAmt +  sellAmt - pchsAmt; 				
			}
			 resultList.get(i).put("REMAIND_AMT",String.valueOf(remaindAmt));
			
			if("0".equals(resultList.get(i).get("rn"))) {
				 resultList.get(i).put("TRST_DT","");
			}
		}
		
		return resultList;
	}
}