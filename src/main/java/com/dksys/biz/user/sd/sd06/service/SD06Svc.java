package com.dksys.biz.user.sd.sd06.service;

import java.util.List;
import java.util.Map;

public interface SD06Svc {

	
	public int selectUprCount(Map<String, String> param);
	
    public List<Map<String, String>> selectUprList(Map<String, String> param);

//	public int insertUpr(Map<String, String> param);

//	public int deleteUpr(Map<String, String> param);

//	public int updateUpr(Map<String, String> param);

	public Map<String, String> selectUprInfo(Map<String, String> param);

	public int selectOneMasterCount(Map<String, String>  param);
	
	public int selectDetail01Count(Map<String, String> param);
	
    public List<Map<String, String>> selectDetail01List(Map<String, String> param);
    
    public int selectDetail02Count(Map<String, String> param);
	
    public List<Map<String, String>> selectDetail02List(Map<String, String> param);
    
    public Map<String, String> seletOneMaster(Map<String, String> param);
    
    public Map<String, String> seletOneDetail01(Map<String, String> param);

    public Map<String, String> seletOneDetail02(Map<String, String> param);
    
    public int insertOneMaster(Map<String, String> param);
    
    public int insertOneDetail01(Map<String, String> param);
    
    public int insertOneDetail02(Map<String, String> param);

    public int updateOneDetail01(Map<String, String> param);

    public int updateOneDetail02(Map<String, String> param);
    
    // 거래처별 단가 -------------
    public int selectUprClntCount(Map<String, String> param);
	
    public List<Map<String, String>> selectUprClntList(Map<String, String> param);
    
	public int selectOneMasterClntCount(Map<String, String>  param);
    
    public Map<String, String> seletOneMasterClnt(Map<String, String> param);
    
    public int insertOneMasterClnt(Map<String, String> param);
    
    public int updateUseYnClnt(Map<String, String> param);
    
}