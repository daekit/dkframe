package com.dksys.biz.admin.cm.cm06;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.admin.cm.cm06.service.CM06Svc;
import com.dksys.biz.main.service.LoginService;
import com.dksys.biz.main.vo.User;
import com.dksys.biz.util.MessageUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/cm/cm06")
public class CM06Ctr {

    private final PasswordEncoder passwordEncoder;
    
    @Autowired
	MessageUtils messageUtils;

    @Autowired
    CM06Svc cm06Svc;
    
    @Autowired
    LoginService loginService;
    
    // 사용자 리스트 조회
    @PostMapping("/selectUserList")
    public String selectUserList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> userList = cm06Svc.selectUserList(paramMap);
    	model.addAttribute("userList", userList);
    	return "jsonView";
    }
    
    // 사용자 리스트 조회
    @PostMapping("/insertPgmHistory")
    public String insertPgmHistory(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	cm06Svc.insertPgmHistory(paramMap);
    	return "jsonView";
    }
    
    // 사용자 정보 조회
    @PostMapping("/selectUserInfo")
    public String selectUserInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, String> userInfo = cm06Svc.selectUserInfo(paramMap);
    	model.addAttribute("userInfo", userInfo);
    	return "jsonView";
    }
    
    // 사용자 트리 조회
    @PostMapping("/selectUserTree")
    public String selectUserTree(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> userTree = cm06Svc.selectUserTree(paramMap);
    	model.addAttribute("userTree", userTree);
    	return "jsonView";
    }
    
    // 사용자아이디 중복확인
    @PostMapping("/checkUserId")
    public String checkUserId(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int userCount = cm06Svc.selectUserCount(paramMap);
    	model.addAttribute("userCount", userCount);
    	return "jsonView";
    }
    
    // 사용자 등록
    @PostMapping("/createUser")
    public String createUser(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		String rawPassword = paramMap.get("password");
    		paramMap.put("password", passwordEncoder.encode(rawPassword));
    		cm06Svc.insertUser(paramMap);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	
    	return "jsonView";
    }
    
    // 사용자 수정
    @PutMapping("/updateUser")
    public String updateUser(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		cm06Svc.updateUser(paramMap);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	
    	return "jsonView";
    }
    
    // 비밀번호 수정
    @PutMapping("/updatePw")
    public String updatePw(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	paramMap.put("id", paramMap.get("userId"));
    	String prePw = paramMap.get("prePw");
    	String newPw = paramMap.get("newPw");
		paramMap.put("password", passwordEncoder.encode(newPw));
    	User user = loginService.selectUserInfo(paramMap);
		if(!passwordEncoder.matches(prePw, user.getPassword())) {
			model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("unMatchPw"));
			return "jsonView";
		}
		int result = cm06Svc.updatePw(paramMap);
		if(result == 2) {
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("relogin"));
		} else {
			model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
    	return "jsonView";
    }
    
    // 비밀번호 수정 by admin
    @PutMapping("/updatePwByAdmin")
    public String updatePwByAdmin(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	paramMap.put("id", paramMap.get("userId"));
    	String prePw = paramMap.get("prePw");
    	String newPw = paramMap.get("newPw");
		paramMap.put("password", passwordEncoder.encode(newPw));
		int result = cm06Svc.updatePw(paramMap);
		if(result == 2) {
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("relogin"));
		} else {
			model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
    	return "jsonView";
    }
    
    // 암호관리규칙조회
    @PostMapping("/selectRuleCheckList")
    public String selectRuleCheckList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> ruleList = cm06Svc.selectRuleCheckList(paramMap);
    	model.addAttribute("ruleList", ruleList);
    	return "jsonView";
    }
    
    // 비밀번호 변경기간 유효성 검사.
    @PostMapping("/checkPwdDttm")
    public String checkPwdDttm(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	User user = loginService.selectUserInfo(paramMap);
    	Map<String, String> pwdRuleInfo = cm06Svc.selectRuleCheckList(paramMap).get(0);
    	String passYn = pwdRuleInfo.get("passYn");
    	
    	if("Y".equals(passYn)) {
    		int passChg = -1 * Integer.parseInt(pwdRuleInfo.get("passChg"));
    		// 현재일로부터 지정된 개월수만큼 빼기
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(new Date());
    		cal.add(Calendar.MONTH, passChg);
    		
    		// 패스워드 변경날짜와 비교
    		Date pwdDttm = user.getPwdDttm();
    		if(pwdDttm.compareTo(cal.getTime()) == -1) {
    		// 패스워드 변경일이 계산한 날보다 작으면
    			model.addAttribute("isExpired", "Y");
    			return "jsonView";
    		}
    	}
    	model.addAttribute("isExpired", "N");
    	return "jsonView";
    }
}