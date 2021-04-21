package com.dksys.biz.admin.cm.cm20.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CM20Mapper {
	
	int selectSanctnCount(Map<String, String> paramMap);

	List<Map<String, String>> selectSanctnList(Map<String, String> paramMap);
	
	Map<String, String> selectSanctnInfo(Map<String, String> paramMap);
	
	int deleteSanctnInfo(Map<String, String> param);
		
	int insertSanctnInfo(Map<String, String> paramMap);

	int updateSanctnInfo(Map<String, String> paramMap);


}