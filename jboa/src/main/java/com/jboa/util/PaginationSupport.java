package com.jboa.util;

import java.util.LinkedList;
import java.util.List;

/**
 * 翻页工具类
 * @author 86185
 *
 * @param <T>
 */
public class PaginationSupport<T> {
	
	//每页大小
	private int pageSize = 5;
	//当前页码
	private int currPageNo = 1;
	//总页数
	private int totalPageCount;
	//总数量
	private int totalCount;
	//每页的对象集合
	private List<T> items = new LinkedList<T>();
	
	public PaginationSupport(){
		
	}
	
	public PaginationSupport(int currPageNo, int pageSize){
    	setPageSize(pageSize);
    	setCurrPageNo(currPageNo);
    }
//	public PaginationSupport(List<T> items ,int currPageNo, int totalCount){
//		setTotalCount(totalCount);
//		refreshPageNo(currPageNo);
//		setItems(items);
//		
//	}
//	
//	private int refreshPageNo(int currPageNo){
//    	int tempCurrPage = currPageNo;
//    	if(currPageNo < 1){
//    		tempCurrPage = 1;
//    	}
//    	if(currPageNo > this.totalPageCount){
//    		tempCurrPage = this.totalPageCount;
//    	}
//    	return tempCurrPage;
//    }
	/**
	 * 根据页码和大小返回实际开始查询的行码
	 * 
	 * @return
	 */
	public int getStartRow() {
		return (currPageNo - 1) * pageSize;
	}

	/**
	 * 寰楀埌缁撴潫璁板綍鏁�
	 * 
	 * @return
	 */
//	public int getEndRow() {
//		return currPageNo * pageSize;
//	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrPageNo() {
		return currPageNo;
	}

	public void setCurrPageNo(int currPageNo) {
		this.currPageNo = currPageNo;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 根据总记录数设置总页数
	 * @param totalCount
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		if (totalCount >= 0) {
			int count = totalCount / pageSize;
			if (totalCount % pageSize > 0) {
				count++;
			}
			this.totalPageCount = count;
		}
	}

	public List<T> getItems() {
		return items;
	}
	
	public void setItems(List<T> items) {
		this.items = items;
	}
	
}
