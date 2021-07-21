package com.dksys.biz.admin.bm.bm02.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BM02Mapper {
	
	int selectClntCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectClntList(Map<String, String> paramMap);

	Map<String, Object> selectClntInfo(Map<String, String> paramMap);
	
	int insertClnt(Map<String, String> paramMap);
	
	int updateClnt(Map<String, String> paramMap);
	
	int unuseClnt(Map<String, String> paramMap);
	
	int unuseBizdept(Map<String, String> paramMap);
	
	int deleteBizdept(Map<String, String> paramMap);
	
	int insertBizdept(Map<String, String> paramMap);

	int updateBizdept(Map<String, String> paramMap);

	int unusePldg(Map<String, String> paramMap);
	
	int deletePldg(Map<String, String> paramMap);

	int insertPldg(Map<String, String> paramMap);
	
	int updatePldg(Map<String, String> paramMap);

	Map<String, String> selectMngInfo(Map<String, String> paramMap);
	
	// 동일 사업자 번호 체크
	Map<String, String> selectCrnDupChk(Map<String, String> paramMap);


}
