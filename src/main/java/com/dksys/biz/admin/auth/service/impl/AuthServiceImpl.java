package com.dksys.biz.admin.auth.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.auth.mapper.AuthMapper;
import com.dksys.biz.admin.auth.service.AuthService;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
	
    @Autowired
    AuthMapper authMapper;

	@Override
	public List<Map<String, String>> selectAuthList() {
		return authMapper.selectAuthList();
	}

	@Override
	public int insertAuth(Map<String, String> param) {
		return authMapper.insertAuth(param);
	}

	@Override
	public int deleteAuth(Map<String, String> param) {
		return authMapper.deleteAuth(param);
	}

	@Override
	public int updateAuth(Map<String, String> param) {
		return authMapper.updateAuth(param);
	}

	@Override
	public int updateAuthRole(Map<String, String> param) {
		return authMapper.updateAuthRole(param);
	}

	@Override
	public List<Map<String, Object>> getAccessUrl(Map<String, Object> param) {
//		String roleInfo = authMapper.selectRoleFromAuth(param);
//		String[] roleList = roleInfo.split(",");
//		List<Map<String, Object>> result = authMapper.selectMenuFromRole(roleList);
		return null;
	}

}