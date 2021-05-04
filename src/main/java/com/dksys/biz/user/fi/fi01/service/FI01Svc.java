package com.dksys.biz.user.fi.fi01.service;

import java.util.List;
import java.util.Map;

public interface FI01Svc {
	
	public int selectPrftDeptCount(Map<String, String> param);

	public List<Map<String, String>> selectPrftDeptList(Map<String, String> param);

	public List<Map<String, String>> selectUpperDept(Map<String, String> param);

	public List<Map<String, String>> selecrLastDept(Map<String, String> param);
	
	public Map<String, String> selectPrftDeptDetail(Map<String, String> param);
	
	public int selectPrdtDeptDuplicate(Map<String, String> param);
	
	public int insertPrftDept(Map<String, String> param);

	public int updatePrftDept(Map<String, String> param);
	
	public int deletePrftDept(Map<String, String> param);	
}