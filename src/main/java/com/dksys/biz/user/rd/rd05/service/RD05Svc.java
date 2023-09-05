package com.dksys.biz.user.rd.rd05.service;

import java.util.List;
import java.util.Map;

public interface RD05Svc {

	public List<Map<String, String>> selectRcvpayDailyList(Map<String, String> param);
	
	public int selectRcvpayDailyCount(Map<String, String> param);

	public int selectRcvpayDailyOnlyPrdtCount(Map<String, String> param);
	
	public List<Map<String, String>> selectRcvpayDailyListOnlyPrdt(Map<String, String> param);
	
	public List<Map<String, String>> selectRcvpayDailyDtlList(Map<String, String> param);
	
	public int selectRcvpayDailyDtlCount(Map<String, String> param);

	

	
	
}