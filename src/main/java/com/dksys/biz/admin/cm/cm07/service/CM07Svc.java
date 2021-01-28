package com.dksys.biz.admin.cm.cm07.service;

import java.util.List;
import java.util.Map;

public interface CM07Svc {

	public List<Map<String, String>> selectLevelList(Map<String, String> paramMap);

	public int selectLevelCount(Map<String, String> paramMap);

	public void insertLevel(Map<String, String> paramMap) throws Exception;

	public Map<String, String> selectLevelInfo(Map<String, String> paramMap);

	public void updateLevel(Map<String, String> paramMap) throws Exception;

}