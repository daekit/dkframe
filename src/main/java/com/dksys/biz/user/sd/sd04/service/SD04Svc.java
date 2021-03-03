package com.dksys.biz.user.sd.sd04.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface SD04Svc {

	int selectOrderCount(Map<String, String> paramMap);

	List<Map<String, String>> selectOrderList(Map<String, String> paramMap);

	Map<String, Object> selectOrderInfo(Map<String, String> paramMap);
	
	Map<String, Object> getOrderInfo(Map<String, Object> paramMap);
	
	void insertOrder(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);
	
	void updateOrder(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);
	
	void deleteOrder(Map<String, String> paramMap);

}