package com.jboa.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jboa.dao.LeaveDao;
import com.jboa.entity.ClaimVoucher;
import com.jboa.entity.Employee;
import com.jboa.entity.Leave;
import com.jboa.service.LeaveService;
import com.jboa.util.PaginationSupport;

/**
 * 请假单Serivce实现类
 * @author 86185
 *
 */
public abstract class BaseLeavelServiceImpl implements LeaveService {

	private LeaveDao leaveDao;

	public LeaveDao getLeaveDao() {
		return leaveDao;
	}

	public void setLeaveDao(LeaveDao leaveDao) {
		this.leaveDao = leaveDao;
	}

	@Override
	public void saveLeave(Leave leave) {
		// TODO Auto-generated method stub

	}
	
	public Leave findLeaveByid(Long id){
		return leaveDao.get(id);
	}
	
	@Override
	public void checkLeave(Leave leave) {
		
	}

	/**
	 * 翻页操作具体方法
	 * 入参：登录的职工、请假单、开始时间、结束时间、页码、大小
	 */
	@Override
	public PaginationSupport<Leave> getClaimVoucherPage(Employee employee, Leave leave, Date startDate, Date endDate,
			Integer pageNo, Integer pageSize) {
		PaginationSupport<Leave> result = new PaginationSupport<Leave>();
		if (pageNo > 0) {
			result.setCurrPageNo(pageNo);
		}
		if (pageSize > 0) {
			result.setPageSize(pageSize);
		}

		StringBuffer sb = new StringBuffer("");
		List<Object> values = new ArrayList<Object>();
		this.buildBaseSql(employee, leave, startDate, endDate, sb, values);

		Integer count = leaveDao.getTotalCount(sb.toString(), values.toArray()).intValue();
		result.setTotalCount(count);
		if (count > 0) {
			int pageCount = (count % result.getPageSize() == 0 ? count / result.getPageSize()
					: count / result.getPageSize() + 1);
			result.setTotalPageCount(pageCount);
			if (result.getCurrPageNo() > pageCount) {
				result.setCurrPageNo(pageCount);
			}
			this.addOrders(leave, sb);
			List<Leave> leaveList = leaveDao.findForPage(sb.toString(), result.getCurrPageNo(), result.getPageSize(),
					values.toArray());
			result.setItems(leaveList);
		}
		return result;
	}

	/**
	 * 根据入参sql 语句封装查询语句
	 * 
	 * @param employee
	 * @param leave
	 * @param startDate
	 * @param endDate
	 * @param sb
	 * @param object
	 */
	protected abstract void buildBaseSql(Employee employee, Leave leave, Date startDate, Date endDate, StringBuffer sb,
			List<Object> object);

	/**
	 * 封装排序语句
	 * 
	 * @param leave
	 * @param sb
	 */
	protected abstract void addOrders(Leave leave, StringBuffer sb);
	
	
}
