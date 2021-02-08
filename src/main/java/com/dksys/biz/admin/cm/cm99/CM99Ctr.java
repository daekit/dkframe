package com.dksys.biz.admin.cm.cm99;

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
@RequestMapping("/admin/cm/cm99")
public class CM99Ctr {
	
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
    
    
}