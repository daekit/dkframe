package com.dksys.biz.user.sd.sd01;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.sd.sd01.service.SD01Svc;
import com.dksys.biz.util.MessageUtils;


@Controller
@RequestMapping("/user/sd/sd01")
public class SD01Ctr {
 
	//단가 관리 컨트롤러 UNIT PRICE -> UPR 표기
	
	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    SD01Svc sd01svc;
    
    @PostMapping(value = "/selectSellList")
    public String selectSellList(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = sd01svc.selectSellCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    
    	List<Map<String, String>> sellList = sd01svc.selectSellList(param);
    	model.addAttribute("sellList", sellList);
        return "jsonView";
    }
    
    @PostMapping(value = "/selectPlanInfo")
    public String selectPlanInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, String> result = sd01svc.selectPlanInfo(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
    @PostMapping(value = "/insertPlan")
    public String insertPlan(HttpServletRequest request, @RequestBody Map<String, String> param, ModelMap model) {
    	try {
	    	sd01svc.insertPlan(param);
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
	    	return "jsonView";
    }
    
    @PutMapping(value = "/updatePlan")
    public String updatePlan(@RequestBody Map<String, String> param, ModelMap model) {
    	try {
			 sd01svc.updatePlan(param);
			 model.addAttribute("resultUpr", 200);
			 model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch(Exception e) {
	    	 model.addAttribute("resultCode", 500);
	 		 model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    		return "jsonView";
    }
    
    @PostMapping(value = "/copyInsert")
    public String copyInsert(@RequestBody Map<String, String> param, ModelMap model) {
    	try {
    		sd01svc.deleteCopy(param);
	    	sd01svc.copyInsert(param);
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
	    	return "jsonView";
    }
    
}