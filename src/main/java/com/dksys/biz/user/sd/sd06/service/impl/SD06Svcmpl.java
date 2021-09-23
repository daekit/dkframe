package com.dksys.biz.user.sd.sd06.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dksys.biz.user.sd.sd06.mapper.SD06Mapper;
import com.dksys.biz.user.sd.sd06.service.SD06Svc;

@Service
@Transactional(rollbackFor = Exception.class)
public class SD06Svcmpl implements SD06Svc {
	
    @Autowired
    SD06Mapper sd06Mapper;

    @Override
	public int selectUprCount(Map<String, String> param) {
		return sd06Mapper.selectUprCount(param);
	}

	@Override
	public List<Map<String, String>> selectUprList(Map<String, String> param) {
		return sd06Mapper.selectUprList(param);
	}

//	@Override
//	public int insertUpr(Map<String, String> param) {
//		return sd06Mapper.insertUpr(param);
//	}

//	@Override
//	public int deleteUpr(Map<String, String> param) {
//		return sd06Mapper.deleteUpr(param);
//	}
//
//	@Override
//	public int updateUpr(Map<String, String> param) {
//		return sd06Mapper.updateUpr(param);
//	}

	@Override
	public Map<String, String> selectUprInfo(Map<String, String> param) {
	
		return sd06Mapper.selectUprInfo(param);
	}


	@Override
	public int selectDetail01Count(Map<String, String> param) {
		return sd06Mapper.selectDetail01Count(param);
	}

	@Override
	public List<Map<String, String>> selectDetail01List(Map<String, String> param) {
		return sd06Mapper.selectDetail01List(param);
	}

	@Override
	public int selectDetail02Count(Map<String, String> param) {
		return sd06Mapper.selectDetail02Count(param);
	}

	@Override
	public List<Map<String, String>> selectDetail02List(Map<String, String> param) {
		return sd06Mapper.selectDetail02List(param);
	}

	@Override
	public Map<String, String> seletOneMaster(Map<String, String> param) {
		return sd06Mapper.seletOneMaster(param);
	}

	@Override
	public Map<String, String> seletOneDetail01(Map<String, String> param) {
		return sd06Mapper.seletOneDetail01(param);
	}

	@Override
	public Map<String, String> seletOneDetail02(Map<String, String> param) {
		return sd06Mapper.seletOneDetail02(param);
	}

	@Override
	public int insertOneMaster(Map<String, String> param) {
		return sd06Mapper.insertOneMaster(param);
	}

	@Override
	public int insertOneDetail01(Map<String, String> param) {
		return sd06Mapper.insertOneDetail01(param);
	}

	@Override
	public int insertOneDetail02(Map<String, String> param) {
		return sd06Mapper.insertOneDetail02(param);
	}

	@Override
	public int selectOneMasterCount(Map<String, String> param) {
		return sd06Mapper.selectOneMasterCount(param);
	}
	
	@Override
	public int updateOneDetail01(Map<String, String> param) {
		return sd06Mapper.updateOneDetail01(param);
	}

	@Override
	public int updateOneDetail02(Map<String, String> param) {
		return sd06Mapper.updateOneDetail02(param);
	}

	// 거래처,턴키별 단가
	
	@Override
	public int selectUprClntCount(Map<String, String> param) {
		// TODO Auto-generated method stub
		return sd06Mapper.selectUprClntCount(param);
	}

	@Override
	public List<Map<String, String>> selectUprClntList(Map<String, String> param) {
		// TODO Auto-generated method stub
		return sd06Mapper.selectUprClntList(param);
	}

	@Override
	public int selectOneMasterClntCount(Map<String, String> param) {
		// TODO Auto-generated method stub
		return sd06Mapper.selectOneMasterClntCount(param);
	}

	@Override
	public Map<String, String> seletOneMasterClnt(Map<String, String> param) {
		// TODO Auto-generated method stub
		return  sd06Mapper.seletOneMasterClnt(param);
	}

	@Override
	public int insertOneMasterClnt(Map<String, String> param) {
		// TODO Auto-generated method stub
		return sd06Mapper.insertOneMasterClnt(param);
	}

	@Override
	public int updateUseYnClnt(Map<String, String> param) {
		// TODO Auto-generated method stub
		return sd06Mapper.updateUseYnClnt(param);
	}
}