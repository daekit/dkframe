package com.dksys.biz.user.ar.ar04.service;

import java.util.List;
import java.util.Map;

public interface AR04Svc {

	public void insertBilg(Map<String, Object> paramMap) throws Exception;
	
	public List<Map<String, String>> selectTaxBilgList(Map<String, String> param);
	
	public List<Map<String, String>> selectTaxBilgDetailList(Map<String, String> param);
	
	public int selectTaxBilgCount(Map<String, String> param);	
	
	public int selectTaxBilgDetailCount(Map<String, String> param);	
	
	public Map<String, String> selectTaxBilg(Map<String, String> param);

	public int updateTaxBilg(Map<String, String> param);

	public List<Map<String, String>> selectTaxRcvList(Map<String, String> param);
	
	public int selectTaxRcvCount(Map<String, String> param);	

	int insertTaxHd(Map<String, Object> paramMap);

	int insertTaxHdUpdate(Map<String, Object> paramMap);

	int insertTaxHdReSend(Map<String, Object> paramMap);

	int insertTaxHdCancel(Map<String, Object> paramMap);

	int updateBilgCancel(Map<String, Object> paramMap);

	int updateBilgRvrs(Map<String, Object> paramMap);

	int updateBilgRvrsCancel(Map<String, Object> paramMap);
	
	public List<Map<String, String>> selectBilgDetailList(Map<String, String> param);

	int updateBilg(Map<String, Object> paramMap);

	int updateNote(Map<String, Object> paramMap);

	public void deleteTaxHd(Map<String, Object> paramMap);
}