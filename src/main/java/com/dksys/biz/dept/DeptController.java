package com.dksys.biz.dept;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dksys.biz.dept.service.DeptService;

@Controller
@RequestMapping("/api")
public class DeptController {

    @Autowired
    DeptService deptService;
    
    // 부서트리 조회
    @GetMapping("/dept/selectDeptTree")
    public ModelAndView selectDeptTree(ModelMap model) {
    	ModelAndView mv = new ModelAndView("jsonView");
    	List<Map<String, String>> deptTree = deptService.selectDeptTree();
    	mv.addObject("deptTree", deptTree);
        return mv;
    }
    
}