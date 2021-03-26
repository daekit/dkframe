package com.dksys.biz.user.sd.sd08.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SD08Mapper {

	int selectCplrUntpcCount(Map<String, String> paramMap);

	List<Map<String, String>> selectCplrUntpcList(Map<String, String> paramMap);

	Map<String, String> selectCplrUntpc(Map<String, String> paramMap);
	
	int selectCplrUntpcKey(Map<String, String> paramMap);

	int insertCplrUntpc(Map<String, String> paramMap);
	
	int updateCplrUntpc(Map<String, String> paramMap);
	
	int deleteCplrUntpc(Map<String, String> paramMap);

}