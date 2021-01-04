package com.dksys.biz.admin.auth.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {
	
	List<Map<String, String>> selectAuthList();

	int insertAuth(Map<String, String> param);

	int deleteAuth(Map<String, String> param);

	int updateAuth(Map<String, String> param);

	int updateAuthRole(Map<String, String> param);

	List<String> selectRoleFromAuth(String[] authArray);

	List<String> selectMenuFromRole(String[] roleArray);

	List<Map<String, Object>> selectMenuAuth(String[] menuArray);

	List<Map<String, Object>> selectParentMenuAuth(String upMenuId);
	
}
