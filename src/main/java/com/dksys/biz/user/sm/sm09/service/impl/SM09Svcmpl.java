package com.dksys.biz.user.sm.sm09.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.sm.sm09.mapper.SM09Mapper;
import com.dksys.biz.user.sm.sm09.service.SM09Svc;

@Service
@Transactional("erpTransactionManager")
public class SM09Svcmpl implements SM09Svc {
	
    @Autowired
    SM09Mapper sm09Mapper;

 

	@Override
	public int selectStockListCount(Map<String, String> param) {
		return sm09Mapper.selectStockListCount(param);
	}

	@Override
	public List<Map<String, String>> selectStockList(Map<String, String> param) {
		return sm09Mapper.selectStockList(param);
	}

//	@Override
//	public List<Map<String, String>> selectPrdtStockList(Map<String, String> param) {
//		return sm09Mapper.selectPrdtStockList(param);
//	}

	@Override
	public int selectStockHistoryListCount(Map<String, String> param) {
		return sm09Mapper.selectStockHistoryListCount(param);
	}

	@Override
	public List<Map<String, String>> selectStockHistoryList(Map<String, String> param) {
		return sm09Mapper.selectStockHistoryList(param);
	}

	


}