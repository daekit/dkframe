package com.dksys.biz.user.ar.ar04.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR04Mapper {
	
	int insertBilg(Map<String, String> bilgInfo);

	int getBilgCertNo();
	
	int selectTaxBilgCount(Map<String, String> paramMap);
	
	int selectTaxBilgDetailCount(Map<String, String> paramMap);

	List<Map<String, String>> selectTaxBilgList(Map<String, String> paramMap);

	List<Map<String, String>> selectTaxBilgDetailList(Map<String, String> paramMap);

	Map<String, String> selectTaxBilg(Map<String, String> paramMap);

	int updateTaxBilg(Map<String, String> paramMap);

	int selectTaxRcvCount(Map<String, String> paramMap);

	List<Map<String, String>> selectTaxRcvList(Map<String, String> paramMap);
	
	String selectMsgId(int msgId);
	
	String selectBgmSeq();

	int insertTaxHd(Map<String, String> bilgInfo);
	
	int updateTaxBilgNo(Map<String, String> bilgCertNo);
	
	List<Map<String, String>> selectSellList(Map<String, String> paramMap);
	
	int insertTaxItem(Map<String, String> paramMap);

	int deleteBilgInfo(Map<String, String> param);
}