package com.dksys.biz.user.ar.ar01.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface AR01Svc {

	int insertShip(Map<String, String> paramMap, MultipartHttpServletRequest mRequest);

	int selectShipCount(Map<String, String> paramMap);

	List<Map<String, String>> selectShipList(Map<String, String> paramMap);

	int deleteShip(Map<String, String> paramMap);


	
}