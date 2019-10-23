package com.jboa.service;

import java.util.Date;
import java.util.Map;

import com.jboa.entity.ClaimVoucher;
import com.jboa.entity.Employee;
import com.jboa.util.PaginationSupport;

/**
 * 报销单 Service 接口
 * @author 86185
 *
 */
public interface ClaimVoucherService {
	
	/**
	 * 根据报销单对象保存信息
	 * @param claim
	 */
	public void saveClaimVoucher(ClaimVoucher claim);
	/*public PaginationSupport<ClaimVoucher> getClaimVoucherPage(String createSn,String status,Date startDate,Date endDate,
			Integer pageNo,Integer pageSize);*/
	
	/**
	 * 根据条件翻页查询报销单
	 * 
	 * @param employee
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport<ClaimVoucher> getClaimVoucherPage(Employee employee,String status,Date startDate,Date endDate,
			Integer pageNo,Integer pageSize);
	
	/**
	 * 根据报销单id查找报销单对象
	 * @param id
	 * @return
	 */
	public ClaimVoucher findClaimVoucherById(Long id);
	
	/**
	 * 根据报销单对象更新信息
	 * @param claim
	 */
	public void updateClaimVoucher(ClaimVoucher claim);
	
	/**
	 * 根据报销单信息删除对象
	 * @param claim
	 */
	public void deleteClaimVoucherByid(ClaimVoucher claim);
	
	/**
	 * 根据报销单状态id 获取报销单中文表述
	 * @return
	 */
	public Map<String, String>getStatusMap();
}
