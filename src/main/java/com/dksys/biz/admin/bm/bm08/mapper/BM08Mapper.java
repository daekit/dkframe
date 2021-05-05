package com.dksys.biz.admin.bm.bm08.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BM08Mapper {
	
	int selectClntPledgeCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectClntPledgeList(Map<String, String> paramMap);

	int selectClntPledgeDetailCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectClntPledgeDetailList(Map<String, String> paramMap);

	// 답보이력추가
	int selectClntPldgHistoryCount(Map<String, String> paramMap);

	List<Map<String, String>> selectClntPldgHistoryList(Map<String, String> paramMap);

	

}
