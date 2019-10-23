package com.jboa.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jboa.common.Constants;
import com.jboa.entity.Employee;
import com.jboa.entity.Leave;

/**
 * 普通员工权限下对请假单的具体操作(继承了公有的Service类和请假单Service公有接口)
 * @author 86185
 *
 */
public class LeavelServiceImplPM extends BaseLeavelServiceImpl {

	/**
	 * 部门经理权限下根据条件封装sql语句并入参查询
	 */
	@Override
	protected void buildBaseSql(Employee employee, Leave leave, Date startDate, Date endDate, StringBuffer sb,
			List<Object> object) {
		sb.append("select l from Leave l,Employee e where l.creator.sn = e.sn and  e.sysDepartment.id = ? ");
		object.add(employee.getSysDepartment().getId());

		if (leave.getStatus().length() != 0) {
			sb.append(" and l.status = ?");
			object.add(leave.getStatus());
		}
		if (startDate != null) {
			sb.append(" and l.createTime >= ?");
			object.add(startDate);
		}
		if (endDate != null) {
			sb.append(" and l.createTime < ?");
			Calendar oneDayLater = Calendar.getInstance();
			oneDayLater.setTime(endDate);
			oneDayLater.add(Calendar.DAY_OF_MONTH, 1);
			object.add(oneDayLater.getTime());
		}
	}

	/**
	 * 根据部门经理的需要封装sql的排序的语句
	 */
	@Override
	protected void addOrders(Leave leave, StringBuffer sb) {
		if (leave.getStatus().length() == 0) {
			sb.append(" order by l.status,l.createTime desc");
		} else {
			sb.append(" order by l.createTime desc");
		}
	}

	/**
	 * 部门经理的审核请假单方法
	 */
	@Override
	public void checkLeave(Leave leave) {
		super.checkLeave(leave);
		Leave toCheck = getLeaveDao().get(leave.getId());
		toCheck.setStatus(leave.getStatus());
		toCheck.setApproveOpinion(leave.getApproveOpinion());
		toCheck.setModifyTime(new Date());

		if (toCheck.getStatus().equals(Constants.LEAVESTATUS_APPROVED)) {
			toCheck.setNextDeal(null);
		} else {
			toCheck.setNextDeal(toCheck.getCreator());
		}
	}

}
