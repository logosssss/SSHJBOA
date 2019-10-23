package com.jboa.dao.impl;

import java.util.List;

import com.jboa.common.Constants;
import com.jboa.dao.EmployeeDao;
import com.jboa.entity.Employee;

public class EmployeeDaoImpl extends BaseDaoImpl<Employee> implements EmployeeDao {
	private static Employee employeeGM = null;

	public List<Employee> findsomething() {
		return null;
	}

	@Override
	public Employee findEmloyeeBySN(String sn) {
		String hql = "from Employee e where e.sn = ?";
		List<Employee> empList = this.getHibernateTemplate().find(hql, sn);
		if (empList == null || empList.size() == 0) {
			return null;
		}
		return empList.get(0);
	}

	@Override
	public Employee getManage(Employee emp) {
		String hql = "from Employee e where e.status = ? and e.sysDepartment.id = ? and e.sysPosition.nameCn = ?";
		List<Employee> employeeList = this.find(hql, Constants.EMPLOYEE_STAY, emp.getSysDepartment().getId(),
				Constants.POSITION_FM);
		if (employeeList == null || employeeList.size() == 0) {
			return null;
		}
		return employeeList.get(0);
	}

	@Override
	public Employee getCashier() {
		String hql = "from Employee e where e.status = ? and e.sysPosition.nameCn = ?";
		return (Employee) this.find(hql, Constants.EMPLOYEE_STAY, Constants.POSITION_CASHIER).get(0);
	}

	@Override
	public Employee getGeneralManager() {
		if (employeeGM == null) {
			String hql = "from Employee e where e.status = ? and e.sysPosition.nameCn = ?";
			employeeGM = (Employee) this.find(hql, Constants.EMPLOYEE_STAY, Constants.POSITION_GM).get(0);
		}
		return employeeGM;

	}
}
