package com.dksys.biz.admin.cm.code.service;

import java.util.List;
import java.util.Map;

public interface CodeService {

	public int selectCodeCount(Map<String, String> param);
	
    public List<Map<String, String>> selectCodeList(Map<String, String> param);
    
    public List<Map<String, String>> selectChildCodeList(Map<String, String> param);

	public int insertCode(Map<String, String> param);

	public int deleteCode(Map<String, String> param);

	public int updateCode(Map<String, String> param);

	public List<Map<String, String>> selectCodeInfoList(Map<String, String> param);

}