package com.dksys.biz.user.ar.ar02;

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

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.ar.ar02.service.AR02Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/ar/ar02")
public class AR02Ctr {
 
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    AR02Svc ar02Svc;
	
    @PostMapping(value = "/selectSellList")
	public String selectSellList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar02Svc.selectSellCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = ar02Svc.selectSellList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    @PutMapping(value = "/updatePchsSell")
    public String updatePchsSell(@RequestBody Map<String, Object> paramMap, ModelMap model) {
    	ar02Svc.updatePchsSell(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }

	@DeleteMapping(value = "/deleteSell")
    public String deleteSell(@RequestBody Map<String, String> paramMap, ModelMap model) {
		ar02Svc.deleteSell(paramMap);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
}