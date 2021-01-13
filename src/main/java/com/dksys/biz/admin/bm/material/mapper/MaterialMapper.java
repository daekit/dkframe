package com.dksys.biz.admin.bm.material.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MaterialMapper {
	
	List<Map<String, String>> selectMaterialList();

}
