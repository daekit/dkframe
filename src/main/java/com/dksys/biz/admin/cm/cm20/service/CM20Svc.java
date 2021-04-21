package com.dksys.biz.admin.cm.cm20.service;

import java.util.List;
import java.util.Map;

public interface CM20Svc {


	public List<Map<String, String>> selectSanctnList(Map<String, String> paramMap);

	public int selectSanctnCount(Map<String, String> paramMap);
	
	public Map<String, String> selectSanctnInfo(Map<String, String> paramMap);

	public int deleteSanctnInfo(Map<String, String> paramMap);

	public int insertSanctnInfo(Map<String, Object> paramMap);

	public int updateSanctnInfo(Map<String, String> paramMap);
	
}