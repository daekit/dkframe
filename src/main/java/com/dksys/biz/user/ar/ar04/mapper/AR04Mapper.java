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

	List<Map<String, String>> selectBilgDetailList(Map<String, String> paramMap);

	Map<String, String> selectTaxBilg(Map<String, String> paramMap);

	int updateTaxBilg(Map<String, String> paramMap);

	int deleteTaxBilgDetail(Map<String, String> paramMap);

	int insertTaxBilgDetail(Map<String, String> paramMap);

	int selectTaxRcvCount(Map<String, String> paramMap);

	List<Map<String, String>> selectTaxRcvList(Map<String, String> paramMap);
	
	String selectMsgId(int msgId);
	
	String selectBgmSeq();

	int insertMapoutKey(Map<String, String> bilgInfo);

	int insertTaxHd(Map<String, String> bilgInfo);
	
	int insertTaxHdReSend(Map<String, String> bilgInfo);

	int updateTaxBilgNo(Map<String, String> bilgCertNo);
	
	List<Map<String, String>> selectSellList(Map<String, String> paramMap);
	
	int insertTaxDtl(Map<String, String> paramMap);
	
	int insertTaxItem(Map<String, String> paramMap);

	int deleteBilgInfo(Map<String, String> param);

	int insertInvHd(Map<String, String> bilgInfo);

	int insertInvDtl(Map<String, String> bilgInfo);

	int insertInvDtlUpdate(Map<String, String> bilgInfo);

	int insertItem(Map<String, String> bilgInfo);

	int insertItemMinus(Map<String, String> bilgInfo);
	
	Map<String, String> selectBilgInfo(Map<String, String> paramMap);

	int insertTaxHdCancel(Map<String, String> bilgInfo);

	int insertInvHdCancel(Map<String, String> bilgInfo);

	int insertTaxItemCancel(Map<String, String> bilgInfo);

	int insertInvItemCancel(Map<String, String> bilgInfo);

	int updateTaxBilgNoCancel(Map<String, String> bilgInfo);

	int updateBilgRvrs(Map<String, String> bilgInfo);
	
	int updateBilgRvrsCancel(Map<String, String> bilgInfo);

	String selectBgm4343(String taxBilgNo);

	int insertTaxHdDelete(Map<String, String> bilgInfo);

	int insertTaxItemDelete(Map<String, String> bilgInfo);

	int insertInvHdDelete(Map<String, String> bilgInfo);

	int insertInvItemDelete(Map<String, String> bilgInfo);

	int updateTaxBilgNoDelete(Map<String, String> bilgInfo);

	int insertBilgDetail(Map<String, String> bilgInfo);

	int updateBilgAmt(Map<String, String> bilgParam);

	int insertKladdiHd(Map<String, String> bilgInfo);

	int insertKladdiDtl(Map<String, String> bilgInfo);
	
	int insertCopyBilgTax(Map<String, String> bilgInfo);
	
	int insertCopyTaxBilgDetail(Map<String, String> bilgInfo);
	
	int updateTrstInfo(Map<String, String> bilgInfo);
	
	int updateOrgnTaxBilgNo(Map<String, String> bilgInfo);

	int updateNote(Map<String, String> param);

	int updateNoteData(Map<String, String> param);

	int updateNoteData2(Map<String, String> param);

	void updateTaxHd(Map<String, String> taxHdParam);

	void updateAR02BilgCertNo(Map<String, Object> paramMap);

	void deleteTaxHd(Map<String, Object> paramMap);

	Map<String, Object> selectTaxBilgDetail(String string);

}