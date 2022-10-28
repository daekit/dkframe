package com.dksys.biz.user.ar.ar14.service;

import java.util.List;
import java.util.Map;

public interface AR14Svc {

	int selectDebtCount(Map<String, String> paramMap);

	List<Map<String, String>> selectDebtList(Map<String, String> paramMap);

	void insertDebt(Map<String, String> paramMap);

	int selectDebtGroupCount(Map<String, String> paramMap);

	List<Map<String, String>> selectDebtGroupList(Map<String, String> paramMap);

	void deleteDebt(Map<String, String> paramMap);

	
}