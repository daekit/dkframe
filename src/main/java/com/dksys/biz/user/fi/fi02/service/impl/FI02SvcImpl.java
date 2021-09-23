package com.dksys.biz.user.fi.fi02.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.fi.fi02.mapper.FI02Mapper;
import com.dksys.biz.user.fi.fi02.service.FI02Svc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional(rollbackFor = Exception.class)
public class FI02SvcImpl implements FI02Svc {
	
    @Autowired
    FI02Mapper fi02Mapper;
    
	@Override
	public int selectPalBillCount(Map<String, String> paramMap) {
		return fi02Mapper.selectPalBillCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectPalBillList(Map<String, String> paramMap) {
		return fi02Mapper.selectPalBillList(paramMap);
	}

	@Override
	public List<Map<String, String>> selectBaseDept(Map<String, String> paramMap) {
		return fi02Mapper.selectBaseDept(paramMap);
	}

	@Override
	public List<Map<String, String>> selectUpperDept(Map<String, String> paramMap) {
		return fi02Mapper.selectUpperDept(paramMap);
	}

	@Override
	public List<Map<String, String>> selecrLastDept(Map<String, String> paramMap) {
		return fi02Mapper.selecrLastDept(paramMap);
	}

	@Override
	public Map<String, String> selectPalBillDetail(Map<String, String> paramMap) {
		return fi02Mapper.selectPalBillDetail(paramMap);
	}
    
	@Override
	public int selectPrdtDeptDuplicate(Map<String, String> paramMap) {
		return fi02Mapper.selectPrdtDeptDuplicate(paramMap);
	}

	@Override
	public int insertPalBill(Map<String, String> paramMap) {
		return fi02Mapper.insertPalBill(paramMap);
	}

	@Override
	public int updatePalBill(Map<String, String> paramMap) {
		return fi02Mapper.updatePalBill(paramMap);
	}

	@Override
	public int deletePalBill(Map<String, String> paramMap) {
		int result = 0;
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> saveList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for (Map<String, String> saveMap : saveList) {
			result = fi02Mapper.deletePalBill(saveMap);
		}
		return result;
	}

	@Override
	public int savePalBill(Map<String, String> paramMap) {
		int result = 0;
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> saveList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for (Map<String, String> saveMap : saveList) {
			saveMap.put("userId", paramMap.get("userId"));
			saveMap.put("pgmId", paramMap.get("pgmId"));
			result = fi02Mapper.savePalBill(saveMap);
		}
		return result;
	}

	@Override
	public int copyPrevMonth(Map<String, String> paramMap) {
		fi02Mapper.deletePalBill(paramMap);
		return fi02Mapper.copyPrevMonth(paramMap);
	}


	@Override
	public List<Map<String, String>> selectPalBillSalesPrft(Map<String, String> paramMap) {
		return fi02Mapper.selectPalBillSalesPrft(paramMap);
	}

	@Override
	public List<Map<String, String>> selectPalBillSalesPrftChart(Map<String, String> paramMap) {
		return fi02Mapper.selectPalBillSalesPrftChart(paramMap);
	}

	@Override
	public List<Map<String, String>> selectPalBillBfrxPrft(Map<String, String> paramMap) {
		return fi02Mapper.selectPalBillBfrxPrft(paramMap);
	}

	@Override
	public List<Map<String, String>> selectPalBillBfrxPrftChart(Map<String, String> paramMap) {
		return fi02Mapper.selectPalBillBfrxPrftChart(paramMap);
	}
}