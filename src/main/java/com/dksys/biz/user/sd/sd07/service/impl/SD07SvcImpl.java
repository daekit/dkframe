package com.dksys.biz.user.sd.sd07.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.sd.sd07.mapper.SD07Mapper;
import com.dksys.biz.user.sd.sd07.service.SD07Svc;

@Service
@Transactional("erpTransactionManager")
public class SD07SvcImpl implements SD07Svc {
	
    @Autowired
    SD07Mapper sd7Mapper;
    
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
		sd7Mapper.deleteStockClose(paramMap);
		sd7Mapper.insertStockClose(paramMap);
	}

	@Override
	public void excuteCreditClose(Map<String, String> paramMap) {
		sd7Mapper.deleteCreditClose(paramMap);
		sd7Mapper.insertCreditClose(paramMap);
	}

}