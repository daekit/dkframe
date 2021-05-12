package com.dksys.biz.user.ar.ar02.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR02Mapper {

	int insertPchsSell(Map<String, String> paramMap);
	
	int selectSellMainCount(Map<String, String> paramMap);

	List<Map<String, String>> selectSellMainList(Map<String, String> paramMap);
	
	int selectSellCount(Map<String, String> paramMap);

	List<Map<String, String>> selectSellList(Map<String, String> paramMap);

	int updatePchsSell(Map<String, String> detailMap);

	int deleteSell(Map<String, String> paramMap);

	Map<String, String> selectSellInfo(Map<String, String> paramMap);

	Map<String, String> selectBilgInfo(Map<String, Object> param);

	int updatePchsSellBilg(Map<String, String> sellParam);
	
	int updateBilgCancel(Map<String, String> param);
	
	int updateSalesClnt(Map<String, String> paramMap);

	long callCreditLoan(Map<String, Object> map);

	List<Map<String, String>> selectSellSumList(Map<String, String> paramMap);

	int deletePchsSell(Map<String, String> detailMap);

	List<Map<String, String>> checkBilg(Map<String, String> detailMap);

	String selectOwner1ClntCd(Map<String, String> paramMap);

	long selectBilgVatAmt(Map<String, String> paramMap);

	Map<String, String> selectBilgInfoUpdate(Map<String, String> paramMap);

}