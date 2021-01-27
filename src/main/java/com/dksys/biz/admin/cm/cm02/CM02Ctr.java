package com.dksys.biz.admin.cm.cm02;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.admin.cm.cm02.service.CM02Svc;
import com.dksys.biz.util.MessageUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/cm/cm02")
public class CM02Ctr {

	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    CM02Svc cm02Svc;
    
    // 롤리스트 조회
    @PostMapping("/selectRoleInfo")
    public String selectRoleInfo(ModelMap model) {
    	List<Map<String, String>> roleList = cm02Svc.selectRoleList();
    	model.addAttribute("roleList", roleList);
        return "jsonView";
    }
    
    // 롤등록
    @PostMapping("/insertRole")
    public String insertRole(HttpServletRequest request, @RequestBody Map<String, String> param, ModelMap model) {
    	param.put("pgmId", request.getRequestURI());
    	cm02Svc.insertRole(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	return "jsonView";
    }
    
    // 롤삭제
    @DeleteMapping("/deleteRole")
    public String deleteRole(@RequestBody Map<String, String> param, ModelMap model) {
    	cm02Svc.deleteRole(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
    
    // 롤수정
    @PutMapping("/updateRole")
    public String updateRole(HttpServletRequest request, @RequestBody Map<String, String> param, ModelMap model) {
    	param.put("pgmId", request.getRequestURI());
    	cm02Svc.updateRole(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }

    // 롤메뉴수정
    @PutMapping("/updateRoleMenu")
    public String updateRoleMenu(@RequestBody Map<String, String> param, ModelMap model) {
    	cm02Svc.updateRoleMenu(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
    
}