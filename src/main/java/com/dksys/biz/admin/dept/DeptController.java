package com.dksys.biz.admin.dept;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dksys.biz.admin.dept.service.DeptService;

@Controller
@RequestMapping("/admin/dept")
public class DeptController {

    @Autowired
    DeptService deptService;
    
    // 부서트리 조회
    @GetMapping("/selectDeptTree")
    public ModelAndView selectDeptTree() {
    	ModelAndView mv = new ModelAndView("jsonView");
    	List<Map<String, String>> deptTree = deptService.selectDeptTree();
    	mv.addObject("deptTree", deptTree);
        return mv;
    }
    
    // 부서정보 조회
    @GetMapping("/selectDeptInfo")
    public ModelAndView selectDeptInfo(@RequestParam HashMap<String, String> paramMap) {
    	ModelAndView mv = new ModelAndView("jsonView");
    	Map<String, String> deptInfo = deptService.selectDeptInfo(paramMap);
    	mv.addObject("deptInfo", deptInfo);
    	mv.setViewName("jsonView");
        return mv;
    }
    
    // 부서아이디 중복확인
    @GetMapping("/checkDeptId")
    public ModelAndView checkDeptId(@RequestParam HashMap<String, String> paramMap) {
    	ModelAndView mv = new ModelAndView("jsonView");
    	int deptCount = deptService.selectDeptCount(paramMap);
    	mv.addObject("deptCount", deptCount);
    	mv.setViewName("jsonView");
        return mv;
    }
}