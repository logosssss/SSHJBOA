package com.jboa.dao;

import com.jboa.entity.ClaimVoucher;
import com.jboa.entity.ClaimVoucherDetail;

/**
 * 具体订单信息Dao接口(继承公有BaseDao)
 * @author 86185
 *
 */
public interface ClaimVouCherDetailDao extends BaseDao<ClaimVoucherDetail>{
	/**
	 * 根据报销单的对象删除具体报销单信息
	 * @param claimVoucher
	 * @return
	 */
	public int deleteByClaimVoucher(ClaimVoucher claimVoucher);
}
