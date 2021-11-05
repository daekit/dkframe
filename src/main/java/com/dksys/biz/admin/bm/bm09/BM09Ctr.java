package com.dksys.biz.admin.bm.bm09;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.bm.bm09.service.BM09Svc;
import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/admin/bm/bm09")
public class BM09Ctr {
	
	@Autowired
	MessageUtils messageUtils;
	
	@Autowired
	BM09Svc bm09Svc;
	
    @PostMapping("/selectPledgeList")
    public String selectClntPledgeList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = bm09Svc.selectPledgeCount(paramMap);
    	PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> clntList = bm09Svc.selectPledgeList(paramMap);
    	model.addAttribute("clntList", clntList);
        return "jsonView";
    }

    @PostMapping("/selectPledgeDetailList")
    public String selectClntPledgeDetailList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = bm09Svc.selectPledgeDetailCount(paramMap);
    	PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> clntDetailList = bm09Svc.selectPledgeDetailList(paramMap);
    	model.addAttribute("clntDetailList", clntDetailList);
    	return "jsonView";
    }
    
	// 거래처 담보 이력 리스트 조회
    @PostMapping("/selectPldgHistoryList")
    public String selectClntPledgeHistoryList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = bm09Svc.selectPldgHistoryCount(paramMap);
    	PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> pldgList = bm09Svc.selectPldgHistoryList(paramMap);
    	model.addAttribute("pldgList", pldgList);
        return "jsonView";
    }
  
}