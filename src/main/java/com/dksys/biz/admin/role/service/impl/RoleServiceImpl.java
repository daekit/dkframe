package com.dksys.biz.admin.role.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.role.mapper.RoleMapper;
import com.dksys.biz.admin.role.service.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	
    @Autowired
    RoleMapper RoleMapper;

	@Override
	public List<Map<String, String>> selectRoleList() {
		return RoleMapper.selectRoleList();
	}

	@Override
	public int insertRole(Map<String, String> param) {
		return RoleMapper.insertRole(param);
	}

	@Override
	public int deleteRole(Map<String, String> param) {
		return RoleMapper.deleteRole(param);
	}

	@Override
	public int updateRole(Map<String, String> param) {
		return RoleMapper.updateRole(param);
	}

}