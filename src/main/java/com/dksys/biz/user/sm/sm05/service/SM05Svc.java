package com.dksys.biz.user.sm.sm05.service;

import java.util.List;
import java.util.Map;

public interface SM05Svc {

	int selectPchsAggrCount(Map<String, String> param);

	List<Map<String, String>> selectPchsAggrList(Map<String, String> param);

	List<Map<String, String>> bilgCertNoList(Map<String, String> param);

	List<Map<String, String>> bilgCertNullNoList(Map<String, String> param);

}