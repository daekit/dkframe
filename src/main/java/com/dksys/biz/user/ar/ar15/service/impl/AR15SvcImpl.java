package com.dksys.biz.user.ar.ar15.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.ar.ar15.mapper.AR15Mapper;
import com.dksys.biz.user.ar.ar15.service.AR15Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class AR15SvcImpl implements AR15Svc {

	@Autowired
	AR15Mapper ar15Mapper;

	@Override
	public int selectSalesMngYyListCount(Map<String, String> paramMap) {
		return ar15Mapper.selectSalesMngYyListCount(paramMap);
	}
	
	@Override
	public List<Map<String, String>> selectSalesMngYyList(Map<String, String> paramMap) {
		return ar15Mapper.selectSalesMngYyList(paramMap);
	}

	
	
	

}
