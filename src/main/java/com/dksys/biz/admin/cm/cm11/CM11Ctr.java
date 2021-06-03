package com.dksys.biz.admin.cm.cm11;

import java.util.ArrayList;
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
	
    @PostMapping(value = "/selectDashBoardList")
	public String selectDashBoardList(@RequestBody Map<String, String> paramMap, ModelMap model) {
    	int totalCnt1 = cm11Svc.selectPrdtSelpch2Count(paramMap);
		PaginationInfo paginationInfo1 = new PaginationInfo(paramMap, totalCnt1);
    	model.addAttribute("paginationInfo1", paginationInfo1);
    	
    	String searchDttm = cm11Svc.selectSearchDttm();
    	model.addAttribute("searchDttm", searchDttm);
    	
    	List<Map<String, String>> resultList1 = cm11Svc.selectPrdtSelpch2List(paramMap);
    	model.addAttribute("resultList1", resultList1);

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
    	for(int i = 0; i < resultList1.size(); i++) {
    		JSONObject legend = new JSONObject();
    		legend.put("v", resultList1.get(i).get("prdtDivNm"));
    		legend.put("f", null);
    		JSONObject value1 = new JSONObject();
    		value1.put("v", resultList1.get(i).get("planWt"));
    		value1.put("f", null);
    		JSONObject value2 = new JSONObject();
    		value2.put("v", resultList1.get(i).get("realTrstQty"));
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
    	model.addAttribute("chartData1", data);
    	
    	
    	int totalCnt2 = cm11Svc.selectClntSelpch2Count(paramMap);
		PaginationInfo paginationInfo2 = new PaginationInfo(paramMap, totalCnt2);
    	model.addAttribute("paginationInfo2", paginationInfo2);
    	
    	List<Map<String, String>> resultList2 = cm11Svc.selectClntSelpch2List(paramMap);
    	model.addAttribute("resultList2", resultList2);
    	
    	data = new JSONObject();
    	objCols1 = new JSONObject();
    	objCols2 = new JSONObject();
    	objCols3 = new JSONObject();
    	arryCols = new JSONArray();
    	arryRows = new JSONArray();

    	objCols1.put("type", "string");
    	objCols2.put("type", "number");
    	objCols2.put("label", "계획");
    	objCols3.put("type", "number");
    	objCols3.put("label", "실적");

    	arryCols.add(objCols1);
    	arryCols.add(objCols2);
    	arryCols.add(objCols3);
    	for(int i = 0; i < resultList2.size(); i++) {
    		JSONObject legend = new JSONObject();
    		legend.put("v", resultList2.get(i).get("clntNm"));
    		legend.put("f", null);
    		JSONObject value1 = new JSONObject();
    		value1.put("v", resultList2.get(i).get("planWt"));
    		value1.put("f", null);
    		JSONObject value2 = new JSONObject();
    		value2.put("v", resultList2.get(i).get("realTrstQty"));
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
    	model.addAttribute("chartData2", data);
    	
    	
    	
    	int totalCnt3 = cm11Svc.selectClntSelpch1Count(paramMap);
		PaginationInfo paginationInfo3 = new PaginationInfo(paramMap, totalCnt3);
    	model.addAttribute("paginationInfo3", paginationInfo3);
    	List<Map<String, String>> resultList3 = new ArrayList<Map<String, String>>();
    	if(paramMap.get("option").equals("co")) {
    		resultList3 = cm11Svc.selectClntSelpch1List(paramMap);
    	} else {
    		resultList3 = cm11Svc.selectClntSelpch1List2(paramMap);

    	}
    	model.addAttribute("resultList3", resultList3);
    	data = new JSONObject();
    	objCols1 = new JSONObject();
    	objCols2 = new JSONObject();
    	arryCols = new JSONArray();
    	arryRows = new JSONArray();

    	objCols1.put("type", "string");
    	objCols1.put("label", "거래처");
    	objCols2.put("type", "number");
    	objCols2.put("label", "실적");

    	arryCols.add(objCols1);
    	arryCols.add(objCols2);
    	for(int i = 0; i < resultList3.size(); i++) {
    		JSONObject legend = new JSONObject();
    		legend.put("v", resultList3.get(i).get("clntNm"));
    		legend.put("f", null);
    		JSONObject value1 = new JSONObject();
    		value1.put("v", Integer.parseInt(resultList3.get(i).get("realTrstQty")));
    		value1.put("f", null);
    		
    		JSONArray cValueArry = new JSONArray();
    		cValueArry.add(legend);
    		cValueArry.add(value1);
    		
    		JSONObject cValueObj = new JSONObject();
    		cValueObj.put("c", cValueArry);
    		
    		arryRows.add(cValueObj);
    	}
    	
    	data.put("cols", arryCols);
    	data.put("rows", arryRows);
    	model.addAttribute("chartData3", data);
    	
    	int totalCnt4 = 3; //공장 세곳
		PaginationInfo paginationInfo4 = new PaginationInfo(paramMap, totalCnt4);
    	model.addAttribute("paginationInfo4", paginationInfo4);
    	
    	List<Map<String, String>> resultList4 = cm11Svc.selectFacList(paramMap);
    	model.addAttribute("resultList4", resultList4);
    	data = new JSONObject();
    	objCols1 = new JSONObject();
    	objCols2 = new JSONObject();
    	objCols3 = new JSONObject();
    	arryCols = new JSONArray();
    	arryRows = new JSONArray();

    	objCols1.put("type", "string");
    	objCols1.put("label", "공장");
    	objCols2.put("type", "number");
    	objCols2.put("label", "계획");
    	objCols3.put("type", "number");
    	objCols3.put("label", "실적");

    	arryCols.add(objCols1);
    	arryCols.add(objCols2);
    	arryCols.add(objCols3);
    	for(int i = 0; i < resultList4.size(); i++) {
    		JSONObject legend = new JSONObject();
    		legend.put("v", resultList4.get(i).get("facNm"));
    		legend.put("f", null);
    		JSONObject value1 = new JSONObject();
    		value1.put("v", resultList4.get(i).get("mmPlan"));
    		value1.put("f", null);
    		JSONObject value2 = new JSONObject();
    		value2.put("v", resultList4.get(i).get("mmWgt"));
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
    	model.addAttribute("chartData4", data);
    	
    	List<Map<String, String>> resultList5 = cm11Svc.selectMonthlyStat(paramMap);
    	model.addAttribute("resultList5", resultList5);
    	data = new JSONObject();
    	objCols1 = new JSONObject();
    	objCols2 = new JSONObject();
    	objCols3 = new JSONObject();
    	arryCols = new JSONArray();
    	arryRows = new JSONArray();

    	objCols1.put("type", "string");
    	objCols1.put("label", "월별");
    	objCols2.put("type", "number");
    	objCols2.put("label", "매출");
    	objCols3.put("type", "number");
    	objCols3.put("label", "매입");

    	arryCols.add(objCols1);
    	arryCols.add(objCols2);
    	arryCols.add(objCols3);
    	for(int i = 0; i < resultList5.size(); i++) {
    		JSONObject legend = new JSONObject();
    		legend.put("v", resultList5.get(i).get("trstDt"));
    		legend.put("f", null);
    		JSONObject value1 = new JSONObject();
    		value1.put("v", resultList5.get(i).get("selpch1Qty"));
    		value1.put("f", null);
    		JSONObject value2 = new JSONObject();
    		value2.put("v", resultList5.get(i).get("selpch2Qty"));
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
    	model.addAttribute("chartData5", data);
    	return "jsonView";
    }
	
	/*
	 * @PostMapping(value = "/selectPrdtSelpch2List") public String
	 * selectPrdtSelpch2List(@RequestBody Map<String, String> paramMap, ModelMap
	 * model) { int totalCnt = cm11Svc.selectPrdtSelpch2Count(paramMap);
	 * PaginationInfo paginationInfo = new PaginationInfo(paramMap, totalCnt);
	 * model.addAttribute("paginationInfo", paginationInfo);
	 * 
	 * List<Map<String, String>> resultList =
	 * cm11Svc.selectPrdtSelpch2List(paramMap); model.addAttribute("resultList",
	 * resultList);
	 * 
	 * JSONObject data = new JSONObject(); JSONObject objCols1 = new JSONObject();
	 * JSONObject objCols2 = new JSONObject(); JSONObject objCols3 = new
	 * JSONObject(); JSONArray arryCols = new JSONArray(); JSONArray arryRows = new
	 * JSONArray();
	 * 
	 * objCols1.put("type", "string"); objCols2.put("type", "number");
	 * objCols2.put("label", "계획"); objCols3.put("type", "number");
	 * objCols3.put("label", "실적");
	 * 
	 * arryCols.add(objCols1); arryCols.add(objCols2); arryCols.add(objCols3);
	 * for(int i = 0; i < resultList.size(); i++) { JSONObject legend = new
	 * JSONObject(); legend.put("v", resultList.get(i).get("prdtDivNm"));
	 * legend.put("f", null); JSONObject value1 = new JSONObject(); value1.put("v",
	 * resultList.get(i).get("trstQty")); value1.put("f", null); JSONObject value2 =
	 * new JSONObject(); value2.put("v", resultList.get(i).get("realTrstQty"));
	 * value2.put("f", null);
	 * 
	 * JSONArray cValueArry = new JSONArray(); cValueArry.add(legend);
	 * cValueArry.add(value1); cValueArry.add(value2);
	 * 
	 * JSONObject cValueObj = new JSONObject(); cValueObj.put("c", cValueArry);
	 * 
	 * arryRows.add(cValueObj); }
	 * 
	 * data.put("cols", arryCols); data.put("rows", arryRows);
	 * model.addAttribute("chartData", data); return "jsonView"; }
	 */
	
	/*
	 * @PostMapping(value = "/selectClntSelpch2List") public String
	 * selectOrdrgNList(@RequestBody Map<String, String> paramMap, ModelMap model) {
	 * int totalCnt = cm11Svc.selectClntSelpch2Count(paramMap); PaginationInfo
	 * paginationInfo = new PaginationInfo(paramMap, totalCnt);
	 * model.addAttribute("paginationInfo", paginationInfo);
	 * 
	 * List<Map<String, String>> resultList =
	 * cm11Svc.selectClntSelpch2List(paramMap); model.addAttribute("resultList",
	 * resultList);
	 * 
	 * JSONObject data = new JSONObject(); JSONObject objCols1 = new JSONObject();
	 * JSONObject objCols2 = new JSONObject(); JSONObject objCols3 = new
	 * JSONObject(); JSONArray arryCols = new JSONArray(); JSONArray arryRows = new
	 * JSONArray();
	 * 
	 * objCols1.put("type", "string"); objCols2.put("type", "number");
	 * objCols2.put("label", "계획"); objCols3.put("type", "number");
	 * objCols3.put("label", "실적");
	 * 
	 * arryCols.add(objCols1); arryCols.add(objCols2); arryCols.add(objCols3);
	 * for(int i = 0; i < resultList.size(); i++) { JSONObject legend = new
	 * JSONObject(); legend.put("v", resultList.get(i).get("clntNm"));
	 * legend.put("f", null); JSONObject value1 = new JSONObject(); value1.put("v",
	 * resultList.get(i).get("trstQty")); value1.put("f", null); JSONObject value2 =
	 * new JSONObject(); value2.put("v", resultList.get(i).get("realTrstQty"));
	 * value2.put("f", null);
	 * 
	 * JSONArray cValueArry = new JSONArray(); cValueArry.add(legend);
	 * cValueArry.add(value1); cValueArry.add(value2);
	 * 
	 * JSONObject cValueObj = new JSONObject(); cValueObj.put("c", cValueArry);
	 * 
	 * arryRows.add(cValueObj); }
	 * 
	 * data.put("cols", arryCols); data.put("rows", arryRows);
	 * model.addAttribute("chartData", data); return "jsonView"; }
	 */
	
	/*
	 * @PostMapping(value = "/selectClntSelpch1List") public String
	 * selectShipNList(@RequestBody Map<String, String> paramMap, ModelMap model) {
	 * int totalCnt = cm11Svc.selectClntSelpch1Count(paramMap); PaginationInfo
	 * paginationInfo = new PaginationInfo(paramMap, totalCnt);
	 * model.addAttribute("paginationInfo", paginationInfo);
	 * 
	 * List<Map<String, String>> resultList =
	 * cm11Svc.selectClntSelpch1List(paramMap); model.addAttribute("resultList",
	 * resultList); JSONObject data = new JSONObject(); JSONObject objCols1 = new
	 * JSONObject(); JSONObject objCols2 = new JSONObject(); JSONArray arryCols =
	 * new JSONArray(); JSONArray arryRows = new JSONArray();
	 * 
	 * objCols1.put("type", "string"); objCols1.put("label", "거래처");
	 * objCols2.put("type", "number"); objCols2.put("label", "매입금액");
	 * 
	 * arryCols.add(objCols1); arryCols.add(objCols2); for(int i = 0; i <
	 * resultList.size(); i++) { JSONObject legend = new JSONObject();
	 * legend.put("v", resultList.get(i).get("clntNm") + "/" +
	 * resultList.get(i).get("prdtDivNm")); legend.put("f", null); JSONObject value1
	 * = new JSONObject(); value1.put("v", resultList.get(i).get("realTrstAmt"));
	 * value1.put("f", null);
	 * 
	 * JSONArray cValueArry = new JSONArray(); cValueArry.add(legend);
	 * cValueArry.add(value1);
	 * 
	 * JSONObject cValueObj = new JSONObject(); cValueObj.put("c", cValueArry);
	 * 
	 * arryRows.add(cValueObj); }
	 * 
	 * data.put("cols", arryCols); data.put("rows", arryRows);
	 * model.addAttribute("chartData", data); return "jsonView"; }
	 */
	
	/*
	 * @PostMapping(value = "/selectFacList") public String
	 * selectFacList(@RequestBody Map<String, String> paramMap, ModelMap model) {
	 * int totalCnt = 3; //공장 세곳 PaginationInfo paginationInfo = new
	 * PaginationInfo(paramMap, totalCnt); model.addAttribute("paginationInfo",
	 * paginationInfo);
	 * 
	 * List<Map<String, String>> resultList = cm11Svc.selectFacList(paramMap);
	 * model.addAttribute("resultList", resultList); JSONObject data = new
	 * JSONObject(); JSONObject objCols1 = new JSONObject(); JSONObject objCols2 =
	 * new JSONObject(); JSONObject objCols3 = new JSONObject(); JSONArray arryCols
	 * = new JSONArray(); JSONArray arryRows = new JSONArray();
	 * 
	 * objCols1.put("type", "string"); objCols1.put("label", "공장");
	 * objCols2.put("type", "number"); objCols2.put("label", "계획");
	 * objCols3.put("type", "number"); objCols3.put("label", "실적");
	 * 
	 * arryCols.add(objCols1); arryCols.add(objCols2); arryCols.add(objCols3);
	 * for(int i = 0; i < resultList.size(); i++) { JSONObject legend = new
	 * JSONObject(); legend.put("v", resultList.get(i).get("facNm"));
	 * legend.put("f", null); JSONObject value1 = new JSONObject(); value1.put("v",
	 * resultList.get(i).get("mmPlan")); value1.put("f", null); JSONObject value2 =
	 * new JSONObject(); value2.put("v", resultList.get(i).get("mmWgt"));
	 * value2.put("f", null);
	 * 
	 * JSONArray cValueArry = new JSONArray(); cValueArry.add(legend);
	 * cValueArry.add(value1); cValueArry.add(value2);
	 * 
	 * JSONObject cValueObj = new JSONObject(); cValueObj.put("c", cValueArry);
	 * 
	 * arryRows.add(cValueObj); }
	 * 
	 * data.put("cols", arryCols); data.put("rows", arryRows);
	 * model.addAttribute("chartData", data); return "jsonView"; }
	 */
}