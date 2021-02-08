package com.dksys.biz.admin.cm.cm04;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.admin.cm.cm04.service.CM04Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/admin/cm/cm04")
public class CM04Ctr {
	
	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    CM04Svc cm04Svc;
    
    // 부서트리 조회
    @PostMapping("/selectDeptTree")
    public String selectDeptTree(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> deptTree = cm04Svc.selectDeptTree(paramMap);
    	model.addAttribute("deptTree", deptTree);
        return "jsonView";
    }
    
    // 부서정보 조회
    @PostMapping("/selectDeptInfo")
    public String selectDeptInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, String> deptInfo = cm04Svc.selectDeptInfo(paramMap);
    	model.addAttribute("deptInfo", deptInfo);
    	return "jsonView";
    }
    
    // 부서아이디 중복확인
    @PostMapping("/checkDeptId")
    public String checkDeptId(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int deptCount = cm04Svc.selectDeptCount(paramMap);
    	model.addAttribute("deptCount", deptCount);
        return "jsonView";
    }
    
    // 부서 저장
    @PutMapping("/updateDept")
    public String updateDept(HttpServletRequest request, @RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		cm04Svc.updateDept(paramMap);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("save"));
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
        return "jsonView";
    }
    
    // 부서이동
    @PostMapping("/moveDept")
    public String moveDept(@RequestBody List<Map<String, String>> paramList, ModelMap model) {
    	try {
    		cm04Svc.moveDept(paramList);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("move"));
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
        return "jsonView";
    }
}