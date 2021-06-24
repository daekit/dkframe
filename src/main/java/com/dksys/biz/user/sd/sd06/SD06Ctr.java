package com.dksys.biz.user.sd.sd06;

import java.util.HashMap;
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

import com.dksys.biz.user.sd.sd06.service.SD06Svc;
import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/sd/sd06")
public class SD06Ctr {
 
	//단가 관리 컨트롤러 UNIT PRICE -> UPR 표기
	
	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    SD06Svc sd06svc;
    
    //단가관리 리스트 조회
    @PostMapping("/selectUprList")
    public String selectUprList(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = sd06svc.selectUprCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    
    	List<Map<String, String>> uprList = sd06svc.selectUprList(param);
    	model.addAttribute("uprList", uprList);
        return "jsonView";
    }
    //단가관리 중복 조회
    @PostMapping("/checkOverLapMaster")
    public String checkOverLapMaster(@RequestBody Map<String, String> param, ModelMap model) {
    	int result = sd06svc.selectOneMasterCount(param);
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
    
    //강종별 할증금액 리스트 조회
    @PostMapping("/checkOverLapDetail01")
    public String checkOverLapDetail01(@RequestBody Map<String, String> param, ModelMap model) {
    	int result = sd06svc.selectDetail01Count(param);
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
    
    //사이즈별 할증금액 리스트 조회
    @PostMapping("/checkOverLapDetail02")
    public String checkOverLapDetail02(@RequestBody Map<String, String> param, ModelMap model) {
    	int result = sd06svc.selectDetail02Count(param);
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
    // 
//    @PostMapping("/insertUpr")
//    public String createUpr(@RequestBody Map<String, String> param, ModelMap model) {
//    	sd06svc.insertUpr(param);
//    	model.addAttribute("resultUpr", 200);
//    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
//    	return "jsonView";
//    }
//    
//    // 
//    @DeleteMapping("/deleteUpr")
//    public String deleteUpr(@RequestBody Map<String, String> param, ModelMap model) {
//    	sd06svc.deleteUpr(param);
//    	model.addAttribute("resultUpr", 200);
//    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
//    	return "jsonView";
//    }
//    
//    // 
//    @PutMapping("/updateUpr")
//    public String updateUpr(@RequestBody Map<String, String> param, ModelMap model) {
//    	sd06svc.updateUpr(param);
//    	model.addAttribute("resultUpr", 200);
//    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
//    	return "jsonView";
//    }
    
        
    //강종별 할증금액 리스트 조회
    @PostMapping("/selectDetail01List")
    public String selectDetail01List(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = sd06svc.selectDetail01Count(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    
    	List<Map<String, String>> detail01List = sd06svc.selectDetail01List(param);
    	model.addAttribute("detail01List", detail01List);
        return "jsonView";
    }

    
  //사이즈별 할증금액 조회
    @PostMapping("/selectDetail02List")
    public String selectDetail02List(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = sd06svc.selectDetail02Count(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    
    	List<Map<String, String>> detail02List = sd06svc.selectDetail02List(param);
    	model.addAttribute("detail02List", detail02List);
        return "jsonView";
    }
    
    
    //자재 기준 단가관리 상세조회
    @PostMapping("/seletOneMaster")
    public String seletOneMaster(@RequestBody Map<String, String> param, ModelMap model) {
    	Map<String, String> detailInfo = sd06svc.seletOneMaster(param);
    	model.addAttribute("detailInfo", detailInfo);
    	return "jsonView";
    }
    
    //강종별 할증금액 상세 조회
    @PostMapping("/seletOneDetail01")
    public String seletOneDetail01(@RequestBody Map<String, String> param, ModelMap model) {
    	Map<String, String> detailInfo = sd06svc.seletOneDetail01(param);
    	model.addAttribute("detailInfo", detailInfo);
    	return "jsonView";
    }
    
    //사이즈별 할증금액 조회
    @PostMapping("/seletOneDetail02")
    public String seletOneDetail02(@RequestBody Map<String, String> param, ModelMap model) {
    	Map<String, String> detailInfo = sd06svc.seletOneDetail02(param);
    	model.addAttribute("detailInfo", detailInfo);
    	return "jsonView";
    }
    
    
    //자재 기준 단가관리 수정 및 등록
    @PutMapping("/insertOneMaster")
    public String insertOneMaster(@RequestBody Map<String, String> param, ModelMap model) {
    	
    	Map<String, String> tempParam = new HashMap<String, String>();
    	
    	tempParam.putAll(param);
    	tempParam.put("prdtDt", "");
    	
    	int count = sd06svc.selectOneMasterCount(tempParam);
    	//기존 기준단가 row가 있을 시, 강종, 철근 detail 자동저장
    	if(count > 0) {
    		sd06svc.insertOneMaster(param);
    		sd06svc.updateOneDetail01(param);
    		sd06svc.updateOneDetail02(param);
    		
    	}else {
    		
    		sd06svc.insertOneMaster(param);
        	
    	}
    	
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
    
    //강종별 할증금액 수정 및 등록
    @PutMapping("/insertOneDetail01")
    public String insertOneDetail01(@RequestBody Map<String, String> param, ModelMap model) {
    	sd06svc.insertOneDetail01(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
    //사이즈별 할증금액 수정 및 등록
    @PutMapping("/insertOneDetail02")
    public String insertOneDetail02(@RequestBody Map<String, String> param, ModelMap model) {
    	sd06svc.insertOneDetail02(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
    
//  ----- 거래처별 단가관리 시작 ---
    
    //단가관리 리스트 조회
    @PostMapping("/selectUprClntList")
    public String selectUprClntList(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = sd06svc.selectUprClntCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    
    	List<Map<String, String>> uprList = sd06svc.selectUprClntList(param);
    	model.addAttribute("uprList", uprList);
        return "jsonView";
    }
    //단가관리 중복 조회
    @PostMapping("/checkOverLapMasterClnt")
    public String checkOverLapMasterClnt(@RequestBody Map<String, String> param, ModelMap model) {
    	int result = sd06svc.selectOneMasterClntCount(param);
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
   
    //사용중지 Update
    @PutMapping("/updateUseYnClnt")
    public String updateUseYnClnt(@RequestBody Map<String, String> param, ModelMap model) {
    	int result = sd06svc.updateUseYnClnt(param);
    	if(result > 0) {
			model.addAttribute("resultCode", 200);
			model.addAttribute("resultMessage", messageUtils.getMessage("success"));
		} else {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
		model.addAttribute("result", result);
    	
		return "jsonView";
    }
   
    
    //자재 기준 단가관리 상세조회
    @PostMapping("/seletOneMasterClnt")
    public String seletOneMasterClnt(@RequestBody Map<String, String> param, ModelMap model) {
    	Map<String, String> detailInfo = sd06svc.seletOneMasterClnt(param);
    	model.addAttribute("detailInfo", detailInfo);
    	return "jsonView";
    }    
    
    //자재 기준 단가관리 수정 및 등록
    @PutMapping("/insertOneMasterClnt")
    public String insertOneMasterClnt(@RequestBody Map<String, String> param, ModelMap model) {
 
    	sd06svc.insertOneMasterClnt(param);

    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	return "jsonView";
    }
    
}