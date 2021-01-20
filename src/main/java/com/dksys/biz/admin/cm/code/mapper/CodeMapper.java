package com.dksys.biz.admin.cm.code.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CodeMapper {
	
	int selectCodeCount(Map<String, String> param);
	
	List<Map<String, String>> selectCodeList(Map<String, String> param);

	List<Map<String, String>> selectChildCodeList(Map<String, String> param);

	Map<String, String> selectCodeInfo(Map<String, String> param);
	
	List<Map<String, String>> selectCodeTree();
	
	int insertCode(Map<String, String> param);

	int deleteCode(Map<String, String> param);

}
