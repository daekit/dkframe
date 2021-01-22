package com.dksys.biz.admin.bm.material;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.admin.bm.material.service.MaterialService;
import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/admin/bm/material")
public class MaterialController {

	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    MaterialService materialService;
	
	@PostMapping(value = "/selectMaterialList")
    public String selectMaterialList(@RequestBody Map<String, String> param, ModelMap model) {
		int totalCnt = materialService.selectMaterialCount(param);
		PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
		List<Map<String, String>> materialList = materialService.selectMaterialList(param);
		model.addAttribute("materialList", materialList);
        return "jsonView";
    }
	
	@PostMapping(value = "/selectMaterialInfo")
	public String selectMaterialInfo(@RequestBody Map<String, String> param, ModelMap model) {
		Map<String, String> materialInfo = materialService.selectMaterialInfo(param);
		model.addAttribute("materialInfo", materialInfo);
		return "jsonView";
	}

	@PostMapping(value = "/checkOverLap")
    public String checkOverLap(@RequestBody Map<String, String> param, ModelMap model) {
		int result = materialService.checkOverLap(param);
		if(result > 0) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("exist"));
		} else {
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("check"));
		}
		model.addAttribute("result", result);
        return "jsonView";
    }
	
	@PostMapping(value = "/insertMaterial")
	public String insertMaterial(@RequestBody Map<String, String> param, ModelMap model) {
		materialService.insertMaterial(param);
		model.addAttribute("resultCode", 200);
		model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
		return "jsonView";
	}
	
	@DeleteMapping(value = "/deleteMaterial")
	public String deleteMaterial(@RequestBody Map<String, String> param, ModelMap model) {
		materialService.deleteMaterial(param);
		model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
		return "jsonView";
	} 
	
}