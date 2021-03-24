package com.dksys.biz.user.sd.sd09;

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
import com.dksys.biz.user.sd.sd09.service.SD09Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/sd/sd09")
public class SD09Ctr {

	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    SD09Svc sd09Svc;
	
    @PostMapping(value = "/selectSiteList")
	public String selectSiteList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = sd09Svc.selectSiteCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = sd09Svc.selectSiteList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    @PostMapping(value = "/selectSiteDetail")
	public String selectSiteDetail(@RequestBody Map<String, String> paramMap, ModelMap model) {    	
    	Map<String, String> result = sd09Svc.selectSiteDetail(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
	}
    
    @PostMapping(value = "/insertSite")
    public String insertSite(@RequestParam Map<String, String> paramMap, ModelMap model) {
    	try {
    		sd09Svc.insertSite(paramMap);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
    
    @PostMapping(value = "/updateSite")
    public String updateCplrUntpc(@RequestParam Map<String, String> paramMap, ModelMap model) {
    	try {
    		sd09Svc.updateSite(paramMap);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
    
    @PutMapping(value = "/deleteSite")
    public String deleteCplrUntpc(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		sd09Svc.deleteSite(paramMap);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
}