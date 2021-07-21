package com.dksys.biz.admin.cm.cm08.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CM08Mapper {

	int insertFile(HashMap<String, String> param);

	List<Map<String, String>> selectFileList(Map<String, String> paramMap);

	Map<String, String> selectFileInfo(String fileKey);

	int deleteFileInfo(String fileKey);
	
	
}