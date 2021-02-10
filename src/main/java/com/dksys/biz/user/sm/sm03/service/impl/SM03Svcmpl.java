package com.dksys.biz.user.sm.sm03.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.user.sm.sm03.mapper.SM03Mapper;
import com.dksys.biz.user.sm.sm03.service.SM03Svc;

@Service
public class SM03Svcmpl implements SM03Svc {
	
    @Autowired
    SM03Mapper sm03Mapper;

	@Override
	public int selectStockListCount(Map<String, String> param) {
		return sm03Mapper.selectStockListCount(param);
	}

	@Override
	public List<Map<String, String>> selectStockList(Map<String, String> param) {
		return sm03Mapper.selectStockList(param);
	}

}