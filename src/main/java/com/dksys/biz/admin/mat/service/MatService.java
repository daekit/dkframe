package com.dksys.biz.admin.mat.service;

import java.util.List;
import java.util.Map;

public interface MatService {

    public List<Map<String, String>> selectMatList();

	public int insertMat(Map<String, String> param);

	public int deleteMat(Map<String, String> param);

	public int updateMat(Map<String, String> param);

	public Map<String, String> selectMatInfo(Map<String, String> paramMap);

}