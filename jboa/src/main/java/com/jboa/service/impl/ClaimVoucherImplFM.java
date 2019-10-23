package com.jboa.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jboa.common.Constants;
import com.jboa.entity.Employee;
import com.jboa.service.ClaimVoucherService;

/**
 * 部门经理权限下对报销单的具体操作(继承了公有的Service类和报销单Service公有接口)
 * @author 86185
 *
 */
public class ClaimVoucherImplFM extends BaseClaimVoucherImpl implements ClaimVoucherService {

	/*@Override
	public PaginationSupport<ClaimVoucher> getClaimVoucherPage(Employee employee, String status, Date startDate,
			Date endDate, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	//部门经理报销单状态设置
	private static Map<String, String> statusMap;
	static {
		statusMap = new LinkedHashMap<>();
		statusMap.put(Constants.CLAIMVOUCHER_SUBMITTED, "已提交");
		statusMap.put(Constants.CLAIMVOUCHER_APPROVING, "待审批");
		statusMap.put(Constants.CLAIMVOUCHER_BACK, "已打回");
		statusMap.put(Constants.CLAIMVOUCHER_APPROVED, "已审批");
		statusMap.put(Constants.CLAIMVOUCHER_PAID, "已付款");
		statusMap.put(Constants.CLAIMVOUCHER_TERMINATED, "已终止");
	}
	
	//根据条件设置部门经理查看报销单信息的sql语句
	@Override
	protected void buildBaseSql(Employee employee, String status, Date startDate, Date endDate, StringBuffer sb,
			List<Object> object) {
		
		sb.append("select c from ClaimVoucher c,Employee e where c.creator.sn=e.sn and e.sysDepartment.id = ?");
		object.add(employee.getSysDepartment().getId());
		
		if (status != null && status.length()!=0) {
			sb.append(" and c.status = ?");
			object.add(status);
		}else{
			sb.append("and c.status >= ?");
			object.add(Constants.CLAIMVOUCHER_SUBMITTED); 
		}
		
		if (startDate != null) {
			sb.append(" and c.createTime >= ?");
			object.add(startDate);
		}
		if (endDate != null) {
			sb.append(" and c.createTime < ?");
			Calendar oneDayLater = Calendar.getInstance();
			oneDayLater.setTime(endDate);
			oneDayLater.add(Calendar.DAY_OF_MONTH, 1);
			object.add(oneDayLater.getTime());
		}
		
	}

	/**
	 * 部门经理需要关心的排序条件
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
