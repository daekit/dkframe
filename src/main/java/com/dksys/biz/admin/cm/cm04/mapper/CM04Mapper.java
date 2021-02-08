package com.dksys.biz.admin.cm.cm04.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CM04Mapper {
	
	List<Map<String, String>> selectDeptTree(Map<String, String> paramMap);

	Map<String, String> selectDeptInfo(Map<String, String> paramMap);

	int selectDeptCount(Map<String, String> paramMap);

	int updateDept(Map<String, String> paramMap);

	int moveDept(Map<String, String> paramMap);
	
}
