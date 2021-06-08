package com.dksys.biz.user.fi.douzone;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.fi.douzone.service.DouzoneSvc;
import com.dksys.biz.util.MessageUtils;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@Controller
@RequestMapping("/user/fi/douzone")
public class DouzoneCtr {

	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    DouzoneSvc douzoneSvc;

    @PostMapping(value = "/insertAutodocuSimple")
    public String insertAutodocuSimple(@RequestParam Map<String, String> paramMap, ModelMap model) {
    	try {
    		int duplicate = 100; // douzoneSvc.selectAutodocuSimpleDuplicate(paramMap);
    		if(duplicate > 0 ) {
            	model.addAttribute("resultCode", 999);
            	model.addAttribute("resultMessage", "중복 에러입니다. 키 값을 확인해주세요.");
    		} else {
    			douzoneSvc.insertAutodocuSimple(paramMap);
            	model.addAttribute("resultCode", 200);
            	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));	
    		}
    		
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
    
    @PostMapping(value = "/updatePalBill")
    public String updateCplrUntpc(@RequestParam Map<String, String> paramMap, ModelMap model) {
    	try {
    		douzoneSvc.updateAutodocuSimple(paramMap);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
    
    @PutMapping(value = "/deletePalBill")
    public String deleteCplrUntpc(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		douzoneSvc.deleteAutodocuSimple(paramMap);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }  
}