package com.dksys.biz.user.sd.sd04.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface SD04Svc {

	int selectOrderCount(Map<String, String> paramMap);

	List<Map<String, Object>> selectOrderList(Map<String, String> paramMap);

	Map<String, Object> selectOrderInfo(Map<String, String> paramMap);
	
	int selectFixedOrderCount(Map<String, String> paramMap);
	
	void insertOrder(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);
	
	void updateOrder(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);
	
	void deleteOrder(Map<String, String> paramMap);

	void closeOrder(List<Map<String, String>> paramMapList);

	String selectOdrSeq();

}