package com.dksys.biz.user.fi.fi02.service;

import java.util.List;
import java.util.Map;

public interface FI02Svc {
	
	public int selectPalBillCount(Map<String, String> param);

	public List<Map<String, String>> selectPalBillList(Map<String, String> param);

	public List<Map<String, String>> selectBaseDept(Map<String, String> param);

	public List<Map<String, String>> selectUpperDept(Map<String, String> param);

	public List<Map<String, String>> selecrLastDept(Map<String, String> param);
	
	public Map<String, String> selectPalBillDetail(Map<String, String> param);
	
	public int selectPrdtDeptDuplicate(Map<String, String> param);
	
	public int insertPalBill(Map<String, String> param);

	public int updatePalBill(Map<String, String> param);
	
	public int deletePalBill(Map<String, String> param);	
	
	public int savePalBill(Map<String, String> param);	
	
	public int copyPrevMonth(Map<String, String> param);	

	public List<Map<String, String>> selectPalBillSalesPrft(Map<String, String> param);

	public List<Map<String, String>> selectPalBillSalesPrftChart(Map<String, String> param);

	public List<Map<String, String>> selectPalBillBfrxPrft(Map<String, String> param);

	public List<Map<String, String>> selectPalBillBfrxPrftChart(Map<String, String> param);
}