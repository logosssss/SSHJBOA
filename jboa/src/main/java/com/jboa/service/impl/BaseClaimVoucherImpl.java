package com.jboa.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jboa.common.Constants;
import com.jboa.dao.CheckResultDao;
import com.jboa.dao.ClaimVouCherDetailDao;
import com.jboa.dao.ClaimVoucherDao;
import com.jboa.entity.ClaimVoucher;
import com.jboa.entity.ClaimVoucherDetail;
import com.jboa.entity.Employee;
import com.jboa.service.ClaimVoucherService;
import com.jboa.util.PaginationSupport;

/**
 * 报销单Service 抽象实现类(共有类)
 * @author 86185
 *
 */
public abstract class BaseClaimVoucherImpl implements ClaimVoucherService {
	
	private ClaimVoucherDao claimVouCherDao;
	private ClaimVouCherDetailDao claimVouCherDetailDao;
	private CheckResultDao checkResultDao;
	
	public CheckResultDao getCheckResultDao() {
		return checkResultDao;
	}
	public void setCheckResultDao(CheckResultDao checkResultDao) {
		this.checkResultDao = checkResultDao;
	}
	public ClaimVouCherDetailDao getClaimVouCherDetailDao() {
		return claimVouCherDetailDao;
	}
	public void setClaimVouCherDetailDao(ClaimVouCherDetailDao claimVouCherDetailDao) {
		this.claimVouCherDetailDao = claimVouCherDetailDao;
	}
	public void setClaimVouCherDao(ClaimVoucherDao claimVouCherDao) {
		this.claimVouCherDao = claimVouCherDao;
	}
	public ClaimVoucherDao getClaimVouCherDao() {
		return claimVouCherDao;
	}
	
	/**
	 * 保存报销单信息的具体实现方法(直接利用Hibernate持久状态操作，不用手动保存信息)
	 */
	@Override
	public void saveClaimVoucher(ClaimVoucher claim) {
		claim.setCreateTime(new Date());
		claim.setModifyTime(claim.getCreateTime());
		claimVouCherDao.save(claim);
	}
	
	/**
	 * 翻页操作具体方法
	 * 入参：登录的职工、报销单的状态、开始时间、结束时间、页码、大小
	 */
	@Override
	public PaginationSupport<ClaimVoucher> getClaimVoucherPage(Employee employee, String status, Date startDate,
			Date endDate, Integer pageNo, Integer pageSize) {
		//System.out.println(status);
		PaginationSupport<ClaimVoucher> result = new PaginationSupport<ClaimVoucher>();
		
		if (pageNo>0) {
			result.setCurrPageNo(pageNo);
		}
		if (pageSize>0) {
			result.setPageSize(pageSize);
		}
		//查询的sql 语句
		StringBuffer sb = new StringBuffer("");
		//入参的集合
		List<Object>values = new ArrayList<Object>();
		//调用 buildBaseSql() 方法封装 sql语句 并入参条件
		this.buildBaseSql(employee, status, startDate, endDate,sb,values);
		//总记录数
		Integer count = claimVouCherDao.getTotalCount(sb.toString(),values.toArray()).intValue();
		result.setTotalCount(count);
		
		if (count>0) {
			int pageCount = (count%result.getPageSize()==0 ? count/result.getPageSize():count/result.getPageSize()+1);
			result.setTotalPageCount(pageCount);
			if (result.getCurrPageNo()>pageCount) {
				result.setCurrPageNo(pageCount);
			}
			/*if (status==null || status.length()==0) {
				sb.append(" order by c.status,c.createTime desc");
			}else{
				sb.append(" order by c.createTime desc");
			}*/
			
			this.addOrders(status, sb);
			//根据封装的sql 语句查询报销单集合
			List<ClaimVoucher> claList = claimVouCherDao.findForPage(sb.toString(), result.getCurrPageNo(), result.getPageSize(), values.toArray());
			result.setItems(claList);
		}
		return result;
	}
	
	/**
	 * 根据入参sql 语句封装查询语句
	 * 
	 * @param employee
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @param sb
	 * @param object
	 */
	protected abstract void buildBaseSql(Employee employee, String status, Date startDate,
			Date endDate,StringBuffer sb,List<Object> object);
	
	/**
	 * 封装排序语句
	 * @param status
	 * @param sb
	 */
	protected abstract void addOrders( String status,StringBuffer sb);

	@Override
	public ClaimVoucher findClaimVoucherById(Long id) {
		return claimVouCherDao.get(id);
	}
	
	@Override
	public void updateClaimVoucher(ClaimVoucher claim) {
		//方案1：映射文件控制不参与update操作的字段，update="false"
		/*claimVouCherDetailDao.deleteByClaimVoucher(claim);
		claim.setModifyTime(new Date());
		claimVouCherDao.update(claim);*/
		
		//方案2：利用持久态，不允许修改的字段不要重新赋值，首先查询，为需要修改的属性赋值
		claimVouCherDetailDao.deleteByClaimVoucher(claim);
		ClaimVoucher toUpdate = claimVouCherDao.get(claim.getId());
		toUpdate.setModifyTime(new Date());
		toUpdate.setTotalAccount(claim.getTotalAccount());
		toUpdate.setEvent(claim.getEvent());
		
		if (claim.getStatus().equals(Constants.CLAIMVOUCHER_SUBMITTED)) {
			toUpdate.setStatus(claim.getStatus());
			toUpdate.setNextDeal(claim.getNextDeal());
		}
		toUpdate.setDetailList(claim.getDetailList());
		
		for (ClaimVoucherDetail claimVoucherDetail : toUpdate.getDetailList()) {
			claimVoucherDetail.setBizClaimVoucher(toUpdate);
		}
	}
	
	@Override
	public void deleteClaimVoucherByid(ClaimVoucher claim) {
		//删除相关明细
		claimVouCherDetailDao.deleteByClaimVoucher(claim);
		//删除审核 
		checkResultDao.deleteByClaimVouCher(claim);
		//删除报销单
		claimVouCherDao.delete(claim);
	}

}
