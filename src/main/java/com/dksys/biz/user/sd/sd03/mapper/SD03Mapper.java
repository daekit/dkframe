package com.dksys.biz.user.sd.sd03.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Mapper
public interface SD03Mapper {
	
	String selectEstNo();
	
	int selectEstCount(Map<String, String> param);
	
	List<Map<String, Object>> selectEstList(Map<String, String> param);
	
	Map<String, Object> selectEstInfo(Map<String, String> paramMap);
	
	int insertEst(Map<String, String> paramMap);
	
	int insertEstDetail(Map<String, String> detailMap);
	
	int updateEst(Map<String, String> paramMap);

	int deleteEstDetail(Map<String, String> paramMap);
	
	int deleteEst(Map<String, String> paramMap);
}
