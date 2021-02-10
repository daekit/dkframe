package com.dksys.biz.user.sm.sm03.service;

import java.util.List;
import java.util.Map;

public interface SM03Svc {

	public int selectStockListCount(Map<String, String> param);

	public List<Map<String, String>> selectStockList(Map<String, String> param);

}