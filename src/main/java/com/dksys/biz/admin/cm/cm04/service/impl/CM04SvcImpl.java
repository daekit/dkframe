package com.dksys.biz.admin.cm.cm04.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.admin.cm.cm04.mapper.CM04Mapper;
import com.dksys.biz.admin.cm.cm04.service.CM04Svc;

@Service
public class CM04SvcImpl implements CM04Svc {
	
    @Autowired
    CM04Mapper cm04Mapper;

	@Override
	public List<Map<String, String>> selectDeptTree(Map<String, String> paramMap) {
		return cm04Mapper.selectDeptTree(paramMap);
	}

	@Override
	public Map<String, String> selectDeptInfo(Map<String, String> paramMap) {
		return cm04Mapper.selectDeptInfo(paramMap);
	}

	@Override
	public int selectDeptCount(Map<String, String> paramMap) {
		return cm04Mapper.selectDeptCount(paramMap);
	}

	@Override
	public void updateDept(Map<String, String> paramMap) throws Exception{
		cm04Mapper.updateDept(paramMap);
	}

	@Override
	public void moveDept(List<Map<String, String>> paramList) throws Exception{
		for(Map<String, String> paramMap : paramList) {
			cm04Mapper.moveDept(paramMap);
		}
	}

}