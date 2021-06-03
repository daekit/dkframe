package com.dksys.biz.admin.cm.cm11.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CM11Mapper {
	
	String selectSearchDttm();
	
	int selectPrdtSelpch2Count(Map<String, String> paramMap);

	List<Map<String, String>> selectPrdtSelpch2List(Map<String, String> paramMap);
	
	int selectClntSelpch2Count(Map<String, String> paramMap);

	List<Map<String, String>> selectClntSelpch2List(Map<String, String> paramMap);
	
	int selectClntSelpch1Count(Map<String, String> paramMap);

	List<Map<String, String>> selectClntSelpch1List(Map<String, String> paramMap);

	List<Map<String, String>> selectClntSelpch1List2(Map<String, String> paramMap);

	List<Map<String, String>> selectFacList(Map<String, String> paramMap);

	List<Map<String, String>> selectMonthlyStat(Map<String, String> paramMap);

}