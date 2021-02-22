package com.dksys.biz.cmn.vo;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;


public class PaginationInfo
{
    private int currentPageNo;
    private int recordCountPerPage;
    private int pageSize;
    private int totalRecordCount;
    private int totalPageCount;
    private int firstPageNoOnPageList;
    private int lastPageNoOnPageList;
    private int firstRecordIndex;
    private int lastRecordIndex;
    
    public PaginationInfo(Map<String, String> param, int totalCnt) {
    	
    	// 현재 페이지 번호
    	String pageNo = param.get("pageNo");
    	if(StringUtils.isEmpty(pageNo)) {
    		this.currentPageNo = 1;
    	}else {
    		this.currentPageNo = Integer.parseInt(pageNo);
    	}
    	
    	// 페이지당 로우 개수
    	String recordCnt = param.get("recordCnt");
    	if(StringUtils.isEmpty(recordCnt)) {
    		this.recordCountPerPage = 20;
    	}else {
    		this.recordCountPerPage = Integer.parseInt(recordCnt);
    	}
    	
    	// 하단 페이지 개수
    	this.pageSize = 10;
    	
    	// 총 로우 개수
    	this.totalRecordCount = totalCnt;
    	
    	// 해당 페이지 처음 인덱스, 마지막 인덱스 set
    	param.put("firstIndex", String.valueOf(this.getFirstRecordIndex()+1));
    	param.put("lastIndex", String.valueOf(this.getLastRecordIndex()));
    }
    
    public int getCurrentPageNo() {
        return this.currentPageNo;
    }
    
    public void setCurrentPageNo(final int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }
    
    public int getRecordCountPerPage() {
        return this.recordCountPerPage;
    }
    
    public void setRecordCountPerPage(final int recordCountPerPage) {
        this.recordCountPerPage = recordCountPerPage;
    }
    
    public int getPageSize() {
        return this.pageSize;
    }
    
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }
    
    public void setTotalRecordCount(final int totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }
    
    public int getTotalRecordCount() {
        return this.totalRecordCount;
    }
    
    public int getTotalPageCount() {
        return this.totalPageCount = (this.getTotalRecordCount() - 1) / this.getRecordCountPerPage() + 1;
    }
    
    public int getFirstPageNo() {
        return 1;
    }
    
    public int getLastPageNo() {
        return this.getTotalPageCount();
    }
    
    public int getFirstPageNoOnPageList() {
        return this.firstPageNoOnPageList = (this.getCurrentPageNo() - 1) / this.getPageSize() * this.getPageSize() + 1;
    }
    
    public int getLastPageNoOnPageList() {
        this.lastPageNoOnPageList = this.getFirstPageNoOnPageList() + this.getPageSize() - 1;
        if (this.lastPageNoOnPageList > this.getTotalPageCount()) {
            this.lastPageNoOnPageList = this.getTotalPageCount();
        }
        return this.lastPageNoOnPageList;
    }
    
    public int getFirstRecordIndex() {
        return this.firstRecordIndex = (this.getCurrentPageNo() - 1) * this.getRecordCountPerPage();
    }
    
    public int getLastRecordIndex() {
        return this.lastRecordIndex = this.getCurrentPageNo() * this.getRecordCountPerPage();
    }
}
