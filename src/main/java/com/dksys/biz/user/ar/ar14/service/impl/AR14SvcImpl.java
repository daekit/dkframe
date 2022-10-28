package com.dksys.biz.user.ar.ar14.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar14.mapper.AR14Mapper;
import com.dksys.biz.user.ar.ar14.service.AR14Svc;
import com.dksys.biz.util.ObjectUtil;
import com.dksys.biz.user.ar.ar14.mapper.AR14Mapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class AR14SvcImpl implements AR14Svc {
	
    @Autowired
    AR14Mapper ar14Mapper;
    
	@Override
	public int selectDebtCount(Map<String, String> paramMap) {
		return ar14Mapper.selectDebtCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectDebtList(Map<String, String> paramMap) {
		
		List<Map<String, String>> resultListMap = new ArrayList<Map<String,String>>(); 
		resultListMap = ar14Mapper.selectDebtList(paramMap);
		
		
		int balance = 0;
		
		if(resultListMap != null) {
			for(int i=0; i<resultListMap.size(); i++) {
				String assortCd = resultListMap.get(i).get("assortCd");
				int debtAmt = MapUtils.getInteger(resultListMap.get(i), "debtAmt"); 
				
				// 부채일시
				if(assortCd.equals("ASSORTCD1")) {
					balance += debtAmt;
					resultListMap.get(i).put("DEBT_AMT", Integer.toString(debtAmt));
					resultListMap.get(i).put("REPAY_AMT", Integer.toString(0));
				// 상환일시
				}else if(assortCd.equals("ASSORTCD2")) {
					balance -= debtAmt;
					resultListMap.get(i).put("DEBT_AMT", Integer.toString(0));
					resultListMap.get(i).put("REPAY_AMT", Integer.toString(debtAmt));
				}
				
				resultListMap.get(i).put("balance", Integer.toString(balance));
			}
		}
		
		return resultListMap;
	}
	
	@Override
	// 매입 / 반입 / 매출 / 반품
	public void insertDebt(Map<String, String> paramMap) {
		ar14Mapper.insertDebt(paramMap);
	}
	
	@Override
	public int selectDebtGroupCount(Map<String, String> paramMap) {
		return ar14Mapper.selectDebtGroupCount(paramMap);
	}
	
	@Override
	public List<Map<String, String>> selectDebtGroupList(Map<String, String> paramMap) {
		return ar14Mapper.selectDebtGroupList(paramMap);
	}
	
	@Override
	// 부실채권 삭제
	public void deleteDebt(Map<String, String> paramMap) {
		ar14Mapper.deleteDebt(paramMap);
	}

}