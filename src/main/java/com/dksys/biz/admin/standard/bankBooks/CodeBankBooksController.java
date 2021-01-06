package com.dksys.biz.admin.standard.bankBooks;

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

import com.dksys.biz.admin.standard.bankBooks.service.CodeBankBooksService;
import com.dksys.biz.util.MessageUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/standard/bankBooks")
public class CodeBankBooksController {

	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    CodeBankBooksService codeService;
    
    // 계정과목 코드 리스트 조회
    @PostMapping("/selectCodeList")
    public String selectCodeList(ModelMap model) {
    	List<Map<String, String>> codeList = codeService.selectCodeList();
    	model.addAttribute("codeList", codeList);
        return "jsonView";
    }
    
    // 계정과목 리스트 생성
    @PostMapping("/createCode")
    public String createCode(@RequestBody Map<String, String> param, ModelMap model) {
    	codeService.insertCode(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	return "jsonView";
    }
    
    // 계정과목 리스트 삭제
    @DeleteMapping("/deleteCode")
    public String deleteCode(@RequestBody Map<String, String> param, ModelMap model) {
    	codeService.deleteCode(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
    
    // 계정과목 리스트 변경
    @PutMapping("/updateCode")
    public String updateCode(@RequestBody Map<String, String> param, ModelMap model) {
    	codeService.updateCode(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
    
}