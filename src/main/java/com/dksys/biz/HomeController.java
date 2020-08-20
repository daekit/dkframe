package com.dksys.biz;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
	// 웰컴 페이지
    @GetMapping("/")
    public String welcome(Model model) {
    	return "redirect:/static/index.html";
    }
	
	// 권한 오류 시 요청처리
    @GetMapping("/noAuth")
    public String noAuth(Model model) {
    	throw new RuntimeException("토큰정보가 유효하지 않습니다.");
    }
    
}