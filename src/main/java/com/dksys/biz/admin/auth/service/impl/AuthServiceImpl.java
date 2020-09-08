package com.dksys.biz.admin.auth.service.impl;

import java.util.Arrays;
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
	public List<Map<String, Object>> selectMenuAuth(String[] authArray) {
		String roleStr = "";
		String menuStr = "";
		List<String> roleList = authMapper.selectRoleFromAuth(authArray);
		for (int i = 0; i < roleList.size(); i++) {
			roleStr += roleList.get(i) + ",";
		}
		String[] roleArray = roleStr.split(",");
		roleArray = Arrays.stream(roleArray).distinct().toArray(String[]::new);
		List<String> menuList = authMapper.selectMenuFromRole(roleArray);
		for (int i = 0; i < menuList.size(); i++) {
			menuStr += menuList.get(i) + ",";
		}
		String[] menuArray = menuStr.split(",");
		List<Map<String, Object>> result = authMapper.selectMenuAuth(menuArray);
		return result;
	}

}