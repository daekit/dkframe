package com.dksys.biz.user.fi.douzone.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DouzoneMapper {
;

	int insertAutodocuSimple(Map<String, String> paramMap);
	
	int updateAutodocuSimple(Map<String, String> paramMap);
	
	int deleteAutodocuSimple(Map<String, String> paramMap);
}