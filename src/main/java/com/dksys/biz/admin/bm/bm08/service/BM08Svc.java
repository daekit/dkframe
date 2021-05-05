package com.dksys.biz.admin.bm.bm08.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface BM08Svc {

	int selectClntPledgeCount(Map<String, String> paramMap);

	List<Map<String, String>> selectClntPledgeList(Map<String, String> paramMap);

	int selectClntPledgeDetailCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectClntPledgeDetailList(Map<String, String> paramMap);

	// 답보 이력 추가
	int selectClntPldgHistoryCount(Map<String, String> paramMap);

	List<Map<String, String>> selectClntPldgHistoryList(Map<String, String> paramMap);

	

}