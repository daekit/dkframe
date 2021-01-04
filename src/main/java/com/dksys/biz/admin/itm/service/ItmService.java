package com.dksys.biz.admin.itm.service;

import java.util.List;
import java.util.Map;

public interface ItmService {

    public List<Map<String, String>> selectItmList();

	public int insertItm(Map<String, String> param);

	public int deleteItm(Map<String, String> param);

	public int updateItm(Map<String, String> param);

	public Map<String, String> selectItmInfo(Map<String, String> paramMap);

}