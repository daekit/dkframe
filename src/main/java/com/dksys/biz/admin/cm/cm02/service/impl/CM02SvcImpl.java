package com.dksys.biz.admin.cm.cm02.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dksys.biz.admin.cm.cm02.mapper.CM02Mapper;
import com.dksys.biz.admin.cm.cm02.service.CM02Svc;

@Service
public class CM02SvcImpl implements CM02Svc {
	
    @Autowired
    CM02Mapper cm02Mapper;

	@Override
	public List<Map<String, String>> selectRoleList() {
		return cm02Mapper.selectRoleList();
	}

	@Override
	public int insertRole(Map<String, String> param) {
		return cm02Mapper.insertRole(param);
	}

	@Override
	public int deleteRole(Map<String, String> param) {
		return cm02Mapper.deleteRole(param);
	}

	@Override
	public int updateRole(Map<String, String> param) {
		return cm02Mapper.updateRole(param);
	}

	@Override
	public int updateRoleMenu(Map<String, String> param) {
		return cm02Mapper.updateRoleMenu(param);
	}

}