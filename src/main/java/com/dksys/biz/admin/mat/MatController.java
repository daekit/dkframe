package com.dksys.biz.admin.mat;

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

import com.dksys.biz.admin.auth.service.AuthService;
import com.dksys.biz.util.MessageUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/mat")
public class MatController {

	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    AuthService authService;
    
    // 자재코드리스트 조회
    @PostMapping("/selectMatInfo")
    public String selectMatInfo(ModelMap model) {
    	List<Map<String, String>> authList = authService.selectAuthList();
    	model.addAttribute("authList", authList);
        return "jsonView";
    }
    
    // 자재코드등록
    @PostMapping("/createMat")
    public String createMat(@RequestBody Map<String, String> param, ModelMap model) {
    	authService.insertAuth(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	return "jsonView";
    }
    
    // 자재코드삭제
    @DeleteMapping("/deleteMat")
    public String deleteMat(@RequestBody Map<String, String> param, ModelMap model) {
    	authService.deleteAuth(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
    
    // 자재코드수정
    @PutMapping("/updateMat")
    public String updateMat(@RequestBody Map<String, String> param, ModelMap model) {
    	authService.updateAuth(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
}