package com.dksys.biz.admin.bm.bm09.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface BM09Svc {

	int selectPledgeCount(Map<String, String> paramMap);

	List<Map<String, String>> selectPledgeList(Map<String, String> paramMap);

	int selectPledgeDetailCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectPledgeDetailList(Map<String, String> paramMap);

	// 답보 이력 추가
	int selectPldgHistoryCount(Map<String, String> paramMap);

	List<Map<String, String>> selectPldgHistoryList(Map<String, String> paramMap);

}