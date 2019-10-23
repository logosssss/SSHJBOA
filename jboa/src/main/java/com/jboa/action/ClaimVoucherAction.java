package com.jboa.action;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jboa.common.Constants;
import com.jboa.entity.ClaimVoucher;
import com.jboa.entity.ClaimVoucherDetail;
import com.jboa.entity.Employee;
import com.jboa.service.ClaimVoucherService;

/**
 * 报销单Action(继承了公有的BaseAction)
 * 
 * @author 86185
 *
 */
public class ClaimVoucherAction extends BaseAction<ClaimVoucher> {
	// private ClaimVoucherService claimVoucherService;
	//serviceMap 根据不同的职务创建不同权限的service
	private Map<String, ClaimVoucherService> claimVoucherServicesMapping;
	//报销单基本信息
	private ClaimVoucher claimVoucher;
	//报销单具体信息集合
	private List<ClaimVoucherDetail> detailList;
	//查询的状态
	private String status;
	//查询的开始时间
	private Date startDate;
	//查询的结束时间
	private Date endDate;

	/*
	 * private static Map<String, String> statusMap;
	 * 
	 * static { statusMap = new LinkedHashMap<>();
	 * statusMap.put(Constants.CLAIMVOUCHER_CREATED, "新创建");
	 * statusMap.put(Constants.CLAIMVOUCHER_SUBMITTED, "已提交");
	 * statusMap.put(Constants.CLAIMVOUCHER_APPROVING, "待审批");
	 * statusMap.put(Constants.CLAIMVOUCHER_BACK, "已打回");
	 * statusMap.put(Constants.CLAIMVOUCHER_APPROVED, "已审批");
	 * statusMap.put(Constants.CLAIMVOUCHER_PAID, "已付款");
	 * statusMap.put(Constants.CLAIMVOUCHER_TERMINATED, "已终止"); }
	 */
	/**
	 * 根据不同的职务权限获取不同的报销单状态初始化
	 * @return
	 */
	public Map<String, String> getStatusMap() {
		// return statusMap;
		return this.getClaimVoucherService().getStatusMap();
	}

	/*
	 * public void setStatusMap(Map<String, String> statusMap) {
	 * ClaimVoucherAction.statusMap = statusMap; }
	 */

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public void setClaimVoucherServicesMapping(Map<String, ClaimVoucherService> claimVoucherServicesMapping) {
		this.claimVoucherServicesMapping = claimVoucherServicesMapping;
	}

	public Map<String, ClaimVoucherService> getClaimVoucherServicesMapping() {
		return claimVoucherServicesMapping;
	}

	/**
	 * 根据不同的职务获取不同的service
	 * @return
	 */
	public ClaimVoucherService getClaimVoucherService() {
		return this.claimVoucherServicesMapping.get(this.getPositionForShort());
	}
	/*
	 * public ClaimVoucherService getClaimVoucherService() { return
	 * claimVoucherService; }
	 * 
	 * public void setClaimVoucherService(ClaimVoucherService
	 * claimVoucherService) { this.claimVoucherService = claimVoucherService; }
	 */

	public ClaimVoucher getClaimVoucher() {
		return claimVoucher;
	}

	public void setClaimVoucher(ClaimVoucher claimVoucher) {
		this.claimVoucher = claimVoucher;
	}

	public List<ClaimVoucherDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<ClaimVoucherDetail> detailList) {
		this.detailList = detailList;
	}

	/**
	 * 保存请求处理方法
	 * @return
	 */
	public String saveClaimVoucher() {
		claimVoucher.setCreator(this.getEmployee());
		if (claimVoucher.getStatus().equals(Constants.CLAIMVOUCHER_SUBMITTED)) {
			claimVoucher.setNextDeal((Employee) this.getSession().get(Constants.AUTH_EMPLOYEE_MANAGER));
		} else {
			claimVoucher.setNextDeal(claimVoucher.getCreator());
		}
		claimVoucher.setDetailList(detailList);
		for (ClaimVoucherDetail claimVoucherDetail : detailList) {
			claimVoucherDetail.setBizClaimVoucher(claimVoucher);
		}
		getClaimVoucherService().saveClaimVoucher(claimVoucher);
		return "redirectList";
	}

	/**
	 * 查看报销单处理方法
	 * @return
	 */
	public String searchClaimVoucher() {
		//根据登录员工的不同职务获取状态初始化
		switch (this.getEmployee().getSysPosition().getNameCn()) {
		case Constants.POSITION_CASHIER:
			this.cashStuffStatusInit();
			break;
		case Constants.POSITION_FM:
			this.fmStuffStatusInit();
			break;
		case Constants.POSITION_GM:
			this.gmStuffStatusInit();
			break;
		default:
			this.stuffStatusInit();
			break;
		}
		//根据不同的service获取不同权限查看报销的集合
		pageSupport = getClaimVoucherService().getClaimVoucherPage(this.getEmployee(), status, startDate, endDate,
				pageNo, pageSize);
		return "list";
	}

	//查看具体某个报销单请求的处理
	public String getClaimVoucherById() {
		claimVoucher = getClaimVoucherService().findClaimVoucherById(claimVoucher.getId());
		return "view";
	}

	protected void stuffStatusInit() {
		//普通员工的默认状态是 “全部”
	}

	protected void fmStuffStatusInit() {
		
	}

	protected void gmStuffStatusInit() {
		//总经理默认的状态是 “待审批”
		if (status == null) {
			status = Constants.CLAIMVOUCHER_APPROVING;
		}
	}

	protected void cashStuffStatusInit() {
		//财务默认的状态是 “已审批”
		if (status == null) {
			status = Constants.CLAIMVOUCHER_APPROVED;
		}
	}

	/**
	 * 跳转到更新报销单信息页面请求的处理方法
	 * @return
	 */
	public String toUpdate() {
		claimVoucher = getClaimVoucherService().findClaimVoucherById(claimVoucher.getId());
		return "update";
	}

	/**
	 * 更新报销单信息请求的处理方法
	 * @return
	 */
	public String updateClaimVoucher() {
		// 方案1：映射文件控制不参与update操作的字段，update="false"
		/*
		 * if
		 * (claimVoucher.getStatus().equals(Constants.CLAIMVOUCHER_SUBMITTED)) {
		 * claimVoucher.setNextDeal((Employee)this.getSession().get(Constants.
		 * AUTH_EMPLOYEE_MANAGER)); } else {
		 * claimVoucher.setNextDeal(this.getEmployee()); }
		 * claimVoucher.setDetailList(detailList); for (ClaimVoucherDetail
		 * claimVoucherDetail : detailList) {
		 * claimVoucherDetail.setBizClaimVoucher(claimVoucher); }
		 */

		// 方案2：利用持久态，不允许修改的字段不要重新赋值，首先查询，为需要修改的属性赋值
		if (claimVoucher.getStatus().equals(Constants.CLAIMVOUCHER_SUBMITTED)) {
			claimVoucher.setNextDeal((Employee) this.getSession().get(Constants.AUTH_EMPLOYEE_MANAGER));
		}

		claimVoucher.setDetailList(detailList);

		getClaimVoucherService().updateClaimVoucher(claimVoucher);
		return "redirectList";
	}

	/**
	 * 删除报销单信息请求的处理方法
	 * @return
	 */
	public String deleteClaimVoucherById() {
		getClaimVoucherService().deleteClaimVoucherByid(claimVoucher);
		return "redirectList";
	}

	/**
	 * 跳转到审核页面请求的处理方法
	 * @return
	 */
	public String toCheck() {
		claimVoucher = getClaimVoucherService().findClaimVoucherById(claimVoucher.getId());
		return "check";
	}

}
