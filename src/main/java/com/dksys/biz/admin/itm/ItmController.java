package com.dksys.biz.admin.itm;

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
import com.dksys.biz.admin.itm.service.ItmService;
import com.dksys.biz.util.MessageUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/itm")
public class ItmController {

	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    ItmService itmService;
    
    @Autowired
    CodeService codeService;
    
    // 품목코드리스트 조회
    @PostMapping("/selectItmList")
    public String selectItmList(ModelMap model) {
    	List<Map<String, String>> itmList = itmService.selectItmList();
    	model.addAttribute("itmList", itmList);
        return "jsonView";
    }
    
    // 품목코드리스트정보 조회
    @PostMapping("/selectItmInfo")
    public String selectItmInfo(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	Map<String, String> itmInfo = itmService.selectItmInfo(paramMap);
    	model.addAttribute("itmInfo", itmInfo);
    	return "jsonView";
    }
    
    // 품목코드등록
    @PostMapping("/createItm")
    public String createItm(@RequestBody Map<String, String> param, ModelMap model) {
    	itmService.insertItm(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	return "jsonView";
    }
    
    // 품목코드삭제
    @DeleteMapping("/deleteItm")
    public String deleteItm(@RequestBody Map<String, String> param, ModelMap model) {
    	itmService.deleteItm(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
    
    // 품목코드수정
    @PutMapping("/updateItm")
    public String updateItm(@RequestBody Map<String, String> param, ModelMap model) {
    	itmService.updateItm(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
}