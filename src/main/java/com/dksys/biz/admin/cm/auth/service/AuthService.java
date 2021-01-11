package com.dksys.biz.admin.cm.auth.service;

import java.util.List;
import java.util.Map;

public interface AuthService {

    public List<Map<String, String>> selectAuthList();

	public int insertAuth(Map<String, String> param);

	public int deleteAuth(Map<String, String> param);

	public int updateAuth(Map<String, String> param);

	public int updateAuthRole(Map<String, String> param);

	public List<Map<String, Object>> selectMenuAuth(String[] authArray);

	public List<Map<String, Object>> selectSubMenuAuth(String[] authArray, String upMenuId);


}