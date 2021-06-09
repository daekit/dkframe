package com.dksys.biz.user.sm.sm06;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.sm.sm06.service.SM06Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/sm/sm06")
public class SM06Ctr {
 
	
	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    SM06Svc sm06svc;
    
    //selectStockTotalList 재고 집계 조회 
    @PostMapping("/selectStockTotalList")
    public String selectStockTotlalList(@RequestBody Map<String, String> param, ModelMap model) {
    	System.out.println("paramparamparamparamparamparam");
    	System.out.println(param);
    	int totalCnt = sm06svc.selectStockTotalListCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    
    	List<Map<String, String>> stockList = sm06svc.selectStockTotalList(param);
    	model.addAttribute("stockList", stockList);
        return "jsonView";
    }
    
    //selectStockList 재고 상세 조회 
    @PostMapping("/selectStockDetailList")
    public String selectStockDetailList(@RequestBody Map<String, String> param, ModelMap model) {
    	System.out.println("paramparamparamparamparamparam");
    	System.out.println(param);
    	int totalCnt = sm06svc.selectStockDetailListCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    
    	List<Map<String, String>> stockList = sm06svc.selectStockDetailList(param);
    	model.addAttribute("stockList", stockList);
        return "jsonView";
    }
   
}