package com.dksys.biz.admin.role.service;

import java.util.List;
import java.util.Map;

public interface RoleService {

    public List<Map<String, String>> selectRoleList();

	public int insertRole(Map<String, String> param);

	public int deleteRole(Map<String, String> param);

	public int updateRole(Map<String, String> param);


}