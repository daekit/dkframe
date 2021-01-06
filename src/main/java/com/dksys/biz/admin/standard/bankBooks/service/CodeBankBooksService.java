package com.dksys.biz.admin.standard.bankBooks.service;

import java.util.List;
import java.util.Map;

public interface CodeBankBooksService {

    public List<Map<String, String>> selectCodeList();

	public int insertCode(Map<String, String> param);

	public int deleteCode(Map<String, String> param);

	public int updateCode(Map<String, String> param);


}