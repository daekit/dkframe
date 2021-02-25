package com.dksys.biz.user.sd.sd04.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
@Transactional("erpTransactionManager")
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
	public List<Map<String, String>> selectOrderList(Map<String, String> paramMap) {
		return sd04Mapper.selectOrderList(paramMap);
	}

	@Override
	public Map<String, Object> selectOrderInfo(Map<String, String> paramMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("fileList", cm08Svc.selectFileList(paramMap.get("reqDt")+paramMap.get("ordrgSeq")));
		returnMap.put("orderInfo", sd04Mapper.selectOrderInfo(paramMap));
		returnMap.put("orderDetail", sd04Mapper.selectOrderDetail(paramMap));
		return returnMap;
	}
	
	@Override
	public void insertOrder(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		
		// 주문 insert
		sd04Mapper.insertOrder(paramMap);
		// 파일 업로드
		cm08Svc.uploadFile("TB_SD04M01", paramMap.get("odrSeq"), mRequest);
		
		// 재고마스터에서 긁어다가 넣는 부분 필요!!!!! (xml주석 참고)
		
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
	}
	
	@Override
	public int updateOrder(Map<String, String> paramMap, MultipartHttpServletRequest mRequest) {
		int result = sd04Mapper.updateOrder(paramMap);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type mapList = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
		// 발주상세 delete
		sd04Mapper.deleteOrderDetail(paramMap);
		// 발주상세 insert
		List<Map<String, String>> detailList = gson.fromJson(paramMap.get("detailArr"), mapList);
		for(Map<String, String> detailMap : detailList) {
			detailMap.put("ordrgSeq", paramMap.get("ordrgSeq"));
			detailMap.put("userId", paramMap.get("userId"));
			detailMap.put("pgmId", paramMap.get("pgmId"));
			sd04Mapper.insertOrderDetail(detailMap);
		}
		cm08Svc.uploadFile("TB_OD01M01", paramMap.get("reqDt")+paramMap.get("ordrgSeq"), mRequest);
		String[] deleteFileArr = gson.fromJson(paramMap.get("deleteFileArr"), String[].class);
		List<String> deleteFileList = Arrays.asList(deleteFileArr);
		for(String fileKey : deleteFileList) {
			cm08Svc.deleteFile(fileKey);
		}
		return result;
	}
	
	@Override
	public int deleteOrder(Map<String, String> paramMap) {
		return sd04Mapper.deleteOrder(paramMap);
	}
}