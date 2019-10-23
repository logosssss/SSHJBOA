package com.jboa.action;

import com.jboa.common.Constants;
import com.jboa.entity.Employee;
import com.jboa.exception.JboaException;
import com.jboa.service.EmployeeService;
import com.jboa.util.MD5;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 用户请求控制器 继承了BaseAction
 * @author 86185
 *
 */
public class EmployeeAction extends BaseAction{
	//当前登录的职工
	private Employee employee;
	private EmployeeService employeeService;
	//登录错误信息
	private String msg;
	//当前登录职工的部门经理
	private Employee manage;
	//随机数(验证码)
	private String random;
	
	public void setRandom(String random) {
		this.random = random;
	}
	public String getRandom() {
		return random;
	} 
	public Employee getManage() {
		return manage;
	}
	public void setManage(Employee manage) {
		this.manage = manage;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMsg() {
		return msg;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	/**
	 * 登录请求
	 * @return
	 */
	public String login(){
		Object sessionRandom = this.getSession().get("random");
		
		//如果验证码不正确或为空就抛出自定义的错误信息
		if (random == null || !random.equals(sessionRandom)) {
			this.addActionError(this.getText("errors.codeerror"));
			msg = this.getText("errors.codeerror");
			return INPUT;
		}
		employee.setPassword(employee.getPassword());
		try {
			//如果登录成功
			employee = employeeService.login(employee);
			manage = employeeService.getManage(employee);
			//用户信息加入session
			this.getSession().put(Constants.AUTH_EMPLOYEE, employee);
			//用户部门经理入参session
			this.getSession().put(Constants.AUTH_EMPLOYEE_MANAGER, manage);
			//用户英文职务入参session
			this.getSession().put(Constants.EMPLOYEE_POSITION, employee.getSysPosition().getNameCn());
		} catch (JboaException e) {
			//登录失败抛出自定义报错信息
			this.addActionError(this.getText("errors.invalid.usernameorpassword"));
			msg = this.getText("errors.invalid.usernameorpassword");
		}
		if (this.hasActionErrors()) {
			return INPUT;
		}
		return SUCCESS;
	}
	
	
}
