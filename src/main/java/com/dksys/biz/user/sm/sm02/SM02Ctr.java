package com.dksys.biz.user.sm.sm02;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
    
    //재고 이동 등록
    @PutMapping("/insertUpdateStockMove")
    public String insertUpdateStockMove(@RequestBody Map<String, String> param, ModelMap model) {
    	//int result = sm02svc.sm01CheckCnt(param);
		/*
		 * if(result == 0) { sm02svc.sm01InsertStockMove(param); }else {
		 * sm02svc.sm01UpdateStockMove(param); sm02svc.sm01UpdateInsertStockMove(param);
		 * }
		 */
    	sm02svc.sm01UpdateInsertStockMove(param);
    	sm02svc.sm01UpdateStockMove(param);
    	sm02svc.sm02InsertStockMove(param);
    	model.addAttribute("resultCode", 200);
    	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));
    	
    	return "jsonView";
    }
       
}