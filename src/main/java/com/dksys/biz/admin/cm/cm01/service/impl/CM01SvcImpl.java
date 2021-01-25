package com.dksys.biz.admin.cm.cm01.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.cm.cm01.mapper.CM01Mapper;
import com.dksys.biz.admin.cm.cm01.service.CM01Svc;

@Service
@Transactional("erpTransactionManager")
public class CM01SvcImpl implements CM01Svc {
	
    @Autowired
    CM01Mapper authMapper;

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


	@Override
	public List<Map<String, Object>> selectSubMenuAuth(String[] authArray, String upMenuId) {
		String roleStr = "";
		String menuStr = "";
		String subMenuStr = "";
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
		List<Map<String, Object>> upResult = authMapper.selectParentMenuAuth(upMenuId);
		for (int i = 0; i < result.size(); i++) {
			for (int j = 0; j < upResult.size(); j++) {
				if(result.get(i).get("menuId").toString().equals(upResult.get(j).get("menuId").toString())){
					subMenuStr += upResult.get(j).get("menuId").toString() + ",";
				}
			}
		}
		String[] subMenuArray = subMenuStr.split(",");
		List<Map<String, Object>> returnResult = authMapper.selectMenuAuth(subMenuArray);
		return returnResult;
	}
}