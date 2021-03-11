package com.dksys.biz.user.rd.rd01.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RD01Mapper {

	void inOutPrdt(Map<String, String> paramMap);

}