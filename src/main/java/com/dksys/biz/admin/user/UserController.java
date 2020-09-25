package com.dksys.biz.admin.user;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.admin.user.service.UserService;
import com.dksys.biz.util.MessageUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/user")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    
    @Autowired
	MessageUtils messageUtils;

    @Autowired
    UserService userService;
    
    // 사용자 리스트
    @PostMapping("/selectUserList")
    public String selectUserList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> userList = userService.selectUserList(paramMap);
    	model.addAttribute("userList", userList);
    	return "jsonView";
    }
    
    // 사용자아이디 중복확인
    @PostMapping("/checkUserId")
    public String checkUserId(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int userCount = userService.selectUserCount(paramMap);
    	model.addAttribute("userCount", userCount);
    	return "jsonView";
    }
    
    // 사용자 등록
    @PostMapping("/createUser")
    public String createUser(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		String rawPassword = paramMap.get("password");
    		paramMap.put("password", passwordEncoder.encode(rawPassword));
    		userService.createUser(paramMap);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	
    	return "jsonView";
    }
    
    // 사용자정보 조회
    @PostMapping("/selectUserInfo")
    public String selectUserInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, String> userInfo = userService.selectUserInfo(paramMap);
    	model.addAttribute("userInfo", userInfo);
    	return "jsonView";
    }
    
    // 사용자 등록
    @PostMapping("/updateUser")
    public String updateUser(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		userService.updateUser(paramMap);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	
    	return "jsonView";
    }
}