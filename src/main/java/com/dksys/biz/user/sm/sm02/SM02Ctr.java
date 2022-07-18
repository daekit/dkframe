package com.dksys.biz.user.sm.sm02;

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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.sm.sm02.service.SM02Svc;
import com.dksys.biz.util.MessageUtils;

@Controller
@RequestMapping("/user/sm/sm02")
public class SM02Ctr {
 
	//단가 관리 컨트롤러 UNIT PRICE -> UPR 표기
	
	@Autowired
	MessageUtils messageUtils;
	
    @Autowired
    SM02Svc sm02svc;
    
    //검색 조건 공통 코드 조회
    @PostMapping("/selectCmnCodeList")
    public String selectCmnCodeList(@RequestBody Map<String, String> param, ModelMap model) {
    	List<Map<String, String>> cmnCodeInfo = sm02svc.selectCmnCodeList(param);
    	model.addAttribute("cmnCodeInfo", cmnCodeInfo);
    	return "jsonView";
    }
    
  //재고 이동 공통 코드 조회
    @PostMapping("/selectDtlCmnCodeList")
    public String selectDtlCmnCodeList(@RequestBody Map<String, String> param, ModelMap model) {
    	List<Map<String, String>> cmnDtlCodeInfo = sm02svc.selectDtlCmnCodeList(param);
    	model.addAttribute("cmnDtlCodeInfo", cmnDtlCodeInfo);
    	return "jsonView";
    }
    
    //거래처 마스터 테이블 조회
    @PostMapping("/selectClntSearchList")
    public String selectClntSearchList(@RequestBody Map<String, String> param, ModelMap model) {
    	List<Map<String, String>> scslList = sm02svc.selectClntSearchList(param);
    	model.addAttribute("scslList", scslList);
    	return "jsonView";
    }
    //제품 마스터 테이블 조회
    @PostMapping("/selectPrdtSearchList")
    public String selectPrdtSearchList(@RequestBody Map<String, String> param, ModelMap model) {
    	List<Map<String, String>> spslList = sm02svc.selectPrdtSearchList(param);
    	model.addAttribute("spslList", spslList);
    	return "jsonView";
    }

    //재고 이동 현황 조회
    @PostMapping("/selectStockMoveStatMngmList")
    public String selectStockMoveStatMngmList(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = sm02svc.selectUprCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    
    	List<Map<String, String>> smsmList = sm02svc.selectStockMoveStatMngmList(param);
    	model.addAttribute("smsmList", smsmList);
        return "jsonView";
    }
    
    //재고 이동 현황 팝업 조회
    @PostMapping("/selectStockMoveStatMngmDtlList")
    public String selectStockMoveStatMngmDtlList(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = sm02svc.selectUprDtlCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    
    	List<Map<String, String>> smsmDtlList = sm02svc.selectStockMoveStatMngmDtlList(param);
    	model.addAttribute("smsmDtlList", smsmDtlList);
        return "jsonView";
    }
    
    //재고 이동 현황 팝업 조회
    @PostMapping("/selectStockTernKeykMovePchList")
    public String selectStockTernKeykMovePchList(@RequestBody Map<String, String> param, ModelMap model) {
    	int totalCnt = sm02svc.selectStockTernKeykMovePchListCount(param);
    	PaginationInfo paginationInfo = new PaginationInfo(param, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    
    	List<Map<String, String>> smsmDtlList = sm02svc.selectStockTernKeykMovePchList(param);
    	model.addAttribute("smsmDtlList", smsmDtlList);
        return "jsonView";
    }
    
    //일반 재고 이동 등록
    @PutMapping("/insertUpdateStockMove")
    public String insertUpdateStockMove(@RequestBody Map<String, String> param, ModelMap model) {
		int result = sm02svc.sm01UpdateInsertStockMove(param);    	
		if(result == 0) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("exceedLoan"));
		} else if(result == 500) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("stockClose"));
		} else {
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
		}
		return "jsonView";
	}
    
    //제강사 턴키 재고 이동 등록
    @PutMapping("/insertUpdateTernKeyStockMove")
    public String insertUpdateTernKeyStockMove(@RequestBody Map<String, String> param, ModelMap model) {
		int result = sm02svc.insertUpdateTernKeyStockMove(param);    	
		if(result == 0) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("exceedLoan"));
		} else if(result == 500) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("stockClose"));
		} else {
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
		}
		return "jsonView";
	}
    
    //바터 재고 이동 등록
    @PutMapping("/insertUpdateBarterStockMove")
    public String insertUpdateBarterStockMove(@RequestBody Map<String, String> param, ModelMap model) {
		int result = sm02svc.sm01UpdateInsertBarterStockMove(param);    	
		if(result == 0) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("exceedLoan"));
		} else if(result == 500) {
			model.addAttribute("resultCode", 500);
			model.addAttribute("resultMessage", messageUtils.getMessage("stockClose"));
		} else {
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
		}
		return "jsonView";
	}
    
	//재고이동 상세 비고 수정
    @PutMapping(value = "/updateStockMoveRmk")
	public String updateStockMoveRmk(@RequestBody Map<String, String> param, ModelMap model) {
		try {
			 sm02svc.updateStockMoveRmk(param);
			 model.addAttribute("resultUpr", 200);
			 model.addAttribute("resultMessage", messageUtils.getMessage("update"));
	   	}catch(Exception e) {
		    	 model.addAttribute("resultCode", 500);
		 		 model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
	   	}
    	return "jsonView";
    }
    
	
    //재고 운송비 update
	@PutMapping(value = "/updateStockMoveCaryng")
    public String updateStockMoveCaryng(@RequestBody Map<String, String> param, ModelMap model) {
		try {
			sm02svc.updateStockMoveCaryng(param);
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
		}catch (Exception e) {
			model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
    	return "jsonView";
    }
	
    //재고 운송비 update
	@PostMapping(value = "/deleteStock")
    public String deleteStock(@RequestBody Map<String, String> param, ModelMap model) {
		try {
			sm02svc.deleteStock(param);
	    	model.addAttribute("resultCode", 200);
	    	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
		}catch (Exception e) {
			model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
		}
    	return "jsonView";
    }
}