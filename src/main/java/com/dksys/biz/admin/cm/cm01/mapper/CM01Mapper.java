package com.dksys.biz.admin.cm.cm01.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CM01Mapper {
	
	List<Map<String, String>> selectAuthList();

	int insertAuth(Map<String, String> param);

	int deleteAuth(Map<String, String> param);

	int updateAuth(Map<String, String> param);

	int updateAuthRole(Map<String, String> param);

	List<String> selectRoleFromAuth(String[] authArray);

	List<Map<String, String>> selectMenuFromRole(String[] roleArray);

	List<Map<String, Object>> selectMenuAuth(String[] menuArray);

	List<Map<String, Object>> selectParentMenuAuth(String upMenuId);
	
}
