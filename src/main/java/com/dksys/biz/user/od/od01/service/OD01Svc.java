package com.dksys.biz.user.od.od01.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface OD01Svc {

	int selectOrdrgCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectOrdrgList(Map<String, String> paramMap);
	
	Map<String, Object> selectOrdrgInfo(Map<String, String> paramMap);
	
	int selectOrdrgDetailCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectOrdrgDetailList(Map<String, String> paramMap);
	
	Map<String, Object> getOrderInfo(Map<String, Object> paramMap);
	
	int selectConfirmCount(Map<String, String> paramMap);
	
	int insertOrdrg(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);
	
	int updateOrdrg(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);
	
	void updateSalesMng(Map<String, String> paramMap);
	
	void updateOrdrgRmk(Map<String, String> paramMap);
	
	int deleteOrdrg(Map<String, String> paramMap);
	
	void updateConfirm(Map<String, String> paramMap) throws Exception;

	void updateCancel(Map<String, String> paramMap) throws Exception;

	int updateOrdrgFile(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);
	
}