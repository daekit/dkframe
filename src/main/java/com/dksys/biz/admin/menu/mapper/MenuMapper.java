package com.dksys.biz.admin.menu.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper {
	
	List<Map<String, String>> selectMenuList();

	int insertMenu(Map<String, String> param);

	int deleteMenu(Map<String, String> param);

	int updateMenu(Map<String, String> param);

	int selectMenuCount(Map<String, String> param);
	
}
