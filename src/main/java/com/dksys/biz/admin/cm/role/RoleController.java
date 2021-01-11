package com.dksys.biz.admin.cm.role;

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

import com.dksys.biz.admin.cm.role.service.RoleService;
import com.dksys.biz.util.MessageUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/cm/role")
public class RoleController {

	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    RoleService roleService;
    
    // 롤리스트 조회
    @PostMapping("/selectRoleInfo")
    public String selectRoleInfo(ModelMap model) {
    	List<Map<String, String>> roleList = roleService.selectRoleList();
    	model.addAttribute("roleList", roleList);
        return "jsonView";
    }
    
    // 롤등록
    @PostMapping("/createRole")
    public String createRole(@RequestBody Map<String, String> param, ModelMap model) {
    	roleService.insertRole(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	return "jsonView";
    }
    
    // 롤삭제
    @DeleteMapping("/deleteRole")
    public String deleteRole(@RequestBody Map<String, String> param, ModelMap model) {
    	roleService.deleteRole(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	return "jsonView";
    }
    
    // 롤수정
    @PutMapping("/updateRole")
    public String updateRole(@RequestBody Map<String, String> param, ModelMap model) {
    	roleService.updateRole(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }

    // 롤메뉴수정
    @PutMapping("/updateRoleMenu")
    public String updateRoleMenu(@RequestBody Map<String, String> param, ModelMap model) {
    	roleService.updateRoleMenu(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
    
}