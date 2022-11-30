package com.dksys.biz.user.ar.ar14.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR14Mapper {

	int selectDebtCount(Map<String, String> paramMap);

	List<Map<String, String>> selectDebtList(Map<String, String> paramMap);

	void insertDebt(Map<String, String> paramMap);

	int selectDebtGroupCount(Map<String, String> paramMap);

	List<Map<String, String>> selectDebtGroupList(Map<String, String> paramMap);

	void deleteDebt(Map<String, String> paramMap);

	Map<String, Object> selectDebtInfo(Map<String, String> paramMap);

	void updateDebt(Map<String, String> paramMap);

}