package com.dksys.biz.user.fi.fi01.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FI01Mapper {

	int selectPrftDeptCount(Map<String, String> paramMap);

	List<Map<String, String>> selectPrftDeptList(Map<String, String> paramMap);

	List<Map<String, String>> selectUpperDept(Map<String, String> paramMap);

	List<Map<String, String>> selecrLastDept(Map<String, String> paramMap);

	Map<String, String> selectPrftDeptDetail(Map<String, String> paramMap);

	int selectPrdtDeptDuplicate(Map<String, String> paramMap);

	int insertPrftDept(Map<String, String> paramMap);
	
	int updatePrftDept(Map<String, String> paramMap);
	
	int deletePrftDept(Map<String, String> paramMap);
}