package com.dksys.biz.admin.cm.cm05.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CM05Mapper {
	
	int selectCodeCount(Map<String, String> param);
	
	List<Map<String, String>> selectCodeList(Map<String, String> param);

	int selectPdskCodeCount(Map<String, String> param);
	
	List<Map<String, String>> selectPdskCodeList(Map<String, String> param);

	List<Map<String, String>> selectChildCodeList(Map<String, String> param);

	Map<String, String> selectCodeInfo(Map<String, String> param);
	
	List<Map<String, String>> selectCodeInfoList(Map<String, String> param);
	
	int insertCode(Map<String, String> param);

	int deleteCode(Map<String, String> param);

}
