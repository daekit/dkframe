package com.dksys.biz.user.rd.rd04.service;

import java.util.List;
import java.util.Map;

public interface RD04Svc {

	public List<Map<String, String>> selectRcvpayList(Map<String, String> param);
	
	public int selectRcvpayCount(Map<String, String> param);

	public List<Map<String, String>> selectRcvpayDtlList(Map<String, String> param);
	
	public int selectRcvpayDtlCount(Map<String, String> param);
	
}