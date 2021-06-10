package com.dksys.biz.user.ar.ar11.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar11.mapper.AR11Mapper;
import com.dksys.biz.user.ar.ar11.service.AR11Svc;

@Service
@Transactional("erpTransactionManager")
public class AR11Svcmpl implements AR11Svc {
	
    @Autowired
    AR11Mapper ar11Mapper;
    
    @Override
	public int kweonCount(Map<String, String> paramMap) {
    	return ar11Mapper.kweonCount(paramMap);
	}

	@Override
	public List<Map<String, Object>> kweonList(Map<String, String> paramMap) {
		return ar11Mapper.kweonList(paramMap);
	}
}