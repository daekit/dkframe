package com.dksys.biz.user.ar.ar14;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.exc.LogicException;
import com.dksys.biz.user.ar.ar07.service.AR07Svc;
import com.dksys.biz.user.ar.ar14.service.AR14Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/ar/ar14")
public class AR14Ctr {

	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    AR14Svc ar14Svc;
	
    @PostMapping(value = "/selectDebtList")
	public String selectRcvpayList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	
    	int totalCnt = ar14Svc.selectDebtCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = ar14Svc.selectDebtList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
	@PostMapping(value = "/insertDebt")
    public String insertDebt(@RequestBody Map<String, String> paramMap, ModelMap model) {
		try {
			ar14Svc.insertDebt(paramMap);
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
		}catch(Exception e) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
    	return "jsonView";
    }
	
    @PostMapping(value = "/selectDebtGroupList")
	public String selectDebtGroupList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	
    	int totalCnt = ar14Svc.selectDebtGroupCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = ar14Svc.selectDebtGroupList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    @DeleteMapping(value = "/deleteDebt")
    public String deleteDebt(@RequestBody Map<String, String> paramMap, ModelMap model) {
		try {
			ar14Svc.deleteDebt(paramMap);
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
		}catch(Exception e) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
    	return "jsonView";
    }
}