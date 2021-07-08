package com.dksys.biz.user.sm.sm08.service;

import java.util.List;
import java.util.Map;

public interface SM08Svc {

	public int selectRcvpayTnKeyDailyListCount(Map<String, String> param);
	
	public List<Map<String, String>> selectRcvpayTnKeyDailyList(Map<String, String> param);	
    
}