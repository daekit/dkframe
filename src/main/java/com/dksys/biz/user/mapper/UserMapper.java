package com.dksys.biz.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.dksys.biz.user.vo.User;

@Mapper
public interface UserMapper {
	
	List<User> selectAllUser();
	
}
