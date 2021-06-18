package com.dksys.biz.user.ar.ar12.service;

import java.util.List;
import java.util.Map;

public interface AR12Svc {
	int SelectPtnCreditCount(Map<String, String> paramMap);
	
	List<Map<String, Object>> SelectPtnCreditList(Map<String, String> param);
}