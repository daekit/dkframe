package com.dksys.biz.user.ar.ar07.service;

import java.util.List;
import java.util.Map;

public interface AR07Svc {

	public List<Map<String, String>> selectMtClosCditList(Map<String, String> param);
	
	public int selectMtClosCditCount(Map<String, String> param);

	public List<Map<String, String>> selectClosCditList(Map<String, String> param);

	public int selectMtCloseChkCount(Map<String, String> paramMap);

	public int selectMtClosCditPreCount(Map<String, String> paramMap);

	public List<Map<String, String>> selectMtCloseCditPreList(Map<String, String> paramMap);

	public List<Map<String, String>> selectEtrdpsSellList(Map<String, String> paramMap);

	public int selectPreMtCloseChkCount(Map<String, String> paramMap);

	public int selectPreMtClosCditCount(Map<String, String> paramMap);

	public List<Map<String, String>> selectPreMtClosCditList(Map<String, String> paramMap);
	
}