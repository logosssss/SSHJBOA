package com.jboa.dao.impl;

import com.jboa.dao.CheckResultDao;
import com.jboa.entity.CheckResult;
import com.jboa.entity.ClaimVoucher;

/**
 * 审核Dao实现类继承BaseDao的实现类和实现审核接口
 * @author 86185
 *
 */
public class CheckResultDaoImpl extends BaseDaoImpl<CheckResult> implements CheckResultDao {
	public int deleteByClaimVouCher(ClaimVoucher claim){
		String hql = "delete from CheckResult c where c.claimId = ?";
		return this.getHibernateTemplate().bulkUpdate(hql,claim.getId());
	}
}
