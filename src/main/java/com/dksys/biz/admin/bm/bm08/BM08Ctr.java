package com.dksys.biz.admin.bm.bm08;

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

import com.dksys.biz.admin.bm.bm08.service.BM08Svc;
import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/admin/bm/bm08")
public class BM08Ctr {
	
	@Autowired
	MessageUtils messageUtils;
	
	@Autowired
	BM08Svc bm08Svc;
	
	// 거래처 리스트 조회
    @PostMapping("/selectClntPledgeList")
    public String selectClntPledgeList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = bm08Svc.selectClntPledgeCount(paramMap);
    	PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> clntList = bm08Svc.selectClntPledgeList(paramMap);
    	model.addAttribute("clntList", clntList);
        return "jsonView";
    }

    // 거래처 리스트 조회
    @PostMapping("/selectClntPledgeDetailList")
    public String selectClntPledgeDetailList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = bm08Svc.selectClntPledgeDetailCount(paramMap);
    	PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> clntDetailList = bm08Svc.selectClntPledgeDetailList(paramMap);
    	model.addAttribute("clntDetailList", clntDetailList);
    	return "jsonView";
    }
    
	// 거래처 담보 이력 리스트 조회
    @PostMapping("/selectClntPldgHistoryList")
    public String selectClntPledgeHistoryList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = bm08Svc.selectClntPldgHistoryCount(paramMap);
    	PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> pldgList = bm08Svc.selectClntPldgHistoryList(paramMap);
    	model.addAttribute("pldgList", pldgList);
        return "jsonView";
    }
  
}