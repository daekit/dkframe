package com.dksys.biz.user.sm.sm03;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.sm.sm03.service.SM03Svc;

@Controller
@RequestMapping("/user/sm/sm03")
public class SM03Ctr {
 
    @Autowired
    SM03Svc sm03svc;
    
    //selectStockList 재고 마스터 리스트 조회 
    @PostMapping("/selectStockList")
    public String selectUprList(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = sm03svc.selectStockListCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    
    	List<Map<String, String>> stockList = sm03svc.selectStockList(param);
    	model.addAttribute("stockList", stockList);
        return "jsonView";
    }
    
}