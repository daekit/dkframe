package com.dksys.biz.main.service;

import java.util.Map;

import com.dksys.biz.main.vo.User;

public interface LoginService {

	User selectUserInfo(Map<String, String> param);

	int insertUserHistory(User user);

}