package com.dksys.biz.user.service;

import java.util.Map;

import com.dksys.biz.user.vo.User;

public interface LoginService {

	User selectUserInfo(Map<String, String> param);

}