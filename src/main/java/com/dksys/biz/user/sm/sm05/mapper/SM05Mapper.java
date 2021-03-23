package com.dksys.biz.user.sm.sm05.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SM05Mapper {
	
	int selectPchsAggrCount(Map<String, String> paramMap);

	List<Map<String, String>> selectPchsAggrList(Map<String, String> paramMap);

	List<Map<String, String>> bilgCertNoList(Map<String, String> paramMap);

	List<Map<String, String>> bilgCertNullNoList(Map<String, String> paramMap);
	
}
