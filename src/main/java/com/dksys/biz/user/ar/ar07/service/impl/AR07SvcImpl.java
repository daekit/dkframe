package com.dksys.biz.user.ar.ar07.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar07.mapper.AR07Mapper;
import com.dksys.biz.user.ar.ar07.service.AR07Svc;

@Service
@Transactional("erpTransactionManager")
public class AR07SvcImpl implements AR07Svc {
	
    @Autowired
    AR07Mapper ar07Mapper;
    
	@Override
	public int selectMtClosCditCount(Map<String, String> paramMap) {
		return ar07Mapper.selectMtClosCditCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectMtClosCditList(Map<String, String> paramMap) {
		return ar07Mapper.selectMtClosCditList(paramMap);
	}
}