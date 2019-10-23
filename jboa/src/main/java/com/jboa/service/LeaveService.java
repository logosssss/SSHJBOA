package com.jboa.service;

import java.util.Date;

import com.jboa.entity.ClaimVoucher;
import com.jboa.entity.Employee;
import com.jboa.entity.Leave;
import com.jboa.util.PaginationSupport;

public interface LeaveService {
	/**
	 * 根据请假对象保存信息
	 * @param leave
	 */
	public void saveLeave(Leave leave);
	
	/**
	 * 根据条件查询请假单
	 * 
	 * @param employee
	 * @param leave
	 * @param startDate
	 * @param endDate
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport<Leave> getClaimVoucherPage(Employee employee,Leave leave,
			Date startDate,Date endDate,
			Integer pageNo,Integer pageSize);
	
	/**
	 * 根据id查看请假单信息
	 * @param id
	 * @return
	 */
	public Leave findLeaveByid(Long id);
	
	/**
	 * 根据请假单对象审核
	 * @param leave
	 */
	public void checkLeave(Leave leave);
}
