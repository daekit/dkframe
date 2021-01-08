package com.dksys.biz.user.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.dksys.biz.user.vo.User;

@Mapper
public interface LoginMapper {

	User selectUserInfo(Map<String, String> param);

}
