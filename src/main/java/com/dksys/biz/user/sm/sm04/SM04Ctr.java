package com.dksys.biz.user.sm.sm04;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.sm.sm04.service.SM04Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/sm/sm04")
public class SM04Ctr {
 
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    SM04Svc sm04Svc;
	
    @PostMapping(value = "/selectPrdtAcinsList")
	public String selectShipList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = sm04Svc.selectPrdtAcinsCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = sm04Svc.selectPrdtAcinsList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/selectPrdtAcinsInfo")
    public String selectPrdtAcinsInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, String> result = sm04Svc.selectPrdtAcinsInfo(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
    @PostMapping(value = "/insertPrdtAcins")
    public String insertPrdtAcins(HttpServletRequest request, @RequestBody Map<String, String> param, ModelMap model) {
    	try {
	    	sm04Svc.insertPrdtAcins(param);
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
	    	return "jsonView";
    }
    
    @PutMapping(value = "/updatePrdtAcins")
    public String updatePrdtAcins(@RequestBody Map<String, String> param, ModelMap model) {
    	try {
			 sm04Svc.updatePrdtAcins(param);
			 model.addAttribute("resultUpr", 200);
			 model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch(Exception e) {
	    	 model.addAttribute("resultCode", 500);
	 		 model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    		return "jsonView";
    }
    
    @DeleteMapping(value = "/deletePrdtAcins")
    public String deletePrdtAcins(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	sm04Svc.deletePrdtAcins(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }

    @PostMapping(value = "/copyInsert")
    public String copyInsert(@RequestBody Map<String, String> param, ModelMap model) {
    	try {
    		sm04Svc.deleteCopy(param);
	    	sm04Svc.copyInsert(param);
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
	    	return "jsonView";
    }
}