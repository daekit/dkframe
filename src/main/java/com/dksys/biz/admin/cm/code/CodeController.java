package com.dksys.biz.admin.cm.code;

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

import com.dksys.biz.admin.cm.code.service.CodeService;
import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.util.MessageUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/cm/code")
public class CodeController {

	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    CodeService codeService;
    
    // 공통코드 리스트 조회
    @PostMapping("/selectCodeList")
    public String selectCodeList(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = codeService.selectCodeCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> codeList = codeService.selectCodeList(param);
    	model.addAttribute("codeList", codeList);
        return "jsonView";
    }
    
    // 하위코드 리스트 조회
    @PostMapping("/selectChildCodeList")
    public String selectChildCodeList(@RequestBody Map<String, String> param, ModelMap model) {
    	List<Map<String, String>> childCodeList = codeService.selectChildCodeList(param);
    	model.addAttribute("childCodeList", childCodeList);
        return "jsonView";
    }
    
    // 코드등록
    @PostMapping("/createCode")
    public String createCode(@RequestBody Map<String, String> param, ModelMap model) {
    	codeService.insertCode(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	return "jsonView";
    }
    
    // 권한삭제
    @DeleteMapping("/deleteCode")
    public String deleteCode(@RequestBody Map<String, String> param, ModelMap model) {
    	codeService.deleteCode(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
    
    // 권한수정
    @PutMapping("/updateCode")
    public String updateCode(@RequestBody Map<String, String> param, ModelMap model) {
    	codeService.updateCode(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
    
    // 공통코드 정보 가져오기
    @PostMapping("/selectCodeInfo")
    public String selectCodeInfo(@RequestBody Map<String, String> param, ModelMap model) {
    	List<Map<String, String>> codeList = codeService.selectCodeInfoList(param);
    	model.addAttribute("codeList", codeList);
        return "jsonView";
    }
    
}