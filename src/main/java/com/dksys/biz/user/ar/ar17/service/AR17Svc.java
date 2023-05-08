package com.dksys.biz.user.ar.ar17.service;

import java.util.List;
import java.util.Map;

public interface AR17Svc {

	int selectTrmendAmtListCount(Map<String, String> paramMap);

	List<Map<String, String>> selectTrmendAmtList(Map<String, String> paramMap);

	

	
}
