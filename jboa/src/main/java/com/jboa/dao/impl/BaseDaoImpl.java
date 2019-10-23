package com.jboa.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.jboa.dao.BaseDao;

/**
 * BaseDao接口的实现类(抽象类，可以拥有抽象方法也可以不拥有)
 * @author 86185
 *
 */
@SuppressWarnings("unchecked")
//利用泛型的形式
public abstract class BaseDaoImpl<T> extends HibernateDaoSupport implements BaseDao<T> {

	//自定义一个泛型的入参对象(减少自定义操作)
	private Class<T> cls;
	
	//无参构造方法(初始化入参的泛型的对象类型)
	public BaseDaoImpl() {
		//这个方法的作用就是通过实现的类获取要操作的Dao对象
		cls = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	@Override
	public void save(T instance) {
		this.getHibernateTemplate().save(instance);
	}

	@Override
	public void update(T instance) {
		this.getHibernateTemplate().update(instance);
	}

	@Override
	public void saveOrUpdate(T instance) {
		this.getHibernateTemplate().saveOrUpdate(instance);
	}

	@Override
	public T merge(T instance) {
		return this.getHibernateTemplate().merge(instance);
	}

	@Override
	public void delete(T instance) {
		this.getHibernateTemplate().delete(instance);
	}

	@Override
	public void delete(Collection<T> instances) {
		this.getHibernateTemplate().deleteAll(instances);
	}
	
	/**
	 * 根据入参id集合删除多个 的具体实现
	 */
	@Override
	public Integer delete(final Object[] ids) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				//拼接公用的Sql 语句(使用 in 子查询)
				String sql = "delete from " + cls.getSimpleName() + "where id in(:ids)";
				//使用setParameterList(String,List);方法入参
				return session.createQuery(sql).setParameterList("ids", ids).executeUpdate();
			}
		});
	}

	@Override
	public T get(Serializable id) {
		return this.getHibernateTemplate().get(cls, id);
	}

	/**
	 * 查找所有对象信息 具体操作
	 */
	@Override
	public List<T> findAll() {
		//拼接共有的sql 语句
		return this.getHibernateTemplate().find("from " + cls.getSimpleName());
	}
	
	/**
	 * 根据自定义sql 语句、入参体条件和页码翻页查询具体操作
	 */
	@Override
	public <E> List<E> findForPage(String hql, int pageNo, int pageSize, Object... pars) {
		//使用了Hibernate的 HibernateCallback<List<E>> 回调方法 目的是使用SessionFactory对象
		return this.getHibernateTemplate().execute(new HibernateCallback<List<E>>(){
			//重写了 doInHibernate(Session session) 方法
			@Override
			public List<E> doInHibernate(Session session) throws HibernateException, SQLException {
				//执行SQL语句并入参
				Query query = session.createQuery(hql);
				if (pars!=null && pars.length>0) {
					for (int i = 0; i < pars.length; i++) {
						query.setParameter(i, pars[i]);
					}
				}
				//执行Hibernate自带的翻页方法
				return query.setFirstResult((pageNo-1)*pageSize).setMaxResults(pageSize).list();
			}
		});
	}
	
	/**
	 * doInHibernate(Session session)
	 */
	@Override
	public Number getTotalCount(String hql, Object... pars) {
		
		//根据条件情况拼接Sql 语句
		if (hql.startsWith("select ")) {
			hql = "select count(*) " + hql.substring(hql.indexOf(" from ")+1);
		}else{
			hql = "select count(*)" + hql;
		}
		return (Number) this.getHibernateTemplate().find(hql,pars).get(0);
	}
	
	@Override
	public <E> List<E> find(String hql, Object... pars) {
		return this.getHibernateTemplate().find(hql,pars);
	}

}
