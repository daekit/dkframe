package com.dksys.biz.douzone.dz.dz01;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.douzone.dz.dz01.service.DZ01Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/douzone/dz/dz01")
public class DZ01Ctr {
	@Autowired
	DZ01Svc dz01Svc;
	
	@Autowired
	MessageUtils messageUtils;
	
    @PostMapping("/testSelect")
    public String testSelect(@RequestBody Map<String, String> paramMap, ModelMap model) {
		List<Map<String, String>> testList = dz01Svc.testSelect(paramMap);
		model.addAttribute("testList", testList);
    	return "jsonView";
    }
    
    @PostMapping(value = "/testInsert")
    public String testInsert(HttpServletRequest request, @RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		dz01Svc.testInsert(paramMap);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
}