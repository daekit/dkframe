package com.dksys.biz.user.sd.sd07;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.user.sd.sd07.service.SD07Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/sd/sd07")

public class SD07Ctr {
	
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    SD07Svc sd07Svc;
	
    @PutMapping(value = "/saveCloseInfo")
	public String saveCloseInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		sd07Svc.saveCloseInfo(paramMap);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("save"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
	}
}