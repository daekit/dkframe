package com.dksys.biz.user.sm.sm09.service;

import java.util.List;
import java.util.Map;

public interface SM09Svc {

	public int selectStockListCount(Map<String, String> param);

	public List<Map<String, String>> selectStockList(Map<String, String> param);

	public int selectStockHistoryListCount(Map<String, String> param);

	public List<Map<String, String>> selectStockHistoryList(Map<String, String> param);

	
    
}