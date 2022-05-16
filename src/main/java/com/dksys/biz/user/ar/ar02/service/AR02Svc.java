package com.dksys.biz.user.ar.ar02.service;

import java.util.List;
import java.util.Map;

public interface AR02Svc {
	
	int selectSellMainCount(Map<String, String> paramMap);

	List<Map<String, String>> selectSellMainList(Map<String, String> paramMap);
	
	int selectSellCount(Map<String, String> paramMap);

	List<Map<String, String>> selectSellList(Map<String, String> paramMap);
	
	int selectSellPchSumCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectSellPchSumList(Map<String, String> paramMap);
	
	List<Map<String, String>> selectSellSumList(Map<String, String> paramMap);
	
	Map<String, String> selectSellInfo(Map<String, String> paramMap);

	void updatePchsSell (Map<String, Object> paramMap) throws Exception;
	
	void updatePchsSellPart (Map<String, String> paramMap) throws Exception;

	void deleteSell(Map<String, String> paramMap) throws Exception;

	void insertPchsSell(Map<String, String> paramMap) throws Exception;
	
	void insertSalesDivision(List<Map<String, String>> paramList) throws Exception;
	
	void updateSalesClnt(List<Map<String, String>> paramList);

	boolean checkSellClose(Map<String, String> paramMap);
	
	boolean checkPchsClose(Map<String, String> paramMap);
	
	long checkLoan(Map<String, Object> paramMap);
	
	long deductLoan(Map<String, Object> paramMap);
	
	long depositLoan(Map<String, Object> paramMap);

	void insertDeletePchsSell(Map<String, String> paramMap) throws Exception;

	void checkClose(Map<String, String> paramMap) throws Exception;
	
}