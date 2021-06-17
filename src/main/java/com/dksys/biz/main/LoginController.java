package com.dksys.biz.main;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.admin.cm.cm01.service.CM01Svc;
import com.dksys.biz.main.service.LoginService;
import com.dksys.biz.main.vo.User;
import com.dksys.biz.util.MessageUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class LoginController {

    private final PasswordEncoder passwordEncoder;
    
    @Autowired
	MessageUtils messageUtils;
    
    @Autowired
    LoginService loginService;
    
    @Autowired
    CM01Svc cm01Svc;

//    // 회원가입
//    @PostMapping("/join")
//    public String join(@RequestBody Map<String, String> user, ModelMap model) {
//    	String id = userRepository.save(User.builder()
//        		.id(user.get("email").split("@")[0])
//                .email(user.get("email"))
//                .password(passwordEncoder.encode(user.get("password")))
//                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
//                .build()).getId();
//    	model.addAttribute("id", id);
//        model.addAttribute("msg", "가입 되었습니다.");
//        return "jsonView";
//    }

    // 로그인
    @RequestMapping("/login")
    public String login(@RequestBody Map<String, String> param, ModelMap model) {
    	User user = loginService.selectUserInfo(param);
    	if(user == null) {
    		model.addAttribute("msg", "ID를 확인해주세요.");
    	} else if(!passwordEncoder.matches(param.get("password"), user.getPassword())) {
    		model.addAttribute("msg", "비밀번호를 확인해주세요.");
    	} else {
    		loginService.insertUserHistory(user);
    		model.addAttribute("msg", "success");
    	}
        return "jsonView";
    }
    
    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
      new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
      return "redirect:/";
    }
    
}