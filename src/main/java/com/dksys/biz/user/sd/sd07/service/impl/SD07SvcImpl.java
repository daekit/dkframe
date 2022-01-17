package com.dksys.biz.user.sd.sd07.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.sd.sd07.mapper.SD07Mapper;
import com.dksys.biz.user.sd.sd07.service.SD07Svc;
import com.dksys.biz.user.sm.sm01.mapper.SM01Mapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class SD07SvcImpl implements SD07Svc {

    @Autowired
    SD07Mapper sd07Mapper;
    
    @Autowired
    SM01Mapper sm01Mapper;
    
    @Override
	public Map<String, String> selectClose(Map<String, String> paramMap) {

		return sd07Mapper.selectClose(paramMap);
	}

	@Override
	public void saveClose(Map<String, String> paramMap) {
		sd07Mapper.saveClose(paramMap);
	}

	@Override
	public void excuteStockClose(Map<String, String> paramMap) {
//		int chkCount = sd07Mapper.chkBilgFlagYn(paramMap);
//		if (chkCount > 0) {
//			paramMap.put("chkCount", String.valueOf(chkCount));			
//		}else {
    	String closeYm = paramMap.get("closeYm");
    	if(closeYm.equals("202106")){
      		return;
    	}
    	else {    	
			paramMap.put("errCode", "200");	
			paramMap.put("Message", "");	
			paramMap.put("chkCount", "0");		
			sd07Mapper.deleteStockClose(paramMap);
			sd07Mapper.insertStockClose(paramMap);
			sm01Mapper.updateStockUpr(paramMap);
		}
	}

	@Override
	public void excuteCreditClose(Map<String, String> paramMap) {
//		int chkCount = sd07Mapper.chkBilgFlagYn(paramMap);
//		if (chkCount > 0) {
//			paramMap.put("chkCount", String.valueOf(chkCount));			
//		}else {
		String closeYm = paramMap.get("closeYm");
    	if(closeYm.equals("202106")){
      		return;
    	}
    	else {
			paramMap.put("errCode", "200");	
			paramMap.put("Message", "");	
			paramMap.put("chkCount", "0");	
			sd07Mapper.deleteCreditClose(paramMap);
			sd07Mapper.insertCreditClose(paramMap);
    	}
//		}
	}

	@Override
	public void excuteCreditClosePur(Map<String, String> paramMap) {
//		int chkCount = sd07Mapper.chkBilgFlagYn(paramMap);
//		if (chkCount > 0) {
//			paramMap.put("chkCount", String.valueOf(chkCount));		
//		}else {
		String closeYm = paramMap.get("closeYm");
    	if(closeYm.equals("202106")){
      		return;
    	}
    	else {
			paramMap.put("errCode", "200");	
			paramMap.put("Message", "");	
			paramMap.put("chkCount", "0");		
			sd07Mapper.deleteCreditClose(paramMap);
			sd07Mapper.insertCreditClosePur(paramMap);
    	}
//		}
	}
	
	@Override
	public void excuteCreditDeleteClose(Map<String, String> paramMap) {
		paramMap.put("errCode", "200");	
		paramMap.put("Message", "");	
		paramMap.put("chkCount", "0");	
		sd07Mapper.deleteCreditClose(paramMap);
	}
	
	@Override
	public void excuteCreditDeleteClosePur(Map<String, String> paramMap) {
		paramMap.put("errCode", "200");	
		paramMap.put("Message", "");	
		paramMap.put("chkCount", "0");		
		sd07Mapper.deleteCreditClose(paramMap);
	}

	@Override
	public List<Map<String, String>> selectCloseYmList(Map<String, String> paramMap) {
		return sd07Mapper.selectCloseYmList(paramMap);
	}
	
}