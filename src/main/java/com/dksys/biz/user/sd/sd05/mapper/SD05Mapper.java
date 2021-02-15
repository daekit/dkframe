package com.dksys.biz.user.sd.sd05.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SD05Mapper {
	
	
	List<Map<String, String>> selectProjectList(Map<String, String> param);
	
	List<Map<String, String>> selectProjectcoCdList(Map<String, String> param);
	
	List<Map<String, String>> selectProjectNameList(Map<String, String> param);
	
	List<Map<String, String>> selectProjectClntList(Map<String, String> param);
	
	Map<String, String> selectProjectKeyList(Map<String, String> param);
	
	int selectProjectCount(Map<String, String> param);
	
	Map<String, String> selectPrjInfo(Map<String, String> paramMap);
	
	List<Map<String, String>> selectOrdDetail(Map<String, String> paramMap);

	List<Map<String, String>> selectShipmentDetail(Map<String, String> paramMap);
	
	int insertProject(Map<String, String> paramMap);

	int updateProject(Map<String, String> param);
	
	int deleteProject(Map<String, String> paramMap);
}
