package com.dksys.biz.admin.level.service;

import java.util.List;
import java.util.Map;

public interface LevelService {

	public List<Map<String, String>> selectLevelList();

	public int selectLevelCount(Map<String, String> paramMap);

	public void insertLevel(Map<String, String> paramMap);

	public Map<String, String> selectLevelInfo(Map<String, String> paramMap);

	public void updateLevel(Map<String, String> paramMap);

}