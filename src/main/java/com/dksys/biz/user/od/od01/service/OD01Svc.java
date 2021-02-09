package com.dksys.biz.user.od.od01.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface OD01Svc {

	int insertOrder(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);

	int selectOrderCount(Map<String, String> paramMap);

	List<Map<String, String>> selectOrderList(Map<String, String> paramMap);

	int deleteOrder(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);

	
}