package com.dksys.biz.user.sm.sm06.service;

import java.util.List;
import java.util.Map;

public interface SM06Svc {

	public int selectStockTotalListCount(Map<String, String> param);

	public List<Map<String, String>> selectStockTotalList(Map<String, String> param);	

	public int selectStockDetailListCount(Map<String, String> param);

	public List<Map<String, String>> selectStockDetailList(Map<String, String> param);	
    
}