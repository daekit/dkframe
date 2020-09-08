package com.dksys.biz.admin.auth.service;

import java.util.List;
import java.util.Map;

public interface AuthService {

    public List<Map<String, String>> selectAuthList();

	public int insertAuth(Map<String, String> param);

	public int deleteAuth(Map<String, String> param);

	public int updateAuth(Map<String, String> param);

	public int updateAuthRole(Map<String, String> param);

	public List<Map<String, Object>> getAccessUrl(Map<String, Object> param);


}