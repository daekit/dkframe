package com.dksys.biz.user.ar.ar09.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar09.mapper.AR09Mapper;
import com.dksys.biz.user.ar.ar09.service.AR09Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class AR09Svcmpl implements AR09Svc {
	
    @Autowired
    AR09Mapper ar09Mapper;

    @Override
	public int selectYySalesListCount(Map<String, String> paramMap) {
		return ar09Mapper.selectYySalesListCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectYySalesList(Map<String, String> paramMap) {
			
		return ar09Mapper.selectYySalesList(paramMap);
	}
}