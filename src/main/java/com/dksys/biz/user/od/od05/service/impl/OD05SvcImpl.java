package com.dksys.biz.user.od.od05.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar02.mapper.AR02Mapper;
import com.dksys.biz.user.od.od05.service.OD05Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class OD05SvcImpl implements OD05Svc {
	
	@Autowired
	AR02Mapper ar02Mapper;
	
	@Override
	public int selectSellSaleCount(Map<String, String> paramMap) {
		return ar02Mapper.selectSellSaleCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectSellSaleList(Map<String, String> paramMap) {
		return ar02Mapper.selectSellSaleList(paramMap);
	}
}