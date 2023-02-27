package com.dksys.biz.user.ar.ar15.service;

import java.util.List;
import java.util.Map;

public interface AR15Svc {

	int selectSalesMngYyListCount(Map<String, String> param);
	
	List<Map<String, String>> selectSalesMngYyList(Map<String, String> param);
}
