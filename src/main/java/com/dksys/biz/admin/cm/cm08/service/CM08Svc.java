package com.dksys.biz.admin.cm.cm08.service;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface CM08Svc {

	public int uploadFile(String fileTrgtKey, MultipartHttpServletRequest mRequest);

}