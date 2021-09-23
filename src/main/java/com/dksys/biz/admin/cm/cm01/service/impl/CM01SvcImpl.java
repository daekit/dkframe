package com.dksys.biz.admin.cm.cm01.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.cm.cm01.mapper.CM01Mapper;
import com.dksys.biz.admin.cm.cm01.service.CM01Svc;
import com.tmax.tibero.Debug;

@Service
@Transactional(rollbackFor = Exception.class)
public class CM01SvcImpl implements CM01Svc {
	
    @Autowired
    CM01Mapper cm01Mapper;

	@Override
	public List<Map<String, String>> selectAuthList() {
		return cm01Mapper.selectAuthList();
	}

	@Override
	public int insertAuth(Map<String, String> param) {
		return cm01Mapper.insertAuth(param);
	}

	@Override
	public int deleteAuth(Map<String, String> param) {
		return cm01Mapper.deleteAuth(param);
	}

	@Override
	public int updateAuth(Map<String, String> param) {
		return cm01Mapper.updateAuth(param);
	}

	@Override
	public int updateAuthRole(Map<String, String> param) {
		return cm01Mapper.updateAuthRole(param);
	}

	@Override
	public List<Map<String, Object>> selectMenuAuth(String[] authArray) {
		String roleStr = "";
		String menuStr = "";
		
		// auth에 해당하는 role 조회
		List<String> roleList = cm01Mapper.selectRoleFromAuth(authArray);
		for (int i = 0; i < roleList.size(); i++) {
			roleStr += roleList.get(i) + ",";
		}
		String[] roleArray = roleStr.split(",");
		roleArray = Arrays.stream(roleArray).distinct().toArray(String[]::new);
		
		// role에 해당하는 menuId, 저장권한 조회
		List<Map<String, String>> menuList = cm01Mapper.selectMenuFromRole(roleArray);
		for (int i = 0; i < menuList.size(); i++) {
			menuStr += menuList.get(i).get("roleMenu") + ",";
		}
		String[] menuArray = menuStr.split(",");
		
		// menuId에 해당하는 menu정보 조회
		List<Map<String, Object>> result = cm01Mapper.selectMenuAuth(menuArray);
		for(Map<String, Object> map : result) {
			for(Map<String, String> menuMap : menuList) {
				if(map.get("menuId").toString().equals((menuMap.get("roleMenu")))){
					map.put("save_Yn", menuMap.get("saveYn"));
					break;
				}
			}
		}
		return result;
	}


	@Override
	public List<Map<String, Object>> selectSubMenuAuth(String[] authArray, String upMenuId) {
		String roleStr = "";
		String menuStr = "";
		String subMenuStr = "";
		List<String> roleList = cm01Mapper.selectRoleFromAuth(authArray);
		for (int i = 0; i < roleList.size(); i++) {
			roleStr += roleList.get(i) + ",";
		}
		String[] roleArray = roleStr.split(",");
		roleArray = Arrays.stream(roleArray).distinct().toArray(String[]::new);
		List<Map<String, String>> menuList = cm01Mapper.selectMenuFromRole(roleArray);
		for (int i = 0; i < menuList.size(); i++) {
			menuStr += menuList.get(i).get("roleMenu") + ",";
		}
		
		String[] menuArray = menuStr.split(",");
		List<Map<String, Object>> result = cm01Mapper.selectMenuAuth(menuArray);
		List<Map<String, Object>> upResult = cm01Mapper.selectParentMenuAuth(upMenuId);
		for (int i = 0; i < result.size(); i++) {
			for (int j = 0; j < upResult.size(); j++) {
				if(result.get(i).get("menuId").toString().equals(upResult.get(j).get("menuId").toString())){
					subMenuStr += upResult.get(j).get("menuId").toString() + ",";
				}
			}
		}
		String[] subMenuArray = subMenuStr.split(",");
		List<Map<String, Object>> returnResult = cm01Mapper.selectMenuAuth(subMenuArray);
		return returnResult;
	}
}