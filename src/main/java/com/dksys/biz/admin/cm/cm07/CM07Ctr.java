package com.dksys.biz.admin.cm.cm07;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.admin.cm.cm07.service.CM07Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/admin/cm/cm07")
public class CM07Ctr {
	
	@Autowired
	MessageUtils messageUtils;
	
	@Autowired
	CM07Svc cm07Svc;
	
	// 직급 리스트
    @PostMapping("/selectLevelList")
    public String selectLevelList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> levelList = cm07Svc.selectLevelList(paramMap);
    	model.addAttribute("levelList", levelList);
    	return "jsonView";
    }
    
    // 직급 중복확인
    @PostMapping("/checkLevelCode")
    public String checkLevelCode(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int levelCount = cm07Svc.selectLevelCount(paramMap);
    	model.addAttribute("levelCount", levelCount);
    	return "jsonView";
    }
    
    // 직급 등록
    @PostMapping("/createLevel")
    public String createLevel(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		cm07Svc.insertLevel(paramMap);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	
    	return "jsonView";
    }
    
    // 직급정보 조회
    @PostMapping("/selectLevelInfo")
    public String selectLevelInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
		Map<String, String> levelInfo = cm07Svc.selectLevelInfo(paramMap);
		model.addAttribute("levelInfo", levelInfo);
    	return "jsonView";
    }
    
    // 직급 수정
    @PutMapping("/updateLevel")
    public String updateLevel(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		cm07Svc.updateLevel(paramMap);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	
    	return "jsonView";
    }
    
}