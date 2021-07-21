package com.dksys.biz.admin.bm.bm02.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface BM02Svc {

	int selectClntCount(Map<String, String> paramMap);

	List<Map<String, String>> selectClntList(Map<String, String> paramMap);

	Map<String, Object> selectClntInfo(Map<String, String> paramMap);
	
	void insertClnt(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);
	
	void updateClnt(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);
	
	void unuseClnt(Map<String, String> paramMap);

	Map<String, String> selectMngInfo(Map<String, String> paramMap);
	
	Map<String, String> selectCrnDupChk(Map<String, String> paramMap);

	void deleteClntPldg(Map<String, Object> paramMap);

	void deleteClntBizdept(Map<String, Object> paramMap);

}