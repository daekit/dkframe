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
	
	void updateSalesMng(Map<String, String> paramMap);
	
	void updateShipRmk(Map<String, String> paramMap);

	void updateConfirm(Map<String, String> paramMap) throws Exception;
	
	// 여신체크 일괄변경으로 인한 주석처리 - 20210630
	// int updateConfirmToMes(Map<String, String> paramMap, List<Map<String, String>> detailList);

	int selectConfirmCount(Map<String, String> paramMap);
	
	int selectDetailCount(Map<String, String> paramMap);

	void updateCancel(Map<String, String> paramMap) throws Exception;

	int updateRecptYn(Map<String, Object> param);

}