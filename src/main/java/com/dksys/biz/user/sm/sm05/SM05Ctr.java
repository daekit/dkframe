package com.dksys.biz.user.sm.sm05;

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
import com.dksys.biz.user.sm.sm05.service.SM05Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/sm/sm05")
public class SM05Ctr {
 
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    SM05Svc sm05Svc;
	
    @PostMapping(value = "/selectPchsAggr")
	public String selectPchsAggr(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = sm05Svc.selectPchsAggrCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = sm05Svc.selectPchsAggrList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/bilgCertNoList")
	public String bilgCertNoList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> resultList = sm05Svc.bilgCertNoList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/bilgCertNullNoList")
    public String bilgCertNullNoList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> resultList = sm05Svc.bilgCertNullNoList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
    }
    
}