package com.dksys.biz.user.od.od05.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface OD05Svc {

	int selectSellSaleCount(Map<String, String> paramMap);

	List<Map<String, String>> selectSellSaleList(Map<String, String> paramMap);
	
}