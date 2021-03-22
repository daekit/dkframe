package com.dksys.biz.user.sd.sd07.service;

import java.util.Map;

public interface SD07Svc {

	Map<String, String> selectCloseInfo(Map<String, String> paramMap);

	void saveCloseInfo(Map<String, String> paramMap);

}