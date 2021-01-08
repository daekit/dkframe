package com.dksys.biz.admin.menu.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.admin.menu.mapper.MenuMapper;
import com.dksys.biz.admin.menu.service.MenuService;

@Service
public class MenuServiceImpl implements MenuService {
	
    @Autowired
    MenuMapper menuMapper;

	@Override
	public List<Map<String, String>> selectMenuList() {
		return menuMapper.selectMenuList();
	}

	@Override
	public int insertMenu(Map<String, String> param) {
		return menuMapper.insertMenu(param);
	}

	@Override
	public int deleteMenu(Map<String, String> param) {
		return menuMapper.deleteMenu(param);
	}

	@Override
	public int updateMenu(Map<String, String> param) {
		return menuMapper.updateMenu(param);
	}

	@Override
	public int selectMenuCount(Map<String, String> param) {
		return menuMapper.selectMenuCount(param);
	}

}