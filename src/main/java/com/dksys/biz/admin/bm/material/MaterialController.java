package com.dksys.biz.admin.bm.material;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.admin.bm.material.service.MaterialService;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/admin/bm/material")
public class MaterialController {

	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    MaterialService materialService;
	
	@PostMapping(value = "/selectMaterialList")
    public String selectMaterialList(ModelMap model) {
		List<Map<String, String>> materialList = materialService.selectMaterialList();
		model.addAttribute("materialList", materialList);
        return "jsonView";
    }
	
}