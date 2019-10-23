package com.jboa.service;

import com.jboa.entity.Employee;

/**
 * 用户服务接口
 * @author 86185
 *
 */
public interface EmployeeService {
	/**
	 * 登录方法 根据入参的对象登录
	 * @param emp
	 * @return
	 */
	public Employee login(Employee emp);
	
	/**
	 * 根据入参的职工对象获取他的部门经理
	 * @param emp
	 * @return
	 */
	public Employee getManage(Employee emp);
}
