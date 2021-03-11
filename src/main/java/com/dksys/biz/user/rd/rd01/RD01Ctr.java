package com.dksys.biz.user.rd.rd01;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.user.rd.rd01.service.RD01Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/rd/rd01")
public class RD01Ctr {
	
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    RD01Svc rd01Svc;
	
    @PutMapping(value = "/inOutPrdt")
	public String inOutPrdt(@RequestBody List<Map<String, String>> paramList, ModelMap model) {
    	try {
    		rd01Svc.inOutPrdt(paramList);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("save"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
	}
}