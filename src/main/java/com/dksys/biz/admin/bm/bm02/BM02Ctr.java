package com.dksys.biz.admin.bm.bm02;

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
	
	// 거래처 리스트 조회
    @PostMapping("/selectClntList")
    public String selectClntList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = bm02Svc.selectClntCount(paramMap);
    	PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> clntList = bm02Svc.selectClntList(paramMap);
    	model.addAttribute("clntList", clntList);
        return "jsonView";
    }

    // 거래처 정보 조회
    @PostMapping("/selectClntInfo")
    public String selectClntInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, Object> clntInfo = bm02Svc.selectClntInfo(paramMap);
    	model.addAttribute("clntInfo", clntInfo);
        return "jsonView";
    }
    
    // 거래처 등록
	@PostMapping("/insertClnt")
    public String insertClnt(@RequestParam Map<String, String> paramMap, MultipartHttpServletRequest mRequest, ModelMap model) {
    	try {
    		Map<String, String> dupClnt = bm02Svc.selectCrnDupChk(paramMap);
        	if(dupClnt != null && dupClnt.size() > 0) {
        		model.addAttribute("resultCode", 500);
        		model.addAttribute("resultMessage",dupClnt.get("clntNm") + " : 동일 사업자번호를 가진 거래처가 있습니다.");
        	}else {
	    		bm02Svc.insertClnt(paramMap, mRequest);
	    		model.addAttribute("resultCode", 200);
	    		model.addAttribute("resultMessage", messageUtils.getMessage("insert"));    		
        	}
    	}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
	
	// 거래처 수정
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
	
	// 담보내역 삭제
	@DeleteMapping("/deleteClntPldg")
    public String deleteClntPldg(@RequestBody Map<String, Object> paramMap, ModelMap model) {
		try {
    		bm02Svc.deleteClntPldg(paramMap);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }

	
	// 사업자부서 내역 삭제
	@DeleteMapping("/deleteClntBizdept")
    public String deleteClntBizdept(@RequestBody Map<String, Object> paramMap, ModelMap model) {
		try {
    		bm02Svc.deleteClntBizdept(paramMap);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
		
	
	
	// 거래처 사용안함 처리
	@PutMapping("/unuseClnt")
    public String unuseClnt(@RequestBody Map<String, String> paramMap, ModelMap model) {
		try {
    		bm02Svc.unuseClnt(paramMap);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	}catch(Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
	
	// 거래처 담당자 정보 조회
	@PostMapping("/selectMngInfo")
    public String selectMngInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, String> mngInfo = bm02Svc.selectMngInfo(paramMap);
    	model.addAttribute("mngInfo", mngInfo);
        return "jsonView";
    }
}