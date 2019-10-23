package com.jboa.action;

import java.util.Map;

import com.jboa.common.Constants;
import com.jboa.entity.Employee;
import com.jboa.util.PaginationSupport;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * baseAction 基类
 * @author 86185
 *
 * @param <T>
 */
//使用了泛型类的方式
public class BaseAction<T> extends ActionSupport {
	//默认的页码 1
	protected Integer pageNo = 1;
	//默认的每页大小 5
	protected Integer pageSize = 5;
	//翻页的工具类 入参一个Dao操作对象
	protected PaginationSupport<T> pageSupport;
	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public PaginationSupport<T> getPageSupport() {
		return pageSupport;
	}
	
	public void setPageSupport(PaginationSupport<T> pageSupport) {
		this.pageSupport = pageSupport;
	}
	
	//封装一个根据key查询Session对象的通用方法
	protected Map<String, Object> getSession() {
		ActionContext actionContext = ActionContext.getContext();
		return actionContext.getSession();
	}

	//封装一个获取当前登录用户对象的方法(通过Session获取)
	protected Employee getEmployee() {
		return (Employee) this.getSession().get(Constants.AUTH_EMPLOYEE);
	}
	
	//根据员工的中文职务获取英文职务(用于判断权限)
	public String getPositionForShort(){
		switch (this.getEmployee().getSysPosition().getNameCn()) {
		case Constants.POSITION_CASHIER:
			return Constants.POSITION_CASHIER_EN;
		case Constants.POSITION_FM:
			return Constants.POSITION_FM_EN;
		case Constants.POSITION_GM:
			return Constants.POSITION_GM_EN;
		default:
			return Constants.POSITION_STAFF_EN;
		}
	}

}
