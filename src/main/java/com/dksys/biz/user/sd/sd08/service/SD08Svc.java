package com.dksys.biz.user.sd.sd08.service;

import java.util.List;
import java.util.Map;

public interface SD08Svc {

	public List<Map<String, String>> selectCplrUntpcList(Map<String, String> param);
	
	public Map<String, String> selectCplrUntpc(Map<String, String> param);
	
	public int selectCplrUntpcCount(Map<String, String> param);
	
	public int selectCplrUntpcKey(Map<String, String> param);
	
	public int insertCplrUntpc(Map<String, String> param);

	public int updateCplrUntpc(Map<String, String> param);
	
	public int deleteCplrUntpc(Map<String, String> param);
	
}