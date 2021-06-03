package com.dksys.biz.admin.cm.cm11.service;

import java.util.List;
import java.util.Map;

public interface CM11Svc {
	
	public String selectSearchDttm();

	public List<Map<String, String>> selectPrdtSelpch2List(Map<String, String> param);
	
	public int selectPrdtSelpch2Count(Map<String, String> param);

	public List<Map<String, String>> selectClntSelpch2List(Map<String, String> param);
	
	public int selectClntSelpch2Count(Map<String, String> param);

	public List<Map<String, String>> selectClntSelpch1List(Map<String, String> param);

	public List<Map<String, String>> selectClntSelpch1List2(Map<String, String> param);
	
	public int selectClntSelpch1Count(Map<String, String> param);

	public List<Map<String, String>> selectFacList(Map<String, String> param);

	public List<Map<String, String>> selectMonthlyStat(Map<String, String> param);
	
}