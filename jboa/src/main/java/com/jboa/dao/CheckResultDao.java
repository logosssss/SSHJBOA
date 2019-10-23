package com.jboa.dao;

import com.jboa.entity.CheckResult;
import com.jboa.entity.ClaimVoucher;

/**
 * 审核Dao接口(继承BaseDao接口)
 * @author 86185
 *
 */
public interface CheckResultDao extends BaseDao<CheckResult> {
	/**
	 * 根据报销单对象删除审核的信息
	 * @param claim
	 * @return
	 */
	public int deleteByClaimVouCher(ClaimVoucher claim);
	
}
