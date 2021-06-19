package com.dksys.biz.user.sm.sm07;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.sm.sm07.service.SM07Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/sm/sm07")
public class SM07Ctr {
 
	
	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    SM07Svc sm07svc;
    
    //selectStockSummaryList 재고 요약 리스트 조회 
    @PostMapping("/selectStockSummaryList")
    public String selectStockSummaryList(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = sm07svc.selectStockSummaryListCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    
    	List<Map<String, String>> stockList = sm07svc.selectStockSummaryList(param);
    	model.addAttribute("stockList", stockList);
        return "jsonView";
    }
    
    //selectStockSummaryDetailList 재고 요약 상세 리스트 조회 
    @PostMapping("/selectStockSummaryDetailList")
    public String selectStockSummaryDetailList(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = sm07svc.selectStockSummaryDetailListCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    
    	List<Map<String, String>> stockList = sm07svc.selectStockSummaryDetailList(param);
    	model.addAttribute("stockDetailList", stockList);
        return "jsonView";
    }
    
   
}