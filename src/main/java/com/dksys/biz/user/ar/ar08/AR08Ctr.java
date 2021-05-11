package com.dksys.biz.user.ar.ar08;

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
import com.dksys.biz.user.ar.ar08.service.AR08Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/ar/ar08")
public class AR08Ctr {
 
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    AR08Svc ar08Svc;
	
    @PostMapping(value = "/selectCreditList")
	public String selectCreditList(@RequestBody Map<String, String> paramMap, ModelMap model) {
   // 	int totalCnt = ar08Svc.selectCreditCount(paramMap);
	//	PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
 //   	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = ar08Svc.selectCreditList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
    

}