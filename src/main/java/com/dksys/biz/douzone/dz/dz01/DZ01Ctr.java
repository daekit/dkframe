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
	
    @PostMapping( "/testSelect")
    public String testSelect(@RequestBody Map<String, String> paramMap, ModelMap model) {
		System.out.println("-----------------------------------testSelect");
		List<Map<String, String>> testList = dz01Svc.testSelect(paramMap);
		model.addAttribute("testList", testList);		
    	return "jsonView";
    }
    
    @PostMapping(value = "/dzInsert")
    public String dzInsert(HttpServletRequest request, @RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		int  gbn = 0;
    		
    	    gbn = dz01Svc.dzInsert(paramMap);
    		
    	    if (gbn == 20000) {
        	
    	    	model.addAttribute("resultMessage", "더존 은행거래처를 등록해 주세요");
    	    	
    	    }else {
    	    		
        	    model.addAttribute("resultCode", gbn);
        		model.addAttribute("resultMessage", "insert");

    	    }
    	    
    	    
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 30000);
			model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
    
    

    @PostMapping( "/dzDelete")
    public String dzDelete(@RequestBody Map<String, String> paramMap, ModelMap model) {
		  int chk =  dz01Svc.dzDelete(paramMap);
		 	
		  if (chk == 0) {
			    model.addAttribute("resultCode", 500);
			   	model.addAttribute("resultMessage", "발행된 전표가 있습니다.(i-Cube확인)");
		  }else {
			    model.addAttribute("resultCode", 200);
	      		model.addAttribute("resultMessage", "전표삭제에 성공하였습니다.");
		  }
		  
    	return "jsonView";
    }
}