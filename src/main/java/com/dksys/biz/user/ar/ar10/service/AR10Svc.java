package com.dksys.biz.user.ar.ar10.service;

import java.util.List;
import java.util.Map;

public interface AR10Svc {
	int selectPchsSellCount(Map<String, String> paramMap);
	
	List<Map<String, Object>> selectPchsSellList(Map<String, String> param);

	int selectPchsSellSumCount(Map<String, String> paramMap);
	
	List<Map<String, Object>> selectPchsSellSumList(Map<String, String> param);
}