package com.dksys.biz;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.dksys.biz.admin.auth.service.AuthService;
import com.dksys.biz.util.WebClientUtil;

@Controller
public class HomeController {

    @Autowired
    AuthService authService;
	
    @Autowired
    WebClientUtil webClientUtil;
    
    @GetMapping("/rest/get")
    public String get(Model model) {
    	String result = webClientUtil.get("http://asset2.dongkuk.com/cmn/getHeadInfo.json?orgCd=UNC50011431&comOrgCd=UNC");
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
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
    
    // 접근 가능한 메뉴정보
    @PostMapping("/selectMenuAuth")
    public String selectMenuAuth(@RequestBody Map<String, Object> param, Model model) {
    	String[] authArray = {"AUTH000"};
    	authArray = param.get("authInfo") != null ? param.get("authInfo").toString().split(",") : authArray;
    	List<Map<String, Object>> accessList = authService.selectMenuAuth(authArray);
    	model.addAttribute("accessList", accessList);
    	return "jsonView";
    }
    
}