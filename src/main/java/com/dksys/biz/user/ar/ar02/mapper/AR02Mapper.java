package com.dksys.biz.user.ar.ar02.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR02Mapper {
	
	int selectSellMainCount(Map<String, String> paramMap);

	List<Map<String, String>> selectSellMainList(Map<String, String> paramMap);
	
	int selectSellCount(Map<String, String> paramMap);

	List<Map<String, String>> selectSellList(Map<String, String> paramMap);
	
	int selectSellPchSumCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectSellPchSumList(Map<String, String> paramMap);
	
	List<Map<String, String>> selectSellSumList(Map<String, String> paramMap);
	
	int selectSellSaleCount(Map<String, String> paramMap);

	List<Map<String, String>> selectSellSaleList(Map<String, String> paramMap);
	
	List<Map<String, String>> checkBilg(Map<String, String> detailMap);
	
	Map<String, String> selectSellInfo(Map<String, String> paramMap);

	Map<String, String> selectBilgInfo(Map<String, Object> param);
	
	Map<String, String> selectBilgInfoUpdate(Map<String, String> paramMap);
	
	String selectOwner1ClntCd(Map<String, String> paramMap);
	
	long callCreditLoan(Map<String, Object> map);
	
	long callCreditLoan2(Map<String, Object> map);
	
	long callCreditNonPayAmt(Map<String, Object> map);
	
	long callCreditNonRecvAmt(Map<String, Object> map);
	
	long callCreditUnsetlBilAmt(Map<String, Object> map);
	
	long checkPldgAmt(Map<String, Object> map);
	
	long selectBilgVatAmt(Map<String, String> paramMap);
	
	int selectBilgVatPer(Map<String, String> paramMap);
	
	int insertPchsSell(Map<String, String> paramMap);

	int updatePchsSell(Map<String, String> detailMap);

	int updatePchsSellBilg(Map<String, String> sellParam);
	
	int updateBilgCancel(Map<String, String> param);
	
	int updateSalesClnt(Map<String, String> paramMap);
	
	int updateSalesMng(Map<String, String> trspMap);
	
	int updateTrspRmk(Map<String, String> paramMap);
	
	int deleteSell(Map<String, String> paramMap);
	
	int deletePchsSell(Map<String, String> detailMap);

	void updatePchsSellPart(Map<String, String> paramMap);
	
	int updateSelectedRows(Map<String, String> param);

	void callSaleMatch(Map<String, String> paramMap);

	int countSell(Map<String, String> paramMap);

	List<Map<String, Object>> selectTrstCertiNo(Map<String, String> paramMap);

	List<Map<String, String>> countSellList(Map<String, String> paramMap);

	int countSellFind(Map<String, String> paramMap);

	void deleteSellReal(Map<String, String> paramMap);

	void deleteSell05D(Map<String, String> detailMap);

	void updatePchsSellPart2(Map<String, String> detailMap);

}