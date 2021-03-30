package com.dksys.biz.admin.cm.cm09.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CM09Mapper {
	
	int insertNoti(Map<String, String> paramMap);

	List<Map<String, String>> selectNotiList(Map<String, String> paramMap);

	int selectNotiCount(Map<String, String> paramMap);

	Map<String, String> selectNotiInfo(Map<String, String> paramMap);

	int updateNoti(Map<String, String> paramMap);

	List<String> selectNotiPopList();
	
}
