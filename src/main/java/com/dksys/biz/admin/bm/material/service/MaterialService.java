package com.dksys.biz.admin.bm.material.service;

import java.util.List;
import java.util.Map;

public interface MaterialService {

    public List<Map<String, String>> selectMaterialList(Map<String, String> param);

	public int deleteMaterial(Map<String, String> param);

	public int insertMaterial(Map<String, String> param);

	public int checkOverLap(Map<String, String> param);

	public Map<String, String> selectMaterialInfo(Map<String, String> param);

	public int selectMaterialCount(Map<String, String> param);


}