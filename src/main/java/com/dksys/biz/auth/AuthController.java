package com.dksys.biz.auth;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;
    
    // 회원가입
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
    	model.addAttribute("resultMessage", "SUCCESS");
    	return "jsonView";
    }
    
}