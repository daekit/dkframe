package com.dksys.biz.admin.bm.bm02.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BM02Mapper {
	int insertClnt(Map<String, String> paramMap);
	
	int updateClnt(Map<String, String> paramMap);

	int selectClntCount(Map<String, String> param);

	List<Map<String, String>> selectClntList(Map<String, String> param);

	Map<String, String> selectClntInfo(Map<String, String> param);

}
