package com.dksys.biz.user.sd.sd07.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.sd.sd07.mapper.SD07Mapper;
import com.dksys.biz.user.sd.sd07.service.SD07Svc;
import com.dksys.biz.user.sm.sm01.mapper.SM01Mapper;

@Service
@Transactional("erpTransactionManager")
public class SD07SvcImpl implements SD07Svc {

    @Autowired
    SD07Mapper sd7Mapper;
    
    @Autowired
    SM01Mapper sm01Mapper;
    
    @Override
	public Map<String, String> selectClose(Map<String, String> paramMap) {
		return sd7Mapper.selectClose(paramMap);
	}

	@Override
	public void saveClose(Map<String, String> paramMap) {
		sd7Mapper.saveClose(paramMap);
	}

	@Override
	public void excuteStockClose(Map<String, String> paramMap) {
		int chkCount = sd7Mapper.chkBilgFlagYn(paramMap);
		if (chkCount > 0) {
			paramMap.put("chkCount", String.valueOf(chkCount));			
		}else {
			paramMap.put("errCode", "200");	
			paramMap.put("Message", "");		
			sd7Mapper.deleteStockClose(paramMap);
			sd7Mapper.insertStockClose(paramMap);
			sm01Mapper.updateStockUpr(paramMap);
		}
	}

	@Override
	public void excuteCreditClose(Map<String, String> paramMap) {
		int chkCount = sd7Mapper.chkBilgFlagYn(paramMap);
		if (chkCount > 0) {
			paramMap.put("chkCount", String.valueOf(chkCount));			
		}else {
			paramMap.put("errCode", "200");	
			paramMap.put("Message", "");		
			sd7Mapper.chkBilgFlagYn(paramMap);
			sd7Mapper.deleteCreditClose(paramMap);
			sd7Mapper.insertCreditClose(paramMap);
		}
	}

	@Override
	public void excuteCreditClosePur(Map<String, String> paramMap) {
		int chkCount = sd7Mapper.chkBilgFlagYn(paramMap);
		if (chkCount > 0) {
			paramMap.put("chkCount", String.valueOf(chkCount));		
		}else {
			paramMap.put("errCode", "200");	
			paramMap.put("Message", "");		
			sd7Mapper.chkBilgFlagYn(paramMap);
			sd7Mapper.deleteCreditClose(paramMap);
			sd7Mapper.insertCreditClosePur(paramMap);
		}
	}

}