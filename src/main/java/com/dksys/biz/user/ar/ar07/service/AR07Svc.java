package com.dksys.biz.user.ar.ar07.service;

import java.util.List;
import java.util.Map;

public interface AR07Svc {

	public List<Map<String, String>> selectMtClosCditList(Map<String, String> param);
	
	public int selectMtClosCditCount(Map<String, String> param);
	
}