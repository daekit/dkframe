package com.dksys.biz.user.ar.ar11.service;

import java.util.List;
import java.util.Map;

public interface AR11Svc {
	int kweonCount(Map<String, String> paramMap);
	
	List<Map<String, Object>> kweonList(Map<String, String> param);
}