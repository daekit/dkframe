package com.dksys.biz.user.ar.ar02.service;

import java.util.List;
import java.util.Map;

public interface AR02Svc {
	
	int selectSellMainCount(Map<String, String> paramMap);

	List<Map<String, String>> selectSellMainList(Map<String, String> paramMap);

	List<Map<String, String>> selectSellList(Map<String, String> paramMap);

	int updatePchsSell(Map<String, Object> paramMap);

	int deleteSell(Map<String, String> paramMap);

	int insertPchsSell(Map<String, String> paramMap);
	
	void insertSalesDivision(List<Map<String, String>> paramList);
	
	void updateSalesClnt(List<Map<String, String>> paramList);

	Map<String, String> selectSellInfo(Map<String, String> paramMap);
	
	boolean checkLoan(Map<String, String> paramMap);

	List<Map<String, String>> selectSellSumList(Map<String, String> paramMap);

	boolean creditDeposit(Map<String, Object> paramMap);
	
	boolean checkSellClose(Map<String, String> paramMap);
	
	boolean checkPchsClose(Map<String, String> paramMap);

}