package com.dksys.biz.admin.bm.bm02;

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

import com.dksys.biz.admin.bm.bm02.service.BM02Svc;
import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/admin/bm/bm02")
public class BM02Ctr {
	
	@Autowired
	MessageUtils messageUtils;
	
	@Autowired
	BM02Svc bm02Svc;
	
	@PostMapping("/insertClnt")
    public String insertClnt(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
    	try {
    		bm02Svc.insertClnt(paramMap, mRequest);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
	
	@PutMapping("/updateClnt")
    public String updateClnt(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
		try {
    		bm02Svc.updateClnt(paramMap, mRequest);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
	
	// 거래처 리스트 조회
    @PostMapping("/selectClntList")
    public String selectClntList(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = bm02Svc.selectClntCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> clntList = bm02Svc.selectClntList(param);
    	model.addAttribute("clntList", clntList);
        return "jsonView";
    }
    
    // 거래처 정보 조회
    @PostMapping("/selectClntInfo")
    public String selectClntInfo(@RequestBody Map<String, String> param, ModelMap model) {
    	Map<String, String> clntInfo = bm02Svc.selectClntInfo(param);
    	model.addAttribute("clntInfo", clntInfo);
        return "jsonView";
    }
}