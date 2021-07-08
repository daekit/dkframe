package com.dksys.biz.user.sm.sm08;

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
import com.dksys.biz.user.sm.sm08.service.SM08Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/sm/sm08")
public class SM08Ctr {
	
	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    SM08Svc sm08svc;
    
    //selectStockSummaryDetailList 제강사 턴키 재고 리스트 조회 
    @PostMapping("/selectRcvpayTnKeyDailyList")
    public String selectRcvpayTnKeyDailyList(@RequestBody Map<String, String> param, ModelMap model) {
    	System.out.println("@param : " + param.toString());
    	int totalCnt = sm08svc.selectRcvpayTnKeyDailyListCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    
    	List<Map<String, String>> stockList = sm08svc.selectRcvpayTnKeyDailyList(param);
    	model.addAttribute("resultList", stockList);
        return "jsonView";
    }
   
}