package com.dksys.biz.user.fi.fi02;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dksys.biz.cmn.vo.PaginationInfo;
import com.dksys.biz.user.fi.fi02.service.FI02Svc;
import com.dksys.biz.util.MessageUtils;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@Controller
@RequestMapping("/user/fi/fi02")
public class FI02Ctr {

	@Autowired
	MessageUtils messageUtils;
    
    @Autowired
    FI02Svc fi02Svc;
	
    @PostMapping(value = "/selectPalBillList")
	public String selectPalBillList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt = fi02Svc.selectPalBillCount(paramMap);
		PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
    	model.addAttribute("paginationInfo", paginationInfo);
    	
    	List<Map<String, String>> resultList = fi02Svc.selectPalBillList(paramMap);
    	model.addAttribute("resultList", resultList);
    	return "jsonView";
	}
	
    @PostMapping(value = "/selectDeptOptionList")
	public String selectDeptOptionList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> baseDeptOption = fi02Svc.selectBaseDept(paramMap);
    	List<Map<String, String>> upperDeptOption = fi02Svc.selectUpperDept(paramMap);
    	List<Map<String, String>> lastDeptOption = fi02Svc.selecrLastDept(paramMap);
    	model.addAttribute("baseDeptOption", baseDeptOption);
    	model.addAttribute("upperDeptOption", upperDeptOption);
    	model.addAttribute("lastDeptOption", lastDeptOption);
    	return "jsonView";
	}
	
    @PostMapping(value = "/selectUpperDept")
	public String selectUpperDept(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> upperDeptList = fi02Svc.selectUpperDept(paramMap);
    	List<Map<String, String>> lastDeptList = fi02Svc.selecrLastDept(paramMap);
    	model.addAttribute("upperDeptList", upperDeptList);
    	model.addAttribute("lastDeptList", lastDeptList);
    	return "jsonView";
	}
    
    @PostMapping(value = "/selectPalBillDetail")
	public String selectPalBillDetail(@RequestBody Map<String, String> paramMap, ModelMap model) {    	
    	Map<String, String> result = fi02Svc.selectPalBillDetail(paramMap);
    	model.addAttribute("result", result);
    	return "jsonView";
	}
    
    @PostMapping(value = "/insertPalBill")
    public String insertPalBill(@RequestParam Map<String, String> paramMap, ModelMap model) {
    	try {
    		int duplicate = fi02Svc.selectPrdtDeptDuplicate(paramMap);
    		if(duplicate > 0 ) {
            	model.addAttribute("resultCode", 999);
            	model.addAttribute("resultMessage", "중복 에러입니다. 키 값을 확인해주세요.");
    		} else {
    			fi02Svc.insertPalBill(paramMap);
            	model.addAttribute("resultCode", 200);
            	model.addAttribute("resultMessage", messageUtils.getMessage("insert"));	
    		}
    		
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
    
    @PostMapping(value = "/updatePalBill")
    public String updateCplrUntpc(@RequestParam Map<String, String> paramMap, ModelMap model) {
    	try {
    		fi02Svc.updatePalBill(paramMap);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("update"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }
    
    @PutMapping(value = "/deletePalBill")
    public String deleteCplrUntpc(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		fi02Svc.deletePalBill(paramMap);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", messageUtils.getMessage("delete"));
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }  
    
    @PutMapping(value = "/savePalBill")
    public String saveCplrUntpc(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		fi02Svc.savePalBill(paramMap);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", "저장되었습니다.");
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }  
    
    @PutMapping(value = "/copyPrevMonth")
    public String copyPrevMonth(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	try {
    		fi02Svc.copyPrevMonth(paramMap);
        	model.addAttribute("resultCode", 200);
        	model.addAttribute("resultMessage", "저장되었습니다.");
    	}catch (Exception e){
    		model.addAttribute("resultCode", 500);
    		model.addAttribute("resultMessage", messageUtils.getMessage("fail"));
    	}
    	return "jsonView";
    }    
    
    @PostMapping(value = "/selectPalBillStat")
	public String selectPalBillStat(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	List<Map<String, String>> selectPalBillSalesPrft = fi02Svc.selectPalBillSalesPrft(paramMap);
    	model.addAttribute("resultList1", selectPalBillSalesPrft);

    	List<Map<String, String>> selectPalBillSalesPrftChart = fi02Svc.selectPalBillSalesPrftChart(paramMap);
    	JSONObject data = new JSONObject();
    	JSONObject objCols1 = new JSONObject();
    	JSONObject objCols2 = new JSONObject();
    	JSONObject objCols3 = new JSONObject();
    	JSONObject objCols4 = new JSONObject();
    	JSONArray arryCols = new JSONArray();
    	JSONArray arryRows = new JSONArray();

    	objCols1.put("type", "string");
    	objCols2.put("type", "number");
    	objCols2.put("label", "금문철강(주)");
    	objCols4.put("type", "number");
    	objCols4.put("label", "지지엠");
    	objCols3.put("type", "number");
    	objCols3.put("label", "지지스틸");

    	arryCols.add(objCols1);
    	arryCols.add(objCols2);
    	arryCols.add(objCols3);
    	arryCols.add(objCols4);
    	for(int i = 0; i < selectPalBillSalesPrftChart.size(); i++) {
    		JSONObject legend = new JSONObject();
    		legend.put("v", selectPalBillSalesPrftChart.get(i).get("prftYm"));
    		legend.put("f", null);
    		JSONObject value1 = new JSONObject();
    		value1.put("v", selectPalBillSalesPrftChart.get(i).get("gum"));
    		value1.put("f", null);
    		JSONObject value2 = new JSONObject();
    		value2.put("v", selectPalBillSalesPrftChart.get(i).get("ggm"));
    		value2.put("f", null);
    		JSONObject value3 = new JSONObject();
    		value3.put("v", selectPalBillSalesPrftChart.get(i).get("ggs"));
    		value3.put("f", null);
    		
    		JSONArray cValueArry = new JSONArray();
    		cValueArry.add(legend);
    		cValueArry.add(value1);
    		cValueArry.add(value2);
    		cValueArry.add(value3);
    		
    		JSONObject cValueObj = new JSONObject();
    		cValueObj.put("c", cValueArry);
    		
    		arryRows.add(cValueObj);
    	}
    	
    	data.put("cols", arryCols);
    	data.put("rows", arryRows);
    	model.addAttribute("chartData1", data);
    	

    	List<Map<String, String>> selectPalBillBfrxPrft = fi02Svc.selectPalBillBfrxPrft(paramMap);
		PaginationInfo paginationInfo2 = new PaginationInfo(paramMap, selectPalBillBfrxPrft.size());
    	model.addAttribute("resultList2", selectPalBillBfrxPrft);

    	List<Map<String, String>> selectPalBillBfrxPrftChart = fi02Svc.selectPalBillBfrxPrftChart(paramMap);
    	data = new JSONObject();
    	objCols1 = new JSONObject();
    	objCols2 = new JSONObject();
    	objCols3 = new JSONObject();
    	objCols4 = new JSONObject();
    	arryCols = new JSONArray();
    	arryRows = new JSONArray();

    	objCols1.put("type", "string");
    	objCols2.put("type", "number");
    	objCols2.put("label", "금문철강(주)");
    	objCols4.put("type", "number");
    	objCols4.put("label", "지지엠");
    	objCols3.put("type", "number");
    	objCols3.put("label", "지지스틸");

    	arryCols.add(objCols1);
    	arryCols.add(objCols2);
    	arryCols.add(objCols3);
    	arryCols.add(objCols4);
    	for(int i = 0; i < selectPalBillBfrxPrftChart.size(); i++) {
    		JSONObject legend = new JSONObject();
    		legend.put("v", selectPalBillBfrxPrftChart.get(i).get("prftYm"));
    		legend.put("f", null);
    		JSONObject value1 = new JSONObject();
    		value1.put("v", selectPalBillBfrxPrftChart.get(i).get("gum"));
    		value1.put("f", null);
    		JSONObject value2 = new JSONObject();
    		value2.put("v", selectPalBillBfrxPrftChart.get(i).get("ggm"));
    		value2.put("f", null);
    		JSONObject value3 = new JSONObject();
    		value3.put("v", selectPalBillBfrxPrftChart.get(i).get("ggs"));
    		value3.put("f", null);
    		
    		JSONArray cValueArry = new JSONArray();
    		cValueArry.add(legend);
    		cValueArry.add(value1);
    		cValueArry.add(value2);
    		cValueArry.add(value3);
    		
    		JSONObject cValueObj = new JSONObject();
    		cValueObj.put("c", cValueArry);
    		
    		arryRows.add(cValueObj);
    	}
    	
    	data.put("cols", arryCols);
    	data.put("rows", arryRows);
    	model.addAttribute("chartData2", data);
    	return "jsonView";
    }
}