package com.dksys.biz.user.ar.ar02.service;

import java.util.List;
import java.util.Map;

public interface AR02Svc {

	int selectSellCount(Map<String, String> paramMap);

	List<Map<String, String>> selectSellList(Map<String, String> paramMap);

	int updatePchsSell(Map<String, Object> paramMap);

	int deleteSell(Map<String, String> paramMap);

}