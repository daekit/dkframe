package com.dksys.biz.user.ar.ar06.service;

import java.util.List;
import java.util.Map;

public interface AR06Svc {

	int selectTaxInvoiceCount(Map<String, String> param);

	List<Map<String, String>> selectTaxInvoiceList(Map<String, String> param);

	Map<String, String> selectTaxInvoiceInfo(Map<String, String> paramMap);
	
	int insertTaxInvoice(Map<String, String> param);

	int updateTaxInvoice(Map<String, String> param);

	int deleteTaxInvoice(Map<String, String> param);


}