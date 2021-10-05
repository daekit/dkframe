package com.dksys.biz.douzone.dz.dz01.service;

import java.util.List;
import java.util.Map;

public interface DZ01Svc {
	List<Map<String, String>> testSelect(Map<String, String> paramMap);

	void testMultiTransaction() throws Exception;
}