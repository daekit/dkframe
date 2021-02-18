package com.dksys.biz.user.sd.sd03.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Mapper
public interface SD03Mapper {
	
	List<Map<String, String>> selectPrdtCodeList(Map<String, String> param);
	
	List<Map<String, String>> selectPrdtCd(Map<String, String> param);
	
	int selectUprCount(Map<String, String> param);
	
	List<Map<String, String>> selectMainEstList(Map<String, String> param);
	
	Map<String, String> selectEstInfo(Map<String, String> paramMap);
	
	int insertEst(Map<String, String> paramMap);
	
	int insertEstDetail(Map<String, String> detailMap);
	
	int updateEst(Map<String, String> paramMap);

	int updateEstDetail(Map<String, String> estMap);
	
	
	//int deleteEstDetail(Map<String, String> paramMap);
	//int deleteEst(Map<String, String> paramMap);
}
