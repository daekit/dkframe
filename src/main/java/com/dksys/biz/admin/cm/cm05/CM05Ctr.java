package com.dksys.biz.admin.cm.cm05;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.admin.cm.cm05.service.CM05Svc;
import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.util.MessageUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/cm/cm05")
public class CM05Ctr {

	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    CM05Svc cm05Svc;
    
    // 공통코드 리스트 조회
    @PostMapping("/selectCodeList")
    public String selectCodeList(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = cm05Svc.selectCodeCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> codeList = cm05Svc.selectCodeList(param);
    	model.addAttribute("codeList", codeList);
        return "jsonView";
    }
    
    // 공통코드 리스트 조회
    @PostMapping("/selectPdskCodeList")
    public String selectPdskCodeList(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = cm05Svc.selectPdskCodeCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> codeList = cm05Svc.selectPdskCodeList(param);
    	model.addAttribute("codeList", codeList);
        return "jsonView";
    }
    
    // 하위코드 리스트 조회
    @PostMapping("/selectChildCodeList")
    public String selectChildCodeList(@RequestBody Map<String, String> param, ModelMap model) {
    	List<Map<String, String>> childCodeList = cm05Svc.selectChildCodeList(param);
    	model.addAttribute("childCodeList", childCodeList);
        return "jsonView";
    }
    
    // 공통코드 정보 조회
    @PostMapping("/selectCodeInfo")
    public String selectCodeInfo(@RequestBody Map<String, String> param, ModelMap model) {
    	Map<String, String> codeInfo = cm05Svc.selectCodeInfo(param);
    	model.addAttribute("codeInfo", codeInfo);
        return "jsonView";
    }
    
    // 공통코드 정보 리스트 조회
    @PostMapping("/selectCodeInfoList")
    public String selectCodeInfoList(@RequestBody Map<String, String> param, ModelMap model) {
    	List<Map<String, String>> codeInfoList = cm05Svc.selectCodeInfoList(param);
    	model.addAttribute("codeInfoList", codeInfoList);
        return "jsonView";
    }
    
    // 공통코드 등록/수정
    @PostMapping("/insertCode")
    public String insertCode(@RequestBody Map<String, String> param, ModelMap model) {
    	try {
    		cm05Svc.insertCode(param);
    		model.addAttribute("resultCode", 200);
    		model.addAttribute("resultMessage", "C".equals(param.get("actionType")) ? messageUtils.getMessage("insert") : messageUtils.getMessage("update"));
    	} catch(Exception e) {
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
    
    // 공통코드삭제
    @PutMapping("/deleteCode")
    public String deleteCode(@RequestBody Map<String, String> param, ModelMap model) {
    	cm05Svc.deleteCode(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
    
}