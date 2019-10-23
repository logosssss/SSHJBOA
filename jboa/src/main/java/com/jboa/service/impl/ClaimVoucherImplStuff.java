package com.jboa.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jboa.common.Constants;
import com.jboa.entity.ClaimVoucher;
import com.jboa.entity.Employee;
import com.jboa.service.ClaimVoucherService;
import com.jboa.util.PaginationSupport;

/**
 * 普通员工权限下对报销单的具体操作(继承了公有的Service类和报销单Service公有接口)
 * @author 86185
 *
 */
public class ClaimVoucherImplStuff extends BaseClaimVoucherImpl implements ClaimVoucherService {

	/*
	 * @Override public PaginationSupport<ClaimVoucher>
	 * getClaimVoucherPage(Employee employee, String status, Date startDate,
	 * Date endDate, Integer pageNo, Integer pageSize) { // TODO Auto-generated
	 * method stub return null; }
	 */
	//普通职工报销单状态设置
	private static Map<String, String> statusMap;
	//根据状态id初始化为中文描述
	static {
		statusMap = new LinkedHashMap<>();
		statusMap.put(Constants.CLAIMVOUCHER_CREATED, "新创建");
		statusMap.put(Constants.CLAIMVOUCHER_SUBMITTED, "已提交");
		statusMap.put(Constants.CLAIMVOUCHER_APPROVING, "待审批");
		statusMap.put(Constants.CLAIMVOUCHER_BACK, "已打回");
		statusMap.put(Constants.CLAIMVOUCHER_APPROVED, "已审批");
		statusMap.put(Constants.CLAIMVOUCHER_PAID, "已付款");
		statusMap.put(Constants.CLAIMVOUCHER_TERMINATED, "已终止");
	}
	
	//根据条件设置普通员工查看报销单信息的sql语句
	@Override
	protected void buildBaseSql(Employee employee, String status, Date startDate, Date endDate, StringBuffer sb,
			List<Object> object) {

		sb.append("from ClaimVoucher c where c.creator.sn=?");
		object.add(employee.getSn());
		//判断用户是否选择了根据状态查看 并入参
		if (status != null && status.length() != 0) {
			sb.append(" and c.status = ?");
			object.add(status);
		}
		//判断是否选择了开始时间 并入参
		if (startDate != null) {
			sb.append(" and c.createTime >= ?");
			object.add(startDate);
		}
		//判断是否选择了结束时间并入参(实际查询的结束时间要加一天)
		if (endDate != null) {
			sb.append(" and c.createTime < ?");
			Calendar oneDayLater = Calendar.getInstance();
			oneDayLater.setTime(endDate);
			oneDayLater.add(Calendar.DAY_OF_MONTH, 1);
			object.add(oneDayLater.getTime());
		}

	}

	/**
	 * 普通员工需要关心的排序条件
	 */
	@Override
	protected void addOrders(String status, StringBuffer sb) {
		if (status == null || status.length() == 0) {
			sb.append(" order by c.status,c.createTime desc");
		} else {
			sb.append(" order by c.createTime desc");

		}
	}

	@Override
	public Map<String, String> getStatusMap() {
		return statusMap;
	}

}
