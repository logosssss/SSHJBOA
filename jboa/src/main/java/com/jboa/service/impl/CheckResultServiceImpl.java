package com.jboa.service.impl;

import java.util.Date;

import com.jboa.common.Constants;
import com.jboa.dao.CheckResultDao;
import com.jboa.dao.ClaimVoucherDao;
import com.jboa.dao.EmployeeDao;
import com.jboa.entity.CheckResult;
import com.jboa.entity.ClaimVoucher;
import com.jboa.entity.Employee;
import com.jboa.service.CheckResultService;

public class CheckResultServiceImpl implements CheckResultService {
	private CheckResultDao checkResultDao;
	private ClaimVoucherDao claimVoucherDao;
	private EmployeeDao employeeDao;

	public EmployeeDao getEmployeeDao() {
		return employeeDao;
	}

	public void setEmployeeDao(EmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}

	public ClaimVoucherDao getClaimVoucherDao() {
		return claimVoucherDao;
	}

	public void setClaimVoucherDao(ClaimVoucherDao claimVoucherDao) {
		this.claimVoucherDao = claimVoucherDao;
	}

	public void setCheckResultDao(CheckResultDao checkResultDao) {
		this.checkResultDao = checkResultDao;
	}

	public CheckResultDao getCheckResultDao() {
		return checkResultDao;
	}

	@Override
	public void saveCheckResult(CheckResult checkResult) {
		checkResult.setCheckTime(new Date());
		checkResultDao.save(checkResult);

		ClaimVoucher claimVoucher = claimVoucherDao.get(checkResult.getClaimId());
		claimVoucher.setModifyTime(checkResult.getCheckTime());
		this.updateClaimVoucherStatus(checkResult.getCheckEmployee().getSysPosition().getNameCn(),
				checkResult.getResult(), claimVoucher);
	}

	protected void updateClaimVoucherStatus(String position, String checkResult, ClaimVoucher claim) {
		// 通過
		if (Constants.CHECKRESULT_PASS.equals(checkResult)) {
			// FM通过
			if (Constants.POSITION_FM.equals(position)) {
				// >=5000
				if (claim.getTotalAccount() >= 5000) {
					claim.setStatus(Constants.CLAIMVOUCHER_APPROVING);
					claim.setNextDeal(employeeDao.getGeneralManager());
				}
				// <5000
				else {
					claim.setStatus(Constants.CLAIMVOUCHER_APPROVED);
					claim.setNextDeal(employeeDao.getCashier());
				}

			}
			// GM通过
			else if (Constants.POSITION_GM.equals(position)) {
				claim.setStatus(Constants.CLAIMVOUCHER_APPROVED);
				claim.setNextDeal(employeeDao.getCashier());
			}
			// Cashier通过
			else if (Constants.POSITION_CASHIER.equals(position)) {
				claim.setStatus(Constants.CLAIMVOUCHER_PAID);
				claim.setNextDeal(null);
			}

		}
		// 拒絕
		else if (Constants.CHECKRESULT_REJECT.equals(checkResult)) {
			claim.setStatus(Constants.CLAIMVOUCHER_TERMINATED);
			claim.setNextDeal(null);
		}
		// 打回
		else if (Constants.CHECKRESULT_BACK.equals(checkResult)) {
			claim.setStatus(Constants.CLAIMVOUCHER_BACK);
			claim.setNextDeal(claim.getCreator());
		}
	}

}
