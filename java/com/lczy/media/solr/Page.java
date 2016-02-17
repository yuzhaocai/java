package com.lczy.media.solr;

import java.util.List;

public class Page<T> {
	
	private int total;
	private int pageSize;
	private int pageNum;
	private List<T> data;
	
	public Page() {}
	
	public Page(List<T> data, int total, int pageNum, int pageSize) {
		this.data = data;
		this.total = total;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
	
	public int getTotalPage() {
		int count = total / pageSize;
		if( total % pageSize > 0)
			count += 1;
		
		return count;
	}

	public boolean isNext() {
		return pageNum < getTotalPage();
	}

	public boolean isPrevious() {
		return total > 0 && pageNum > 1;
	}
	
}
