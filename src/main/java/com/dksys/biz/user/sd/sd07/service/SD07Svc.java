package com.dksys.biz.user.sd.sd07.service;

import java.util.List;
import java.util.Map;

public interface SD07Svc {

	Map<String, String> selectClose(Map<String, String> paramMap);

	void saveClose(Map<String, String> paramMap);

	void excuteStockClose(Map<String, String> paramMap);

	void excuteCreditClose(Map<String, String> paramMap);

	void excuteCreditClosePur(Map<String, String> paramMap);
	
	List<Map<String, String>> selectCloseYmList(Map<String, String> paramMap);

	void excuteCreditDeleteClose(Map<String, String> paramMap);

	void excuteCreditDeleteClosePur(Map<String, String> paramMap);
	

}