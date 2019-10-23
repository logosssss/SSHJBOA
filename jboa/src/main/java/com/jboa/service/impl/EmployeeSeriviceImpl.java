package com.jboa.service.impl;

import com.jboa.dao.EmployeeDao;
import com.jboa.entity.Employee;
import com.jboa.exception.JboaException;
import com.jboa.service.EmployeeService;

/**
 * 职工服务接口实现类
 * @author 86185
 *
 */
public class EmployeeSeriviceImpl implements EmployeeService{
	
	private EmployeeDao employeeDao;
	public EmployeeDao getEmployeeDao() {
		return employeeDao;
	}
	public void setEmployeeDao(EmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}


	@Override
	public Employee login(Employee emp) {
		Employee employee = employeeDao.findEmloyeeBySN(emp.getSn());
		if (employee!=null&&employee.getPassword().equals(emp.getPassword())) {
			return employee;
		}else{
			throw new JboaException("账号或密码异常!");
		}
	}
	
	@Override
	public Employee getManage(Employee emp) {
		return employeeDao.getManage(emp);
	}

}
