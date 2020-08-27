package com.dksys.biz.auth.service;

import java.util.List;
import java.util.Map;

public interface AuthService {

    public List<Map<String, String>> selectAuthList();

	public int insertAuth(Map<String, String> param);


}