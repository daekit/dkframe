package com.dksys.biz.user.ar.ar04.service;

import java.util.List;
import java.util.Map;

public interface AR04Svc {

	int insertBilg(Map<String, Object> paramMap);
	
	public List<Map<String, String>> selectTaxBilgList(Map<String, String> param);
	
	public int selectTaxBilgCount(Map<String, String> param);	
	
	public Map<String, String> selectTaxBilg(Map<String, String> param);

	public int updateTaxBilg(Map<String, String> param);

	public List<Map<String, String>> selectTaxRcvList(Map<String, String> param);
	
	public int selectTaxRcvCount(Map<String, String> param);	

}