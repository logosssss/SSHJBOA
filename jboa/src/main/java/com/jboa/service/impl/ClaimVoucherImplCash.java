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
 * 财务Service
 * @author 86185
 *
 */
public class ClaimVoucherImplCash extends BaseClaimVoucherImpl implements ClaimVoucherService {

	/*@Override
	public PaginationSupport<ClaimVoucher> getClaimVoucherPage(Employee employee, String status, Date startDate,
			Date endDate, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	//财务需要的订单状态设置
	private static Map<String, String> statusMap;
	static {
		statusMap = new LinkedHashMap<>();
		statusMap.put(Constants.CLAIMVOUCHER_APPROVED, "已审批");
		statusMap.put(Constants.CLAIMVOUCHER_PAID, "已付款");
	}

	//根据条件设置财务查看报销单信息的sql语句
	@Override
	protected void buildBaseSql(Employee employee, String status, Date startDate, Date endDate, StringBuffer sb,
			List<Object> object) {
		
		
		if (status.length()==0) {
			sb.append("from ClaimVoucher c where c.status >= ? and c.status <= ?");
			object.add(Constants.CLAIMVOUCHER_APPROVED);
			object.add(Constants.CLAIMVOUCHER_PAID);
		}else{
			sb.append("from ClaimVoucher c where c.status = ?");
			object.add(status);
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
	 * 财务需要关心的排序条件
	 */
	@Override
	protected void addOrders(String status, StringBuffer sb) {
		if (status.length() == 0) {
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
