package com.dksys.biz.admin.cm.cm08.mapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CM08Mapper {

	int insertFile(HashMap<String, String> param);
	
	
}
