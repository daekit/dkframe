package com.dksys.biz.admin.bm.bm02;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/admin/bm/bm02")
public class BM02Ctr {
	
	@Autowired
	MessageUtils messageUtils;
	
	@PostMapping("/insertClnt")
    public String insertClnt(@RequestParam Map<String, String> paramMap, HttpServletRequest request, MultipartHttpServletRequest mRequest, ModelMap model) {
    	try {
    		paramMap.put("pgmId", request.getRequestURI());
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	
    	return "jsonView";
    }
}