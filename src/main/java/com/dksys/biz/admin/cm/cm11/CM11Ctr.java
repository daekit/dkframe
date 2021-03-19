package com.dksys.biz.admin.cm.cm11;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dksys.biz.admin.cm.cm11.service.CM11Svc;
import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.util.MessageUtils;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@Controller
@RequestMapping("/user/cm/cm11")
public class CM11Ctr {
	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    CM11Svc cm11Svc;
	
    @PostMapping(value = "/selectPrdtSelpch2List")
	public String selectPrdtSelpch2List(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = cm11Svc.selectPrdtSelpch2Count(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = cm11Svc.selectPrdtSelpch2List(paramMap);
    	model.addAttribute("resultList", resultList);

    	JSONObject data = new JSONObject();
    	JSONObject objCols1 = new JSONObject();
    	JSONObject objCols2 = new JSONObject();
    	JSONObject objCols3 = new JSONObject();
    	JSONArray arryCols = new JSONArray();
    	JSONArray arryRows = new JSONArray();

    	objCols1.put("type", "string");
    	objCols2.put("type", "number");
    	objCols2.put("label", "계획");
    	objCols3.put("type", "number");
    	objCols3.put("label", "실적");

    	arryCols.add(objCols1);
    	arryCols.add(objCols2);
    	arryCols.add(objCols3);
    	System.out.println("resultList : " + resultList);
    	System.out.println(resultList);
    	for(int i = 0; i < resultList.size(); i++) {
    		JSONObject legend = new JSONObject();
    		legend.put("v", resultList.get(i).get("prdtNm"));
    		legend.put("f", null);
    		JSONObject value1 = new JSONObject();
    		value1.put("v", resultList.get(i).get("trstQty"));
    		value1.put("f", null);
    		JSONObject value2 = new JSONObject();
    		value2.put("v", resultList.get(i).get("realTrstQty"));
    		value2.put("f", null);
    		
    		JSONArray cValueArry = new JSONArray();
    		cValueArry.add(legend);
    		cValueArry.add(value1);
    		cValueArry.add(value2);
    		
    		JSONObject cValueObj = new JSONObject();
    		cValueObj.put("c", cValueArry);
    		
    		arryRows.add(cValueObj);
    	}
    	
    	data.put("cols", arryCols);
    	data.put("rows", arryRows);
    	System.out.println(data);
    	model.addAttribute("chartData", data);
    	return "jsonView";
	}
	
    @PostMapping(value = "/selectClntSelpch2List")
	public String selectOrdrgNList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = cm11Svc.selectClntSelpch2Count(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = cm11Svc.selectClntSelpch2List(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
	
    @PostMapping(value = "/selectClntSelpch1List")
	public String selectShipNList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = cm11Svc.selectClntSelpch1Count(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = cm11Svc.selectClntSelpch1List(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
}