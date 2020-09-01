package com.dksys.biz.admin.menu.service;

import java.util.List;
import java.util.Map;

public interface MenuService {

    public List<Map<String, String>> selectMenuList();

	public int insertMenu(Map<String, String> param);

	public int deleteMenu(Map<String, String> param);

	public int updateMenu(Map<String, String> param);


}