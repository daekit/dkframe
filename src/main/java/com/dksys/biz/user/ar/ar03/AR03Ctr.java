package com.dksys.biz.user.ar.ar03;

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
import com.dksys.biz.user.ar.ar03.service.AR03Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/ar/ar03")
public class AR03Ctr {
 
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    AR03Svc ar03Svc;
    
    @PostMapping(value = "/selectCaryngList")
    public String selectCaryngList(@RequestBody Map<String, String> param, ModelMap model) {
		int totalCnt = ar03Svc.selectCaryngCount(param);
		PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
		List<Map<String, String>> resultList = ar03Svc.selectCaryngList(param);
		model.addAttribute("resultList", resultList);
    	return "jsonView";
    }
    
    @PostMapping(value = "/selectCaryngInfo")
    public String selectPlanInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, String> result = ar03Svc.selectCaryngInfo(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
    }
    
    @PostMapping(value = "/selectShipList")
	public String selectShipList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar03Svc.selectShipCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = ar03Svc.selectShipList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
	@PostMapping(value = "/insertCaryng")
    public String insertCaryng(HttpServletRequest request, @RequestBody Map<String, String> param, ModelMap model) {
		try {
			ar03Svc.insertCaryng(param);
	    	model.addAttribute("transSeq",param.get("transSeq") );
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
		}catch (Exception e) {
			model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
    	return "jsonView";
    }
	
	@PutMapping(value = "/updateCaryng")
    public String updateCaryng(@RequestBody Map<String, String> param, ModelMap model) {
		try {
			ar03Svc.updateCaryng(param);
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
		}catch (Exception e) {
			model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
    	return "jsonView";
    }
	
	@DeleteMapping(value = "/deleteTrans")
    public String deleteTrans(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	ar03Svc.deleteTrans(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
	
	@PutMapping(value = "/updateProcYn")
    public String updateProcYn(@RequestBody Map<String, Object> param, ModelMap model) {
		try {
			ar03Svc.updateProcYn(param);
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
		}catch (Exception e) {
			model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
    	return "jsonView";
    }
	
}