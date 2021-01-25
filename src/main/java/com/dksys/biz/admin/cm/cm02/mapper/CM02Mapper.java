package com.dksys.biz.admin.cm.cm02.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CM02Mapper {
	
	List<Map<String, String>> selectRoleList();

	int insertRole(Map<String, String> param);

	int deleteRole(Map<String, String> param);

	int updateRole(Map<String, String> param);

	int updateRoleMenu(Map<String, String> param);
	
}
