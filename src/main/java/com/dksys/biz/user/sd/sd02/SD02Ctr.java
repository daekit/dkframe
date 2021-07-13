package com.dksys.biz.user.sd.sd02;

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
import com.dksys.biz.user.sd.sd02.service.SD02Svc;
import com.dksys.biz.util.MessageUtils;


@Controller
@RequestMapping("/user/sd/sd02")
public class SD02Ctr {
 
	//단가 관리 컨트롤러 UNIT PRICE -> UPR 표기
	
	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    SD02Svc sd02svc;
    
    @PostMapping(value = "/selectSellList")
    public String selectSellList(@RequestBody Map<String, String> param, ModelMap model) {
 /*   	int totalCnt = sd02svc.selectSellCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
  */  
    	List<Map<String, String>> sellList = sd02svc.selectSellList(param);
    	model.addAttribute("sellList", sellList);
        return "jsonView";
    }
    
    @PostMapping(value = "/selectPlanInfo")
    public String selectPlanInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, String> result = sd02svc.selectPlanInfo(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
    @PostMapping(value = "/insertPlan")
    public String insertPlan(HttpServletRequest request, @RequestBody Map<String, String> param, ModelMap model) {
    	try {
	    	sd02svc.insertPlan(param);
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
			 sd02svc.updatePlan(param);
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
    		sd02svc.deleteCopy(param);
	    	sd02svc.copyInsert(param);
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
	    	return "jsonView";
    }
    
    @DeleteMapping(value = "/deletePlan")
    public String deleteEst(@RequestBody Map<String, Object> paramMap, ModelMap model) {
    	sd02svc.deletePlan(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
    
    @PostMapping(value = "/selectSellDailyRep")
    public String selectSellDailyRep(@RequestBody Map<String, String> param, ModelMap model) {
    	List<Map<String, String>> sellList = sd02svc.selectSellDailyRep(param);
    	model.addAttribute("sellList", sellList);
        return "jsonView";
    }

    @PostMapping(value = "/selectSellListInd")
    public String selectSellListInv(@RequestBody Map<String, String> param, ModelMap model) {

    	List<Map<String, String>> sellList = sd02svc.selectSellListInd(param);
    	model.addAttribute("sellList", sellList);
        return "jsonView";
    }
}