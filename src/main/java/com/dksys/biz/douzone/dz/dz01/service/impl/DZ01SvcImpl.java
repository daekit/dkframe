package com.dksys.biz.douzone.dz.dz01.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.cm.cm06.service.CM06Svc;
import com.dksys.biz.douzone.dz.dz01.mssqlmapper.DZ01Mapper;
import com.dksys.biz.douzone.dz.dz01.service.DZ01Svc;

@Service
@Transactional(value="mssqlTransactionManager", rollbackFor = Exception.class)
public class DZ01SvcImpl implements DZ01Svc {
	@Autowired
	DZ01Mapper dz01Mapper;
	
	@Autowired
	CM06Svc cm06Svc;
	
	@Override
	public List<Map<String, String>> testSelect(Map<String, String> paramMap) {
		List<Map<String, String>> testList = dz01Mapper.testSelect(paramMap);
		return testList;
	}

	@Override
	public void testInsert() throws Exception{
		dz01Mapper.testInsert();
		cm06Svc.updateUserName();
	}
}