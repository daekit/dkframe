package com.dksys.biz.admin.bm.bm02.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface BM02Svc {

	void insertClnt(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);

	int selectClntCount(Map<String, String> param);

	List<Map<String, String>> selectClntList(Map<String, String> param);

	Map<String, String> selectClntInfo(Map<String, String> param);

}