package com.dksys.biz.admin.bm.account.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper {
	
	int selectCodeCount(Map<String, String> param);
	
	List<Map<String, String>> selectCodeList(Map<String, String> param);

	int insertCode(Map<String, String> param);

	int deleteCode(Map<String, String> param);

	int updateCode(Map<String, String> param);
		
}
