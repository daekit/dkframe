package com.dksys.biz.user.sm.sm01.service;

import java.util.List;
import java.util.Map;

public interface SM01Svc {

	public int selectStockListCount(Map<String, String> param);

	public List<Map<String, String>> selectStockList(Map<String, String> param);

	
    
}