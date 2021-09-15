package com.dksys.biz.admin.cm.cm10.service;

import java.util.List;
import java.util.Map;

public interface CM10Svc {

	public List<Map<String, String>> selectShipNList(Map<String, String> param);
	
	public int selectShipNCount(Map<String, String> param);

	public List<Map<String, String>> selectOrdrgNList(Map<String, String> param);
	
	public int selectOrdrgNCount(Map<String, String> param);

	public int selectReqNCount(Map<String, String> param);

	public List<Map<String, String>> selectReqNList(Map<String, String> param);
	
	public int selectTaxNCount(Map<String, String> param);
	
	public List<Map<String, String>> selectTaxNList(Map<String, String> param);
	
}