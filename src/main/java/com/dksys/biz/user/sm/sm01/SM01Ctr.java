package com.dksys.biz.user.sm.sm01;

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

import com.dksys.biz.user.sm.sm01.service.SM01Svc;
import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/sm/sm01")
public class SM01Ctr {
 
	
	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    SM01Svc sm01svc;
    
    //selectStockList 재고 마스터 리스트 조회 
    @PostMapping("/selectStockList")
    public String selectUprList(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = sm01svc.selectStockListCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    
    	List<Map<String, String>> stockList = sm01svc.selectStockList(param);
    	model.addAttribute("stockList", stockList);
        return "jsonView";
    }
   
}