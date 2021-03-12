package com.dksys.biz.user.od.od02.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.user.od.od02.mapper.OD02Mapper;
import com.dksys.biz.user.od.od02.service.OD02Svc;

@Service
public class OD02SvcImpl implements OD02Svc {
    
	@Autowired
    OD02Mapper od02Mapper;
	
	@Override
	public int selectPurchaseCount(Map<String, String> paramMap) {
		return od02Mapper.selectPurchaseCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectPurchaseList(Map<String, String> paramMap) {
		return od02Mapper.selectPurchaseList(paramMap);
	}

}