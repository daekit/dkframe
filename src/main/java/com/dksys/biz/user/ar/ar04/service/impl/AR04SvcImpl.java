package com.dksys.biz.user.ar.ar04.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar04.mapper.AR04Mapper;
import com.dksys.biz.user.ar.ar04.service.AR04Svc;

@Service
@Transactional("erpTransactionManager")
public class AR04SvcImpl implements AR04Svc {
	
    @Autowired
    AR04Mapper ar04Mapper;
    
	@Override
	public int selectTaxBilgCount(Map<String, String> paramMap) {
		return ar04Mapper.selectTaxBilgCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectTaxBilgList(Map<String, String> paramMap) {
		return ar04Mapper.selectTaxBilgList(paramMap);
	}

	@Override
	public Map<String, String> selectTaxBilg(Map<String, String> paramMap) {
		return ar04Mapper.selectTaxBilg(paramMap);
	}
    
	@Override
	public int updateTaxBilg(Map<String, String> paramMap) {
		return ar04Mapper.updateTaxBilg(paramMap);
	}
    
	@Override
	public int selectTaxRcvCount(Map<String, String> paramMap) {
		return ar04Mapper.selectTaxRcvCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectTaxRcvList(Map<String, String> paramMap) {
		return ar04Mapper.selectTaxRcvList(paramMap);
	}
}