package com.dksys.biz.user.sm.sm01.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.user.sm.sm01.mapper.SM01Mapper;
import com.dksys.biz.user.sm.sm01.service.SM01Svc;

@Service
public class SM01Svcmpl implements SM01Svc {
	
    @Autowired
    SM01Mapper sm01Mapper;

 

	@Override
	public int selectStockListCount(Map<String, String> param) {
		return sm01Mapper.selectStockListCount(param);
	}

	@Override
	public List<Map<String, String>> selectStockList(Map<String, String> param) {
		return sm01Mapper.selectStockList(param);
	}

	


}