package com.jboa.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.jboa.dao.ClaimVouCherDetailDao;
import com.jboa.entity.ClaimVoucher;
import com.jboa.entity.ClaimVoucherDetail;

/**
 * 具体报销单信息Dao实现类(继承BaseDao实现类和继承报销单具体信息Dao接口)
 * @author 86185
 *
 */
public class ClaimVoucherDetailDaoImpl extends BaseDaoImpl<ClaimVoucherDetail> implements ClaimVouCherDetailDao {

	@Override
	public int deleteByClaimVoucher(ClaimVoucher claimVoucher) {
		String hql = "delete from ClaimVoucherDetail c where c.bizClaimVoucher.id = ?";
		return this.getHibernateTemplate().bulkUpdate(hql,claimVoucher.getId());
	}
	
}
