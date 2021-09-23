package com.dksys.biz.user.sd.sd04.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dksys.biz.admin.cm.cm08.service.CM08Svc;
import com.dksys.biz.user.sd.sd04.mapper.SD04Mapper;
import com.dksys.biz.user.sd.sd04.service.SD04Svc;
import com.dksys.biz.user.sm.sm01.mapper.SM01Mapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
@Transactional(rollbackFor = Exception.class)
public class SD04SvcImpl implements SD04Svc {
	
    @Autowired
    SD04Mapper sd04Mapper;
    
    @Autowired
    SM01Mapper sm01Mapper;
    
    @Autowired
    CM08Svc cm08Svc;
    
	@Override
	public int selectOrderCount(Map<String, String> paramMap) {
		return sd04Mapper.selectOrderCount(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectOrderList(Map<String, String> paramMap) {
		return sd04Mapper.selectOrderList(paramMap);
	}

	@Override
	public Map<String, Object> selectOrderInfo(Map<String, String> paramMap) {
		return sd04Mapper.selectOrderInfo(paramMap);
	}
	
	@Override
	public int selectFixedOrderCount(Map<String, String> paramMap) {
		return sd04Mapper.selectFixedOrderCount(paramMap);
	}

	@Override
	public void insertOrder(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		
		// 주문 insert
		sd04Mapper.insertOrder(paramMap);

		// 주문상세 insert
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for(Map<String, String> detailMap : detailList) {
			paramMap.put("prdtCd", detailMap.get("prdtCd"));
			Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
			if(stockInfo == null) {
				detailMap.put("pchsUpr", "0");
				detailMap.put("sellUpr", "0");
//				detailMap.put("stockUpr", "0");
			} else {
				detailMap.put("pchsUpr", stockInfo.get("pchsUpr"));
				detailMap.put("sellUpr", stockInfo.get("sellUpr"));
//				detailMap.put("stockUpr", stockInfo.get("stockUpr"));
			}
			detailMap.put("odrSeq", paramMap.get("odrSeq"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			sd04Mapper.insertOrderDetail(detailMap);
		}
		
		// 파일 업로드
		cm08Svc.uploadFile("TB_SD04M01", paramMap.get("odrSeq"), mRequest);
	}
	
	@Override
	public void updateOrder(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		Type stringList = new TypeToken<ArrayList<String>>() {}.getType();
		
		// 주문 update
		sd04Mapper.updateOrder(paramMap);
		
		// 주문상세 delete
		sd04Mapper.deleteOrderDetail(paramMap);
		// 주문상세 insert
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for(Map<String, String> detailMap : detailList) {
			paramMap.put("prdtCd", detailMap.get("prdtCd"));
			Map<String, String> stockInfo = sm01Mapper.selectStockInfo(paramMap);
			if(stockInfo == null) {
				detailMap.put("pchsUpr", "0");
				detailMap.put("sellUpr", "0");
				detailMap.put("stockUpr", "0");
			} else {
				detailMap.put("pchsUpr", stockInfo.get("pchsUpr"));
				detailMap.put("sellUpr", stockInfo.get("sellUpr"));
				detailMap.put("stockUpr", stockInfo.get("stockUpr"));
			}
			detailMap.put("odrSeq", paramMap.get("odrSeq"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			sd04Mapper.insertOrderDetail(detailMap);
		}
		
		// 파일 업로드
		cm08Svc.uploadFile("TB_SD04M01", paramMap.get("odrSeq"), mRequest);
		// 파일 삭제
		List<String> deleteFileList = gson.fromJson(paramMap.get("deleteFileArr"), stringList);
		for(String fileKey : deleteFileList) {
			cm08Svc.deleteFile(fileKey);
		}
	}
	
	@Override
	public void deleteOrder(Map<String, String> paramMap) {
		sd04Mapper.deleteOrder(paramMap);
		sd04Mapper.deleteOrderDetail(paramMap);
		// 첨부파일 삭제 보류!
	}

	@Override
	public void closeOrder(List<Map<String, String>> paramMapList) {
		for(Map<String, String> paramMap : paramMapList) {
			sd04Mapper.closeOrder(paramMap);
			sd04Mapper.closeOrderDetail(paramMap);
		}
	}

	@Override
	public String selectOdrSeq() {
		return sd04Mapper.selectOdrSeq();
	}
	
}