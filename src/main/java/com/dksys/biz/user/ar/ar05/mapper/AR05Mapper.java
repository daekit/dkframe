package com.dksys.biz.user.ar.ar05.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AR05Mapper {
	
	int selectEtrdpsCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectEtrdpsList(Map<String, String> paramMap);

	Map<String, String> selectEtrdpsInfo(Map<String, String> paramMap);
	
	Map<String, String> selectBillInfo(Map<String, String> paramMap);
	
	List<Map<String, String>> selectBilgList(Map<String, String> paramMap);
	
	int insertEtrdps(Map<String, String> paramMap);
	
	int insertBill(Map<String, String> paramMap);
	
	int callCreditLoan(Map<String, Object> paramMap);
	
	int updateEtrdps(Map<String, String> paramMap);
	
	int updateBill(Map<String, String> paramMap);
	
	int deleteEtrdps(Map<String, String> paramMap);
	
	int deleteBill(Map<String, String> paramMap);
	
	List<Map<String, String>> selectEtrdpsDtlList(Map<String, String> paramMap);
	
	List<Map<String, String>> selectEtrdpsDtlUpdate(Map<String, String> paramMap);
	
	int updateEtrdpsDtl(Map<String, String> paramMap);
	
	int updateEtrdpsDt(Map<String, String> paramMap);
	
	int insertEtrdpsDtl(Map<String, String> paramMap);
	
	int insertAdvPay(Map<String, String> paramMap);
	
	int updateEtrdpsDtlDelete(Map<String, String> paramMap);
	
	int deleteEtrdpsDtl(Map<String, String> paramMap);
	
	int selectEtrdpsMapCount(Map<String, String> paramMap);
	
	List<Map<String, String>> selectEtrdpsMap(Map<String, String> paramMap);
	
	int updateTrstCertiNo(Map<String, String> paramMap);

	void updateAdvPay(Map<String, String> etrdpsData);

	void deleteAdvPay(Map<String, String> etrdpsData);	
	
	//더존 연동 관련 데이터 update 20220630 kdm 
	int updateDzSndSeq(Map<String, String> paramMap);
	
}
