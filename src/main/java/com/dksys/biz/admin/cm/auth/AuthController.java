package com.dksys.biz.admin.cm.auth;

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

import com.dksys.biz.admin.cm.auth.service.AuthService;
import com.dksys.biz.util.MessageUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/cm/auth")
public class AuthController {

	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    AuthService authService;
    
    // 권한리스트 조회
    @PostMapping("/selectAuthInfo")
    public String selectAuthInfo(ModelMap model) {
    	List<Map<String, String>> authList = authService.selectAuthList();
    	model.addAttribute("authList", authList);
        return "jsonView";
    }
    
    // 권한등록
    @PostMapping("/createAuth")
    public String createAuth(@RequestBody Map<String, String> param, ModelMap model) {
    	authService.insertAuth(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	return "jsonView";
    }
    
    // 권한삭제
    @DeleteMapping("/deleteAuth")
    public String deleteAuth(@RequestBody Map<String, String> param, ModelMap model) {
    	authService.deleteAuth(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
    
    // 권한수정
    @PutMapping("/updateAuth")
    public String updateAuth(@RequestBody Map<String, String> param, ModelMap model) {
    	authService.updateAuth(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
    
    // 권한롤수정
    @PutMapping("/updateAuthRole")
    public String updateAuthRole(@RequestBody Map<String, String> param, ModelMap model) {
    	authService.updateAuthRole(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
    
}