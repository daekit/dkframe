package com.dksys.biz.user.sm.sm06.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.user.sm.sm06.mapper.SM06Mapper;
import com.dksys.biz.user.sm.sm06.service.SM06Svc;

@Service
public class SM06Svcmpl implements SM06Svc {
	
    @Autowired
    SM06Mapper sm06Mapper;

	@Override
	public int selectStockTotalListCount(Map<String, String> param) {
		return sm06Mapper.selectStockTotalListCount(param);
	}

	@Override
	public List<Map<String, String>> selectStockTotalList(Map<String, String> param) {
		return sm06Mapper.selectStockTotalList(param);
	}
	
	@Override
	public int selectStockDetailListCount(Map<String, String> param) {
		return sm06Mapper.selectStockDetailListCount(param);
	}

	@Override
	public List<Map<String, String>> selectStockDetailList(Map<String, String> param) {
		return sm06Mapper.selectStockDetailList(param);
	}
	
}