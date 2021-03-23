package com.dksys.biz.user.sd.sd07.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SD07Mapper {

	Map<String, String> selectClose(Map<String, String> paramMap);

	int saveClose(Map<String, String> paramMap);

	int deleteClose(Map<String, String> paramMap);
	
	int insertClose(Map<String, String> paramMap);

}