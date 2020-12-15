package com.dksys.biz.admin.mat;

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

import com.dksys.biz.admin.code.service.CodeService;
import com.dksys.biz.admin.mat.service.MatService;
import com.dksys.biz.util.MessageUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/mat")
public class MatController {

	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    MatService matService;
    
    @Autowired
    CodeService codeService;
    
    // 자재코드리스트 조회
    @PostMapping("/selectMatList")
    public String selectMatList(ModelMap model) {
    	List<Map<String, String>> matList = matService.selectMatList();
    	model.addAttribute("matList", matList);
        return "jsonView";
    }
    
    // 자재코드리스트정보 조회
    @PostMapping("/selectMatInfo")
    public String selectMatInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, String> matInfo = matService.selectMatInfo(paramMap);
    	model.addAttribute("matInfo", matInfo);
    	//대분류
    	paramMap.put("codeKind", "MATCMN100");
    	List<Map<String, String>> matCodeList100 = codeService.selectCodeInfoList(paramMap);
    	model.addAttribute("matCodeList100", matCodeList100);
    	return "jsonView";
    }
    
    // 자재코드등록
    @PostMapping("/createMat")
    public String createMat(@RequestBody Map<String, String> param, ModelMap model) {
    	matService.insertMat(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	return "jsonView";
    }
    
    // 자재코드삭제
    @DeleteMapping("/deleteMat")
    public String deleteMat(@RequestBody Map<String, String> param, ModelMap model) {
    	matService.deleteMat(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
    
    // 자재코드수정
    @PutMapping("/updateMat")
    public String updateMat(@RequestBody Map<String, String> param, ModelMap model) {
    	matService.updateMat(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
}