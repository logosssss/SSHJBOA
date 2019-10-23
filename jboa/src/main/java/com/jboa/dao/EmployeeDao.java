package com.jboa.dao;

import java.util.List;

import com.jboa.entity.Employee;

/**
 * 职工Dao层接口继承BaseDao接口(拥有BaseDao的方法)
 */
public interface EmployeeDao extends BaseDao<Employee>{
	public List<Employee> findsomething();
	
	public Employee findEmloyeeBySN(String sn);
	
	public Employee getManage(Employee emp);
	
	public Employee getCashier();
	
	public Employee getGeneralManager();
}
