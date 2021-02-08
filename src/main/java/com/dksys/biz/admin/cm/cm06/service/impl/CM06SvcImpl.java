package com.dksys.biz.admin.cm.cm06.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.admin.cm.cm04.mapper.CM04Mapper;
import com.dksys.biz.admin.cm.cm06.mapper.CM06Mapper;
import com.dksys.biz.admin.cm.cm06.service.CM06Svc;

@Service
public class CM06SvcImpl implements CM06Svc {
	
    @Autowired
    CM06Mapper cm06Mapper;
    
    @Autowired
    CM04Mapper cm04Mapper;

    @Override
	public int selectUserCount(Map<String, String> paramMap) {
		return cm06Mapper.selectUserCount(paramMap);
	}
    
	@Override
	public List<Map<String, String>> selectUserList(Map<String, String> paramMap) {
		return cm06Mapper.selectUserList(paramMap);
	}
	
	@Override
	public Map<String, String> selectUserInfo(Map<String, String> paramMap) {
		return cm06Mapper.selectUserInfo(paramMap);
	}
	
	@Override
	public List<Map<String, String>> selectUserTree(Map<String, String> paramMap) {
		List<Map<String, String>> deptTree = cm04Mapper.selectDeptTree(paramMap);
		List<Map<String, String>> useTree = cm06Mapper.selectUserTree(paramMap);
		deptTree.addAll(useTree);
		return deptTree;
	}

	@Override
	public void insertUser(Map<String, String> paramMap) throws Exception{
		cm06Mapper.insertUser(paramMap);
	}

	@Override
	public void updateUser(Map<String, String> paramMap) throws Exception {
		cm06Mapper.updateUser(paramMap);
	}

}