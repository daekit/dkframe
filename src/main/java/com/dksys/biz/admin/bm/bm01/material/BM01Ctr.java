package com.dksys.biz.admin.bm.bm01.material;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.dksys.biz.admin.bm.bm01.service.BM01Svc;
import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/admin/bm/bm01")
public class BM01Ctr {

	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    BM01Svc bm01Svc;
	
	@PostMapping(value = "/selectMaterialList")
    public String selectMaterialList(@RequestBody Map<String, String> param, ModelMap model) {
		int totalCnt = bm01Svc.selectMaterialCount(param);
		PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
		List<Map<String, String>> materialList = bm01Svc.selectMaterialList(param);
		model.addAttribute("materialList", materialList);
        return "jsonView";
    }
	
	@PostMapping(value = "/selectMaterialInfo")
	public String selectMaterialInfo(@RequestBody Map<String, String> param, ModelMap model) {
		Map<String, String> materialInfo = bm01Svc.selectMaterialInfo(param);
		model.addAttribute("materialInfo", materialInfo);
		return "jsonView";
	}
	
	@PostMapping(value = "/checkOverLap")
    public String checkOverLap(@RequestBody Map<String, String> param, ModelMap model) {
		int result = bm01Svc.checkOverLap(param);
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
		bm01Svc.insertMaterial(param);
		model.addAttribute("resultCode", 200);
		model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
		return "jsonView";
	}
	
	@PutMapping(value = "/updateMaterial")
	public String updateMaterial(@RequestBody Map<String, String> param, ModelMap model) {
		bm01Svc.updateMaterial(param);
		model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
		return "jsonView";
	}
	
	@DeleteMapping(value = "/deleteMaterial")
	public String deleteMaterial(@RequestBody Map<String, String> param, ModelMap model) {
		bm01Svc.deleteMaterial(param);
		model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
		return "jsonView";
	} 
	
}