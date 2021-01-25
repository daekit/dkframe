package com.dksys.biz.admin.cm.cm03.service;

import java.util.List;
import java.util.Map;

public interface CM03Svc {

    public List<Map<String, String>> selectMenuList();

	public int insertMenu(Map<String, String> param);

	public int deleteMenu(Map<String, String> param);

	public int updateMenu(Map<String, String> param);

	public int selectMenuCount(Map<String, String> param);


}