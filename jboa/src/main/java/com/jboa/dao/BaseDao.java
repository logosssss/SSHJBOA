package com.jboa.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * ͨdao层base接口(公有接口)
 * @author 86185
 *
 */
//使用泛型类的方式(不同的Dao实现不同的操作)
public interface BaseDao<T> {
	/**
	 * 根据入参的一个对象保存数据
	 * @param instance
	 */
	public void save(T instance);
	
	/**
	 * 根据入参的一个对象更新信息
	 * @param instance
	 */
	public void update(T instance);
	
	/**
	 * 根据入参的一个对象保存或更新信息
	 * @param instance
	 */
	public void saveOrUpdate(T instance);
	
	/**
	 * 合并方法
	 * @param instance
	 * @return
	 */
	public T merge(T instance);
	
	/**
	 * 根据入参对象删除一个
	 * @param instance
	 */
	public void delete(T instance);
	
	/**
	 * 根据集合对象入参删除多个
	 * @param instances
	 */
	public void delete(Collection<T> instances);
	
	/**
	 * 根据入参id集合删除多个
	 * @param ids
	 * @return
	 */
	public Integer delete(Object[] ids);
	
	/**
	 * 根据主键查询一个对象信息
	 * @param id
	 * @return
	 */
	public T get(Serializable id);
	
	/**
	 * 查找所有对象信息
	 * @return
	 */
	public List<T> findAll();// from Object
	
	/**
	 * 根据自定义sql 语句、入参体条件和页码翻页查询
	 * @param hql
	 * @param pageNo
	 * @param pageSize
	 * @param pars
	 * @return
	 */
	public <E> List<E> findForPage(String hql,int pageNo,int pageSize,Object... pars);//ͶӰ��ѯ���Զ���
	
/*	public <E> List<E> findForPage(String hql,int pageNo,int pageSize,Object obj);//ͶӰ��ѯ���Զ���
	public <E> List<E> findForPage(String hql,int pageNo,int pageSize,Object[] values);//ͶӰ��ѯ���Զ���
	public <E> List<E> findForPage(String hql,int pageNo,int pageSize,Map<String,Object> map);//ͶӰ��ѯ���Զ���
	public <E> List<E> findForPage(String hql,int pageNo,int pageSize);//ͶӰ��ѯ���Զ���
*/
	/**
	 * 根据sql语句 和入参条件查询总数量
	 * @param hql
	 * @param pars
	 * @return
	 */
	public Number getTotalCount(String hql,Object... pars);
	
	/**
	 * 根据sql 语句入参条件查询集合对象
	 * @param hql
	 * @param pars
	 * @return
	 */
	public <E> List<E> find(String hql,Object... pars);
}
