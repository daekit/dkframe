package com.dksys.biz.user.ar.ar06.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR06Mapper {
	
	int selectTaxInvoiceCount(Map<String, String> paramMap);

	List<Map<String, String>> selectTaxInvoiceList(Map<String, String> paramMap);
	
	Map<String, String> selectTaxInvoiceInfo(Map<String, String> paramMap);
	
	int insertTaxInvoice(Map<String, String> param);

	int updateTaxInvoice(Map<String, String> param);
	
	int deleteTaxInvoice(Map<String, String> param);

	List<Map<String, String>> selectTaxRcvInfo(Map<String, String> paramMap);
	
	int insertTaxRcvInfo(Map<String, String> param);
	
	int updateTaxRcvInfoUseY(Map<String, String> param);
	
}
