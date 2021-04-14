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

	int insertMapoutKey(Map<String, String> bilgInfo);

	int insertTaxHd(Map<String, String> bilgInfo);
	
	int updateTaxBilgNo(Map<String, String> bilgCertNo);
	
	List<Map<String, String>> selectSellList(Map<String, String> paramMap);
	
	int insertTaxDtl(Map<String, String> paramMap);
	
	int insertTaxItem(Map<String, String> paramMap);

	int deleteBilgInfo(Map<String, String> param);

	int insertInvHd(Map<String, String> bilgInfo);

	int insertInvDtl(Map<String, String> bilgInfo);

	int insertItem(Map<String, String> bilgInfo);
	
	Map<String, String> selectBilgInfo(Map<String, String> paramMap);

	int insertTaxHdCancel(Map<String, String> bilgInfo);

	int insertInvHdCancel(Map<String, String> bilgInfo);

	int insertTaxItemCancel(Map<String, String> bilgInfo);

	int insertInvItemCancel(Map<String, String> bilgInfo);

	int updateTaxBilgNoCancel(Map<String, String> bilgInfo);

	int updateBilgRvrs(Map<String, String> bilgInfo);
	
	String selectBgm4343(String taxBilgNo);

	int insertTaxHdDelete(Map<String, String> bilgInfo);

	int insertTaxItemDelete(Map<String, String> bilgInfo);

	int insertInvHdDelete(Map<String, String> bilgInfo);

	int insertInvItemDelete(Map<String, String> bilgInfo);

	int updateTaxBilgNoDelete(Map<String, String> bilgInfo);

}