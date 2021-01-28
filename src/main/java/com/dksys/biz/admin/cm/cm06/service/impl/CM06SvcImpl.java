package com.dksys.biz.admin.cm.cm06.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.admin.cm.cm06.mapper.CM06Mapper;
import com.dksys.biz.admin.cm.cm06.service.CM06Svc;

@Service
public class CM06SvcImpl implements CM06Svc {
	
    @Autowired
    CM06Mapper cm06Mapper;

	@Override
	public List<Map<String, String>> selectUserList(Map<String, String> paramMap) {
		return cm06Mapper.selectUserList(paramMap);
	}

	@Override
	public int selectUserCount(Map<String, String> paramMap) {
		return cm06Mapper.selectUserCount(paramMap);
	}

	@Override
	public void insertUser(Map<String, String> paramMap) throws Exception{
		cm06Mapper.insertUser(paramMap);
	}

	@Override
	public Map<String, String> selectUserInfo(Map<String, String> paramMap) {
		return cm06Mapper.selectUserInfo(paramMap);
	}

	@Override
	public void updateUser(Map<String, String> paramMap) throws Exception {
		cm06Mapper.updateUser(paramMap);
	}

}