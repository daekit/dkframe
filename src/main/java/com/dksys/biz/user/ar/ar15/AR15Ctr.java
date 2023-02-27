package com.dksys.biz.user.ar.ar15;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.ar.ar15.service.AR15Svc;

@Controller
@RequestMapping("/user/ar/ar15")
public class AR15Ctr {
	
	@Autowired
	AR15Svc ar15Svc;
	
	@PostMapping(value = "/selectSalesMngYyList")
	public String selectSalesMngYyList(@RequestBody Map<String, String> paramMap, ModelMap model) {
		int totalCnt = ar15Svc.selectSalesMngYyListCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
		model.addAttribute("paginationInfo", paginationInfo);
		
		List<Map<String, String>> resultList = ar15Svc.selectSalesMngYyList(paramMap);
		model.addAttribute("resultList", resultList);
		return "jsonView";
	}
	
}
