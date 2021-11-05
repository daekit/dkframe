package com.dksys.biz.admin.bm.bm09.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BM09Mapper {
	
	int selectPledgeCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectPledgeList(Map<String, String> paramMap);

	int selectPledgeDetailCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectPledgeDetailList(Map<String, String> paramMap);

	// 답보이력추가
	int selectPldgHistoryCount(Map<String, String> paramMap);

	List<Map<String, String>> selectPldgHistoryList(Map<String, String> paramMap);

	

}
