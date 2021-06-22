package com.dksys.biz.user.sm.sm07.service;

import java.util.List;
import java.util.Map;

public interface SM07Svc {

	public int selectStockSummaryListCount(Map<String, String> param);

	public List<Map<String, String>> selectStockSummaryList(Map<String, String> param);	
	
	public int selectStockSummaryDetailListCount(Map<String, String> param);
	
	public List<Map<String, String>> selectStockSummaryDetailList(Map<String, String> param);	
    
	public String selectSearchDttm();

}