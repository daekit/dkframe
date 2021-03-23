package com.dksys.biz.user.sd.sd07.service;

import java.util.Map;

public interface SD07Svc {

	Map<String, String> selectClose(Map<String, String> paramMap);

	void saveClose(Map<String, String> paramMap);

	void excuteClose(Map<String, String> paramMap);

}