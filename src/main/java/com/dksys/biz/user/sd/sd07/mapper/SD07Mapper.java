package com.dksys.biz.user.sd.sd07.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SD07Mapper {

	Map<String, String> selectCloseInfo(Map<String, String> paramMap);

	int saveCloseInfo(Map<String, String> paramMap);

}