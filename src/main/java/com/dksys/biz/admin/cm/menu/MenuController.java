package com.dksys.biz.admin.cm.menu;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.admin.cm.menu.service.MenuService;
import com.dksys.biz.util.MessageUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/cm/menu")
public class MenuController {

	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    MenuService menuService;
    
    // 메뉴리스트 조회
    @PostMapping("/selectMenuList")
    public String selectMenuList(ModelMap model) {
    	List<Map<String, String>> menuList = menuService.selectMenuList();
    	model.addAttribute("menuList", menuList);
        return "jsonView";
    }
    
    // 메뉴등록
    @PostMapping("/createMenu")
    public String createMenu(@RequestBody Map<String, String> param, ModelMap model) {
    	menuService.insertMenu(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	return "jsonView";
    }
    
    // 메뉴삭제
    @DeleteMapping("/deleteMenu")
    public String deleteMenu(@RequestBody Map<String, String> param, ModelMap model) {
    	menuService.deleteMenu(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
    
    // 메뉴수정
    @PutMapping("/updateMenu")
    public String updateMenu(@RequestBody Map<String, String> param, ModelMap model) {
    	menuService.updateMenu(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
    
    // 메뉴아이디 중복확인
    @PostMapping("/checkMenuId")
    public String checkMenuId(@RequestBody Map<String, String> param, ModelMap model) {
    	int menuCount = menuService.selectMenuCount(param);
    	model.addAttribute("menuCount", menuCount);
        return "jsonView";
    }
    
}