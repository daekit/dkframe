package com.dksys.biz.user.pp.pp01;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.pp.pp01.svc.PP01Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/pp/pp01")
public class PP01Ctr {
	
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    PP01Svc pp01Svc;
	
    @PostMapping(value = "/selectMesOrderList")
	public String selectMesOrderList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = pp01Svc.selectMesOrderCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> mesOrderList = pp01Svc.selectMesOrderList(paramMap);
    	model.addAttribute("mesOrderList", mesOrderList);
    	return "jsonView";
	}
    
}