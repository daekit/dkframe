package com.dksys.biz.admin.cm.cm20;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dksys.biz.admin.cm.cm20.service.CM20Svc;
import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/admin/cm/cm20")
public class CM20Ctr {
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    CM20Svc cm20Svc;
	
    @PostMapping(value = "/selectSanctnList")
	public String selectShipNList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	
    	int totalCnt = cm20Svc.selectSanctnCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = cm20Svc.selectSanctnList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}

    
    
    @PostMapping("/selectSanctnInfo")
    public String selectSanctnInfo(@RequestBody Map<String, String> param, ModelMap model) {
    	Map<String, String> sanctnInfo = cm20Svc.selectSanctnInfo(param);
    	model.addAttribute("sanctnInfo", sanctnInfo);
    	return "jsonView";
    }
    
	@DeleteMapping("/deleteSanctnInfo")
    public String deleteSanctnInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
		try {
			cm20Svc.deleteSanctnInfo(paramMap);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
    
	@PostMapping(value = "/insertSanctnInfo")
	public String insertSanctnInfo(@RequestBody Map<String, Object> paramMap, ModelMap model) {
		
		try {
			cm20Svc.insertSanctnInfo(paramMap);
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
			
		} catch (Exception e) {
			System.out.println(e);
			model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
		
		return "jsonView";
	}
	
	@PutMapping(value = "/updateSanctnInfo")
	public String updateSanctnInfo(@RequestParam Map<String, String> paramMap, ModelMap model) {
		cm20Svc.updateSanctnInfo(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
	
    
    
}