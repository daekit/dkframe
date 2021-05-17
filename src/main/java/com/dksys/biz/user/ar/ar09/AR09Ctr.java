package com.dksys.biz.user.ar.ar09;

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
import com.dksys.biz.user.ar.ar09.service.AR09Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/ar/ar09")
public class AR09Ctr {
 
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    AR09Svc ar09Svc;
	
    @PostMapping(value = "/selectYySalesList")
	public String selectYySalesList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = ar09Svc.selectYySalesListCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = ar09Svc.selectYySalesList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    

}