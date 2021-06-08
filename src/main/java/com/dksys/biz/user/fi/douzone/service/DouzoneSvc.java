package com.dksys.biz.user.fi.douzone.service;

import java.util.List;
import java.util.Map;

public interface DouzoneSvc {
	
	public int insertAutodocuSimple(Map<String, String> param);

	public int updateAutodocuSimple(Map<String, String> param);
	
	public int deleteAutodocuSimple(Map<String, String> param);	
}