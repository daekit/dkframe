package com.dksys.biz.user.fi.fi02.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FI02Mapper {

	int selectPalBillCount(Map<String, String> paramMap);

	List<Map<String, String>> selectPalBillList(Map<String, String> paramMap);

	List<Map<String, String>> selectBaseDept(Map<String, String> paramMap);

	List<Map<String, String>> selectUpperDept(Map<String, String> paramMap);

	List<Map<String, String>> selecrLastDept(Map<String, String> paramMap);

	Map<String, String> selectPalBillDetail(Map<String, String> paramMap);

	int selectPrdtDeptDuplicate(Map<String, String> paramMap);

	int insertPalBill(Map<String, String> paramMap);
	
	int updatePalBill(Map<String, String> paramMap);
	
	int deletePalBill(Map<String, String> paramMap);
	
	int savePalBill(Map<String, String> paramMap);
	
	int copyPrevMonth(Map<String, String> paramMap);

	List<Map<String, String>> selectPalBillSalesPrft(Map<String, String> paramMap);

	List<Map<String, String>> selectPalBillSalesPrftChart(Map<String, String> paramMap);

	List<Map<String, String>> selectPalBillBfrxPrft(Map<String, String> paramMap);

	List<Map<String, String>> selectPalBillBfrxPrftChart(Map<String, String> paramMap);
}