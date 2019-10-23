package com.jboa.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jboa.common.Constants;
import com.jboa.entity.Employee;
import com.jboa.entity.Leave;
import com.jboa.service.LeaveService;

/**
 * 请假单Action(继承BaseAction)
 * @author 86185
 *
 */
public class LeaveAction extends BaseAction<Leave> {
	// private LeaveService leaveService;
	//根据不同员工职务获取不同的service
	private Map<String, LeaveService> leaveServiceMap;
	//开始时间
	private Date startDate;
	//结束时间
	private Date endDate;
	//请假单的审核状态
	private static Map<String, String> statusMap;
	//请假单的审核状态 的初始化
	static {
		statusMap = new HashMap<>();
		statusMap.put(Constants.LEAVESTATUS_APPROVING, "待审批");
		statusMap.put(Constants.LEAVESTATUS_APPROVED, "已审批");
		statusMap.put(Constants.LEAVESTATUS_BACK, "已打回");
	}
	//请假单的类别
	private static Map<String, String> leaveTypeMap;
	//请假单的类别的初始化
	static {
		leaveTypeMap = new HashMap<String, String>();
		leaveTypeMap.put(Constants.LEAVE_SICK, Constants.LEAVE_SICK);
		leaveTypeMap.put(Constants.LEAVE_ANNUAL, Constants.LEAVE_ANNUAL);
		leaveTypeMap.put(Constants.LEAVE_CASUAL, Constants.LEAVE_CASUAL);
		leaveTypeMap.put(Constants.LEAVE_MARRIAGE, Constants.LEAVE_MARRIAGE);
		leaveTypeMap.put(Constants.LEAVE_MATERNITY, Constants.LEAVE_MATERNITY);
	}
	//请假单
	private Leave leave;
	public void setLeaveServiceMap(Map<String, LeaveService> leaveServiceMap) {
		this.leaveServiceMap = leaveServiceMap;
	}
	public Map<String, String> getLeaveTypeMap() {
		return leaveTypeMap;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Map<String, String> getStatusMap() {
		return statusMap;
	}
	public void setStatusMap(Map<String, String> statusMap) {
		LeaveAction.statusMap = statusMap;
	}
	public Leave getLeave() {
		return leave;
	}
	public void setLeave(Leave leave) {
		this.leave = leave;
	}
	public void setLeaveTypeMap(Map<String, String> leaveTypeMap) {
		LeaveAction.leaveTypeMap = leaveTypeMap;
	}
	
	
	/**
	 * 根据不同的职务获取不同的service的方法
	 * @return
	 */
	public LeaveService getLeaveService() {
		return leaveServiceMap.get(this.getPositionForShort());
	}
	
	/**
	 * 跳转到编辑请假单页面请求处理方法
	 * @return
	 */
	public String toEdit() {
		return "edit";
	}
	
	/**
	 * 保存请假单请求的方法
	 * @return
	 */
	public String saveLeave() {
		leave.setCreator(this.getEmployee());

		leave.setNextDeal((Employee) this.getSession().get(Constants.AUTH_EMPLOYEE_MANAGER));
		getLeaveService().saveLeave(leave);
		return "redirectList";
	}

	/**
	 * 查看请假单请求处理方法
	 * @return
	 */
	public String searchLeave() {
		switch (this.getEmployee().getSysPosition().getNameCn()) {
		case Constants.POSITION_STAFF:
			this.stuffStatusInit();
			break;
		case Constants.POSITION_FM:
			this.fmStuffStatusInit();
			break;
		}

		pageSupport = getLeaveService().getClaimVoucherPage(this.getEmployee(), leave, startDate, endDate, pageNo,
				pageSize);
		return "list";
	}

	/**
	 * 设置普通员工需要查看的请假单状态
	 */
	protected void stuffStatusInit() {
		//默认为全部
	}

	/**
	 * 设置部门经理需要查看的请假单状态
	 */
	protected void fmStuffStatusInit() {
		//默认为“待审核”
		if (leave == null) {
			leave = new Leave();
		}
		if (leave.getStatus() == null) {
			leave.setStatus(Constants.LEAVESTATUS_APPROVING);
		}
	}
	
	/**
	 * 查看某个请假单信息请求的处理方法
	 * @return
	 */
	public String getLeaveById(){
		leave = this.getLeaveService().findLeaveByid(leave.getId());
		return "view";
	}
	
	/**
	 * 跳转到审核页面的处理方法
	 * @return
	 */
	public String toCheck(){
		leave = this.getLeaveService().findLeaveByid(leave.getId());
		return "check";
	}
	
	/**
	 * 审核请假单请求的处理方法
	 * @return
	 */
	public String checkLeave(){
		getLeaveService().checkLeave(leave);
		return "redirectList";
	}

}
