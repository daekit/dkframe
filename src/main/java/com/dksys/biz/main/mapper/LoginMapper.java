package com.dksys.biz.main.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.dksys.biz.main.vo.User;

@Mapper
public interface LoginMapper {

	User selectUserInfo(Map<String, String> param);

	int insertUserHistory(User user);

}
