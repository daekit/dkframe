package com.dksys.biz.user.ar.ar08.service;

import java.util.List;
import java.util.Map;

public interface AR08Svc {

	int selectCreditCount(Map<String, String> param);

	List<Map<String, String>> selectCreditList(Map<String, String> param);

}