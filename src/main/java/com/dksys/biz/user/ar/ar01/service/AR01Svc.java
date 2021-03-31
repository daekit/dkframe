package com.dksys.biz.user.ar.ar01.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface AR01Svc {

	int insertShip(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);

	int selectShipCount(Map<String, String> paramMap);

	List<Map<String, String>> selectShipList(Map<String, String> paramMap);

	int deleteShip(Map<String, String> paramMap);

	Map<String, Object> selectShipInfo(Map<String, String> paramMap);

	Map<String, Object> getOrderInfo(Map<String, Object> paramMap);
	
	int updateShip(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);

	int updateConfirm(Map<String, String> paramMap);

	int selectConfirmCount(Map<String, String> paramMap);
	
	int selectDetailCount(Map<String, String> paramMap);

	int updateCancel(Map<String, String> paramMap);

}