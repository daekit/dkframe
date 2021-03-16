package com.dksys.biz.user.ar.ar04.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;



@Mapper
public interface AR04Mapper {

	int insertBilg(Map<String, String> bilgInfo);

	int getBilgCertNo();
	
}