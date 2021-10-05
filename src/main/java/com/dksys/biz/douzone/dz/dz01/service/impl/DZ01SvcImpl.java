package com.dksys.biz.douzone.dz.dz01.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.cm.cm06.mapper.CM06Mapper;
import com.dksys.biz.douzone.dz.dz01.mssqlmapper.DZ01Mapper;
import com.dksys.biz.douzone.dz.dz01.service.DZ01Svc;

@Service
public class DZ01SvcImpl implements DZ01Svc {
	@Autowired
	CM06Mapper cm06Mapper;
	
	@Autowired
	DZ01Mapper dz01Mapper;
	
	@Autowired
	@Qualifier("tiberoTransactionManager")
	DataSourceTransactionManager tiberoTransactionManager;
	
	@Autowired
	@Qualifier("mssqlTransactionManager")
	DataSourceTransactionManager mssqlTransactionManager;
	
	@Override
	public List<Map<String, String>> testSelect(Map<String, String> paramMap) {
		List<Map<String, String>> testList = dz01Mapper.testSelect(paramMap);
		return testList;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void testMultiTransaction() throws Exception{
		dz01Mapper.testInsert();
		cm06Mapper.updateUserName();
		double testValue = 10/0;
	}
}