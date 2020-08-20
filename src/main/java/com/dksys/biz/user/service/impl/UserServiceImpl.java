package com.dksys.biz.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.mapper.UserMapper;
import com.dksys.biz.user.service.UserService;
import com.dksys.biz.user.vo.User;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
    @Autowired
    UserMapper userMapper;

    public List<User> selectAllUser() {
        return userMapper.selectAllUser();
    }

	@Override
	public void selectOneUser() {
		System.out.println("ㅎㅇ");
	}

}