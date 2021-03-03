package com.dksys.biz.user.ar.ar03.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.user.ar.ar03.mapper.AR03Mapper;
import com.dksys.biz.user.ar.ar03.service.AR03Svc;

@Service
public class AR03SvcImpl implements AR03Svc {

	@Autowired
    AR03Mapper ar03Mapper;
	
	@Override
	public int selectCaryngCount(Map<String, String> paramMap) {
		return ar03Mapper.selectCaryngCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectCaryngList(Map<String, String> paramMap) {
		return ar03Mapper.selectCaryngList(paramMap);
	}
	
	@Override
	public Map<String, String> selectCaryngInfo(Map<String, String> paramMap) {
		return ar03Mapper.selectCaryngInfo(paramMap);
	}
	
	@Override
	public int selectShipCount(Map<String, String> paramMap) {
		return ar03Mapper.selectShipCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectShipList(Map<String, String> paramMap) {
		return ar03Mapper.selectShipList(paramMap);
	}
	
	@Override
	public int insertCaryng(Map<String, String> param) {
		return ar03Mapper.insertCaryng(param);
	}

	@Override
	public int updateCaryng(Map<String, String> param) {
		return ar03Mapper.updateCaryng(param);
	}



}