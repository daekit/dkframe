package com.dksys.biz.admin.cm.cm02.service;

import java.util.List;
import java.util.Map;

public interface CM02Svc {

    public List<Map<String, String>> selectRoleList();

	public int insertRole(Map<String, String> param);

	public int deleteRole(Map<String, String> param);

	public int updateRole(Map<String, String> param);

	public int updateRoleMenu(Map<String, String> param);


}