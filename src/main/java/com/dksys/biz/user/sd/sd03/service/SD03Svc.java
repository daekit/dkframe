package com.dksys.biz.user.sd.sd03.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface SD03Svc {
	
	public int selectEstCount(Map<String, String> param);
	
	public List<Map<String, Object>> selectEstList(Map<String, String> param);
	
	Map<String, Object> selectEstInfo(Map<String, String> paramMap);
	
	void insertEst(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);

	int updateEst(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);
	
	int deleteEst(Map<String, String> paramMap);
}