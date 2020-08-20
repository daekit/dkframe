package com.dksys.biz.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dksys.biz.user.service.UserService;
import com.dksys.biz.user.vo.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
public class TestController {

    @Autowired
    UserService userService;
    
    // 일반요청 테스트
    @PostMapping("/test")
    public String test(ModelMap model) {
    	model.addAttribute("msg", "요청성공");
    	return "jsonView";
    }
    
    // 사용자 전체 조회
    @GetMapping("/user/selectAllUser")
    public ModelAndView selectAllUser(ModelMap model) {
    	ModelAndView mv = new ModelAndView("jsonView");
    	List<User> userList = userService.selectAllUser();
    	mv.addObject("userList", userList);
        return mv;
    }
    
}