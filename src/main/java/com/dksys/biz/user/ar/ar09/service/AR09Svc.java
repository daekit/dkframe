package com.dksys.biz.user.ar.ar09.service;

import java.util.List;
import java.util.Map;

public interface AR09Svc {

	int selectYySalesListCount(Map<String, String> param);

	List<Map<String, String>> selectYySalesList(Map<String, String> param);

}