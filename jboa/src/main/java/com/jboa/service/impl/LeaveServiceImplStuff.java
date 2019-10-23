package com.jboa.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jboa.common.Constants;
import com.jboa.dao.LeaveDao;
import com.jboa.entity.Employee;
import com.jboa.entity.Leave;
import com.jboa.service.LeaveService;
import com.jboa.util.PaginationSupport;

/**
 * 部门经理权限下对请假单的具体操作(继承了公有的Service类和请假单Service公有接口)
 * @author 86185
 *
 */
public class LeaveServiceImplStuff extends BaseLeavelServiceImpl implements LeaveService{
	 
	/**
	 * 普通员工的提交请假单方法
	 */
	@Override
	public void saveLeave(Leave leave) {
		leave.setStatus(Constants.LEAVESTATUS_APPROVING);
		leave.setCreateTime(new Date());
		getLeaveDao().save(leave);
	}

	/**
	 * 普通员工权限下根据条件封装sql语句并入参查询
	 */
	@Override
	protected void buildBaseSql(Employee employee, Leave leave, Date startDate, Date endDate, StringBuffer sb,
			List<Object> object) {
		sb.append("from Leave where creator.sn = ?");
		object.add(employee.getSn());
		if (!(leave==null || leave.getLeaveType()==null||leave.getLeaveType().length()==0)){
			sb.append(" and leaveType = ?");
			object.add(leave.getLeaveType());
		}
		if (startDate != null) {
			sb.append(" and createTime >= ?");
			object.add(startDate);
		}
		if (endDate != null) {
			sb.append(" and createTime < ?");
			Calendar oneDayLater = Calendar.getInstance();
			oneDayLater.setTime(endDate);
			oneDayLater.add(Calendar.DAY_OF_MONTH, 1);
			object.add(oneDayLater.getTime());
		}
	}

	/**
	 * 根据员工的需要封装sql的排序语句
	 */
	@Override
	protected void addOrders(Leave leave, StringBuffer sb) {
		sb.append(" order by createTime desc");
	}
	
}
