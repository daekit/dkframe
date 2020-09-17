package com.dksys.biz.user;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.user.service.UserService;
import com.dksys.biz.user.vo.User;
import com.dksys.biz.util.MessageUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    
    @Autowired
	MessageUtils messageUtils;

    @Autowired
    UserService userService;
    
    // 회원가입
    @PostMapping("/join")
    public String join(@RequestBody Map<String, String> user, ModelMap model) {
    	String id = userRepository.save(User.builder()
        		.id(user.get("email").split("@")[0])
                .email(user.get("email"))
                .password(passwordEncoder.encode(user.get("password")))
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build()).getId();
    	model.addAttribute("id", id);
        model.addAttribute("msg", "가입 되었습니다.");
        return "jsonView";
    }

    // 로그인
    @RequestMapping("/login")
    public String login(@RequestBody Map<String, String> user, ModelMap model) {
        User member = userRepository.findById(user.get("id"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 ID 입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        } else {
        	model.addAttribute("token", "1");
        }
        return "jsonView";
    }
    
    // 사용자 리스트
    @PostMapping("/admin/user/selectUserList")
    public String selectUserList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> userList = userService.selectUserList(paramMap);
    	model.addAttribute("userList", userList);
    	return "jsonView";
    }
    
    // 사용자아이디 중복확인
    @PostMapping("/admin/user/checkUserId")
    public String checkUserId(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int userCount = userService.selectUserCount(paramMap);
    	model.addAttribute("userCount", userCount);
    	return "jsonView";
    }
    
    // 사용자 등록
    @PostMapping("/admin/user/createUser")
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
    @PostMapping("/admin/user/selectUserInfo")
    public String selectUserInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, String> userInfo = userService.selectUserInfo(paramMap);
    	model.addAttribute("userInfo", userInfo);
    	return "jsonView";
    }
    
    // 사용자 등록
    @PostMapping("/admin/user/updateUser")
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