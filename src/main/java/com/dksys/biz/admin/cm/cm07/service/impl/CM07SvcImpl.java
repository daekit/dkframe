package com.dksys.biz.admin.cm.cm07.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.admin.cm.cm07.mapper.CM07Mapper;
import com.dksys.biz.admin.cm.cm07.service.CM07Svc;

@Service
public class CM07SvcImpl implements CM07Svc {
	
    @Autowired
    CM07Mapper cm07Mapper;

	@Override
	public List<Map<String, String>> selectLevelList(Map<String, String> paramMap) {
		return cm07Mapper.selectLevelList(paramMap);
	}

	@Override
	public int selectLevelCount(Map<String, String> paramMap) {
		return cm07Mapper.selectLevelCount(paramMap);
	}

	@Override
	public void insertLevel(Map<String, String> paramMap) throws Exception{
		cm07Mapper.insertLevel(paramMap);
	}

	@Override
	public Map<String, String> selectLevelInfo(Map<String, String> paramMap) {
		return cm07Mapper.selectLevelInfo(paramMap);
	}

	@Override
	public void updateLevel(Map<String, String> paramMap) throws Exception{
		cm07Mapper.updateLevel(paramMap);
	}

}