package com.dksys.biz.user.service;

import java.util.List;

import com.dksys.biz.user.vo.User;

public interface UserService {

    public List<User> selectAllUser();

	public void selectOneUser();

}