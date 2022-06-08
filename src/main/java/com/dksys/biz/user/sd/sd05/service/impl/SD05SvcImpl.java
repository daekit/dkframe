package com.dksys.biz.user.sd.sd05.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.cm.cm08.service.CM08Svc;
import com.dksys.biz.admin.cm.cm09.service.CM09Svc;
import com.dksys.biz.user.sd.sd05.mapper.SD05Mapper;
import com.dksys.biz.user.sd.sd05.service.SD05Svc;
import com.dksys.biz.user.sd.sd09.mapper.SD09Mapper;
import com.dksys.biz.user.sd.sd09.service.SD09Svc;
import com.dksys.biz.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional(rollbackFor = Exception.class)
public class SD05SvcImpl implements SD05Svc {
	
	@Autowired
	CM08Svc cm08Svc;
	
	@Autowired
	CM09Svc cm09Svc;
	
	@Autowired
    SD09Svc sd09Svc;

    @Autowired
    SD05Mapper sd05Mapper;
    
    @Autowired
    SD09Mapper sd09Mapper;
	
	@Override
	public List<Map<String, String>> selectProjectList(Map<String, String> param) {
		return sd05Mapper.selectProjectList(param);
	}
	
	@Override
	public List<Map<String, String>> selectProjectcoCdList(Map<String, String> param) {
		return sd05Mapper.selectProjectcoCdList(param);
	}
	
	@Override
	public List<Map<String, String>> selectProjectNameList(Map<String, String> param) {
		return sd05Mapper.selectProjectNameList(param);
	}
	
	@Override
	public List<Map<String, String>> selectProjectClntList(Map<String, String> param) {
		return sd05Mapper.selectProjectClntList(param);
	}
	
	@Override
	public Map<String, String> selectProjectKeyList(Map<String, String> param) {
		return sd05Mapper.selectProjectKeyList(param);
	}
	
	@Override
	public int selectChkOrdrgYn(Map<String, String> param) {
		return sd05Mapper.selectChkOrdrgYn(param);
	}
	
	@Override
	public int selectProjectCount(Map<String, String> param) {
		return sd05Mapper.selectProjectCount(param);
	}
	
	@Override
	public Map<String, Object> selectPrjInfo(Map<String, String> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, String> fileMap = new HashMap<String, String>();
		fileMap.put("fileTrgtType", "TB_SD05M01");
		fileMap.put("fileTrgtKey", paramMap.get("prjctCd"));
		returnMap.put("fileList",       cm08Svc.selectFileList(fileMap));
		returnMap.put("prjInfo",        sd05Mapper.selectPrjInfo(paramMap));
		returnMap.put("ordDetail",      sd05Mapper.selectOrdDetail(paramMap));
		returnMap.put("shipmentDetail", sd05Mapper.selectShipmentDetail(paramMap));
		returnMap.put("prjctDtl",       sd05Mapper.selectProjectDtl(paramMap));
		return returnMap;
	}
	
	@Override
	public int insertProject(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		int result = sd05Mapper.insertProject(paramMap);
		paramMap.put("siteNm",      paramMap.get("prjctNm"));
		paramMap.put("siteAddrZip", paramMap.get("prjctAddrZip"));
		paramMap.put("siteAddr",    paramMap.get("prjctAddr"));
		paramMap.put("siteAddrSub", paramMap.get("prjctAddrSub"));
		paramMap.put("siteMngNm",   paramMap.get("prjctMngNm"));
//		String siteCd = sd09Svc.insertSite(paramMap);
		String siteCd = sd09Mapper.selectSiteCd(paramMap); 
		paramMap.put("siteCd", siteCd);
		sd09Mapper.insertSite(paramMap);		
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type dtlMap = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> dtlParam = gson.fromJson(paramMap.get("detailArr"), dtlMap);
		
		for(Map<String, String> dtl : dtlParam) {
			dtl.put("coCd",    paramMap.get("coCd"));
			dtl.put("prjctCd", paramMap.get("prjctCd"));
			dtl.put("siteCd",  siteCd);
			dtl.put("userId",  paramMap.get("userId"));
			dtl.put("pgmId",   paramMap.get("pgmId"));
			sd05Mapper.insertProjectDtl(dtl);	
			sd09Mapper.insertSitePrdt(dtl);		
		}
		
		cm08Svc.uploadFile("TB_SD05M01", paramMap.get("prjctCd"), mRequest);
		// 신규 등록 공지를 위한 게시판 삽입
		insertNoti(paramMap, mRequest);
		return result;
	}

	@Override
	public int updateProject(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		int result = sd05Mapper.updateProject(paramMap);
		cm08Svc.uploadFile("TB_SD05M01", paramMap.get("prjctCd"), mRequest);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String[] deleteFileArr = gson.fromJson(paramMap.get("deleteFileArr"), String[].class);
		List<String> deleteFileList = Arrays.asList(deleteFileArr);
		for(String fileKey : deleteFileList) {
			cm08Svc.deleteFile(fileKey);
		}
		Gson gsonDtl = new GsonBuilder().disableHtmlEscaping().create();
		Type dtlMap = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		List<Map<String, String>> dtlParam = gsonDtl.fromJson(paramMap.get("detailArr"), dtlMap);
//		sd05Mapper.deleteProjectDtl(paramMap);
		for(Map<String, String> dtl : dtlParam) {
			dtl.put("coCd",    paramMap.get("coCd"));
			dtl.put("prjctCd", paramMap.get("prjctCd"));
			dtl.put("userId",  paramMap.get("userId"));
			dtl.put("pgmId",   paramMap.get("pgmId"));
			dtl.put("outordLossRate",   paramMap.get("outordLossRate"));
			
			String prjctSeq = dtl.get("prjctSeq").toString();
			if("".equals(prjctSeq) || "0".equals(prjctSeq)) {
				
				sd05Mapper.insertProjectDtl(dtl);
				String siteCd = sd09Mapper.selectSiteCdFind(paramMap);
				dtl.put("siteCd",  siteCd);
				sd09Mapper.insertSitePrdt(dtl);
				
			}else {
				sd05Mapper.updatePrjctDtl(dtl);
				sd09Mapper.updateSitePrdt(dtl);
			}
		}
		
		// paramMap.put("siteNm",      paramMap.get("prjctNm")); // 프로젝트 수정시 현장명 같이 수정되는 부분 제거
		// paramMap.put("siteAddrZip", paramMap.get("prjctAddrZip"));
		// paramMap.put("siteAddr",    paramMap.get("prjctAddr"));
		// paramMap.put("siteAddrSub", paramMap.get("prjctAddrSub"));
		// paramMap.put("siteMngNm",   paramMap.get("prjctMngNm"));
		paramMap.remove("telNo");
		sd09Mapper.updateSite(paramMap);
		
		return result;
	}
	
	@Override
	public int deleteProject(Map<String, String> param) {
		//sd05Mapper.deleteProjectDtl(param); 마스터테이블 사용여부만 n으로
		param.put("useYn", "N");
		sd09Mapper.updateSiteYn(param); 
		
		return sd05Mapper.deleteProject(param);
	}
	
	@Override
	public void deleteProjectDtl(List<Map<String, String>> paramList) {
		for(Map<String, String> param : paramList) {							
			sd05Mapper.deleteProjectDtl(param);
			sd09Mapper.deleteSitePrdt(param);
		}		
	}
	
	@Override
	public int selectConfirmCount(Map<String, String> paramMap) {
		return sd05Mapper.selectConfirmCount(paramMap);
	}
    
	@Override
	public List<Map<String, String>> selectPrdtDivCd(Map<String, String> param) {
		return sd05Mapper.selectPrdtDivCd(param);
	}
    
	@Override
	public List<Map<String, String>> prdtDivCombo(Map<String, String> param) {
		return sd05Mapper.prdtDivCombo(param);
	}
    
	@Override
	public List<Map<String, String>> prdtSpecCombo(Map<String, String> param) {
		return sd05Mapper.prdtSpecCombo(param);
	}
	
	public Map<String, String> insertNoti(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		paramMap.put("notiTitle", "[신규 프로젝트 등록 안내] " + paramMap.get("prjctNm"));
		String notiCnts = "등록일: "+ paramMap.get("creatDtDtl");;
		notiCnts += "<br>공사기간: "+ paramMap.get("strtDt") + " ~ " + paramMap.get("endDt");
		notiCnts += "<br>계약물량: "+ paramMap.get("totWt");
		notiCnts += "<br><br><a href=\'/static/html/user/sd/sd05/SD0501M01.html\' target='_blank'>프로젝트 관리 바로가기</a>";
		paramMap.put("notiCnts", notiCnts);
		paramMap.put("exprtnYn", "Y");
		paramMap.put("exprtnDt", DateUtil.dateToString(DateUtil.getNextDate(new Date()), "yyyyMMdd"));
		paramMap.put("popupYn", "Y");
		paramMap.put("useYn", "Y");
		cm09Svc.insertNoti(paramMap, mRequest);
		return paramMap;
	}

	@Override
	public  Map<String, String> selectMakerPchsClntCd(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return sd05Mapper.selectMakerPchsClntCd(paramMap);
	}
}