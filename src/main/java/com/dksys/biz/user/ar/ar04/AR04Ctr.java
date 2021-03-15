package com.dksys.biz.user.ar.ar04;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.user.ar.ar04.service.AR04Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/ar/ar04")
public class AR04Ctr {
 
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    AR04Svc ar04Svc;
	
	@PostMapping(value = "/insertBilg")
    public String insertBilg(@RequestBody Map<String, Object> paramMap, ModelMap model) {
		ar04Svc.insertBilg(paramMap);
		System.out.println(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	return "jsonView";
    }
}