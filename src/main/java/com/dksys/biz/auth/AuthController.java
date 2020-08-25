package com.dksys.biz.auth;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;

import com.dksys.biz.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AuthController {

    @Autowired
    AuthService authService;
    
    // 회원가입
    @PostMapping("/auth/selectAuthInfo")
    public String join(ModelMap model) {
    	List<Map<String, String>> authList = authService.selectAuthList();
    	model.addAttribute("authList", authList);
        return "jsonView";
    }
    
}