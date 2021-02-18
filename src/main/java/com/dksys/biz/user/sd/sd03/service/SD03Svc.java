package com.dksys.biz.user.sd.sd03.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface SD03Svc {
	
	public List<Map<String, String>> selectPrdtCodeList(Map<String, String> param);

	public List<Map<String, String>> selectPrdtCd(Map<String, String> param);
	
	public int selectUprCount(Map<String, String> param);
	
	public List<Map<String, String>> selectMainEstList(Map<String, String> param);
	
	Map<String, String> selectEstInfo(Map<String, String> paramMap);
	
	void insertEst(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);

	void updateEst(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);
}