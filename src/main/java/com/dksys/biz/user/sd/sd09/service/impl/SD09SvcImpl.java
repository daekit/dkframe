package com.dksys.biz.user.sd.sd09.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.sd.sd09.mapper.SD09Mapper;
import com.dksys.biz.user.sd.sd09.service.SD09Svc;

@Service
@Transactional("erpTransactionManager")
public class SD09SvcImpl implements SD09Svc {
	
    @Autowired
    SD09Mapper sd09Mapper;
    
	@Override
	public int selectSiteCount(Map<String, String> paramMap) {
		return sd09Mapper.selectSiteCount(paramMap);
	}

	@Override
	public List<Map<String, String>> selectSiteList(Map<String, String> paramMap) {
		return sd09Mapper.selectSiteList(paramMap);
	}

	@Override
	public Map<String, String> selectSiteDetail(Map<String, String> paramMap) {
		return sd09Mapper.selectSiteDetail(paramMap);
	}

	@Override
	public String insertSite(Map<String, String> paramMap) {
		String siteCd = sd09Mapper.selectSiteCd(paramMap); 
		paramMap.put("siteCd", siteCd);
		sd09Mapper.insertSite(paramMap);
		return siteCd;
	}

	@Override
	public int updateSite(Map<String, String> paramMap) {
		return sd09Mapper.updateSite(paramMap);
	}

	@Override
	public int deleteSite(Map<String, String> paramMap) {
		return sd09Mapper.deleteSite(paramMap);
	}

	

	@Override
	@SuppressWarnings("all")
	public int updateSiteYn(Map<String, Object> paramMap) {		
		int result = 0;
		List<Map<String, String>> detailList = (List<Map<String, String>>) paramMap.get("detailArr");
		for(Map<String, String> detailMap : detailList) {
			result += sd09Mapper.updateSiteYn(detailMap);
		}
		return result;
	}
}