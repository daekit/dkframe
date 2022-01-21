package com.dksys.biz.admin.bm.account;

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

import com.dksys.biz.admin.bm.account.service.AccountService;
import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/admin/bm/account")
public class AccountController {
 
	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    AccountService codeService;
    
    // 계정과목코드리스트 조회    
    @PostMapping("/selectCodeList")
    public String selectCodeList(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = codeService.selectCodeCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    
    	List<Map<String, String>> codeList = codeService.selectCodeList(param);
    	model.addAttribute("codeList", codeList);
        return "jsonView";
    }
    
    // 계정과목코드등록
    @PostMapping("/createCode")
    public String createCode(@RequestBody Map<String, String> param, ModelMap model) {
    	codeService.insertCode(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	return "jsonView";
    }
    
    // 계정과목코드삭제
    @DeleteMapping("/deleteCode")
    public String deleteCode(@RequestBody Map<String, String> param, ModelMap model) {
    	codeService.deleteCode(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
    
    // 계정과목코드수정
    @PutMapping("/updateCode")
    public String updateCode(@RequestBody Map<String, String> param, ModelMap model) {
    	codeService.updateCode(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
    


}