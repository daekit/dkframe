package com.dksys.biz.admin.role.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper {
	
	List<Map<String, String>> selectRoleList();

	int insertRole(Map<String, String> param);

	int deleteRole(Map<String, String> param);

	int updateRole(Map<String, String> param);

	int updateRoleMenu(Map<String, String> param);
	
}
