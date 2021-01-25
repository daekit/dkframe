package com.dksys.biz.admin.bm.customer;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.admin.bm.material.service.MaterialService;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/admin/bm/customer")
public class CustomerController {

	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    MaterialService materialService;
	
	@PostMapping(value = "/selectCustomerList")
    public String selectCustomerList(@RequestBody Map<String, String> param, ModelMap model) {
		List<Map<String, String>> materialList = materialService.selectMaterialList(param);
		model.addAttribute("materialList", materialList);
        return "jsonView";
    }
    
}