package com.dksys.biz.user.ar.ar10.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar10.mapper.AR10Mapper;
import com.dksys.biz.user.ar.ar10.service.AR10Svc;

@Service
@Transactional("erpTransactionManager")
public class AR10Svcmpl implements AR10Svc {
	
    @Autowired
    AR10Mapper ar10Mapper;
    
    @Override
	public int selectPchsSellCount(Map<String, String> paramMap) {
    	return ar10Mapper.selectPchsSellCount(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectPchsSellList(Map<String, String> paramMap) {
		return ar10Mapper.selectPchsSellList(paramMap);
	}
}