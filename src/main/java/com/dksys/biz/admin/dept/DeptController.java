package com.dksys.biz.admin.dept;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.admin.dept.service.DeptService;

@Controller
@RequestMapping("/admin/dept")
public class DeptController {

    @Autowired
    DeptService deptService;
    
    // 부서트리 조회
    @PostMapping("/selectDeptTree")
    public String selectDeptTree(ModelMap model) {
    	List<Map<String, String>> deptTree = deptService.selectDeptTree();
    	model.addAttribute("deptTree", deptTree);
        return "jsonView";
    }
    
    // 부서정보 조회
    @PostMapping("/selectDeptInfo")
    public String selectDeptInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, String> deptInfo = deptService.selectDeptInfo(paramMap);
    	model.addAttribute("deptInfo", deptInfo);
    	return "jsonView";
    }
    
    // 부서아이디 중복확인
    @PostMapping("/checkDeptId")
    public String checkDeptId(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int deptCount = deptService.selectDeptCount(paramMap);
    	model.addAttribute("deptCount", deptCount);
        return "jsonView";
    }
    
    // 부서 저장
    @PutMapping("/updateDept")
    public String updateDept(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		deptService.updateDept(paramMap);
    		model.addAttribute("resultCode", 200);
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    	}
    	
        return "jsonView";
    }
}