package com.jboa.action;

import com.jboa.entity.CheckResult;
import com.jboa.service.CheckResultService;

/**
 *  审核Action(继承公有BaseAction)
 * @author 86185
 *
 */
public class CheckResultAction extends BaseAction<CheckResult> {
	private CheckResult checkResult;
	private CheckResultService checkResultService;
	
	public CheckResultService getCheckResultService() {
		return checkResultService;
	}
	public void setCheckResultService(CheckResultService checkResultService) {
		this.checkResultService = checkResultService;
	}
	public CheckResult getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(CheckResult checkResult) {
		this.checkResult = checkResult;
	}
	
	/**
	 * 根据传入的报销单信息进行审核请求处理
	 * @return
	 */
	public String checkClaimVoucher(){
		checkResult.setCheckEmployee(this.getEmployee());
		checkResultService.saveCheckResult(checkResult);
		return SUCCESS;
	}
}
