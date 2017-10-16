package com.Poetry.Dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractBaseDAO<T, ID extends Serializable> {

//	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SessionFactory sessionFactory;
	private final transient Class<T> persistentClass;
	private Session session;

	@SuppressWarnings("unchecked")
	public AbstractBaseDAO() {
		persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	@SuppressWarnings("unchecked")
	public AbstractBaseDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	public final void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public final void setSession(final Session sess) {
		session = sess;
	}

	public final Session getSession() {
		if (session == null || !session.isOpen()) {
			session = sessionFactory.getCurrentSession();
		}
		return session;
	}

	public final Class<T> getPersistentClass() {
		return persistentClass;
	}

	public final T getById(ID id) {
		return (T) getSession().get(persistentClass, id);
	}

	public final List<T> findAll() {
		return findByCriteria();
	}

	public final List<T> deleteAll(final List<T> entities) {
		for (T entity : entities) {
			delete(entity);
		}
		return entities;
	}

	public final void delete(final T entity) {
		getSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public final List<T> findByExample(final T exampleInstance, final String... excludeProperty) {
		final Criteria crit = getSession().createCriteria(getPersistentClass());
		final Example example = Example.create(exampleInstance);
		for (String exclude : excludeProperty) {
			example.excludeProperty(exclude);
		}
		crit.add(example);
		return crit.list();
	}

	public final void flush() {
		getSession().flush();
	}

	public final void clear() {
		getSession().clear();
	}

	public final void evict(final T entity) {
		getSession().evict(entity);
	}

	protected final List<T> findByCriteria(final Criterion... criterion) {
		return findByCriteria(getSession(), null, criterion);
	}

	protected List<T> findInOrderByCriteria(Order order, Criterion... criterion) {
		return findByCriteria(getSession(), order, criterion);
	}

	@SuppressWarnings("unchecked")
	private List<T> findByCriteria(final Session session, final Order order, final Criterion... criterion) {
		final Criteria crit = session.createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		if (null != order) {
			crit.addOrder(order);
		}
		return crit.list();
	}

	public final List<T> fetchByActiveStatus(final Integer activeStatus) {
		return findByCriteria(getSession(), null, Restrictions.eq("isActive", activeStatus));
	}

	public final List<T> fetchByTenant(final ID tenantId) {
		return findByCriteria(getSession(), Order.asc("id"), Restrictions.eq("tenantId", tenantId));
	}

	public final List<T> fetchByStoreCode(final String code){
		return findByCriteria(getSession(), null,Restrictions.eq("storeCode", code));
	}
	
	public final List<T> fetchActiveByTenant(final ID tenantId) {
		return findByCriteria(getSession(), Order.asc("id"), Restrictions.eq("tenantId", tenantId),
				Restrictions.eq("isActive", 1));
	}

	public Object fetchMaxPropertyByTenant(final String propertyName, final Integer tenantId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.setProjection(Projections.max(propertyName));
		criteria.add(Restrictions.eq("tenantId", tenantId));
		criteria.setMaxResults(1);
		return criteria.uniqueResult();
	}

	public Object fetchMaxPropertyValue(final String propertyName) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.setProjection(Projections.max(propertyName));
		criteria.setMaxResults(1);
		return criteria.uniqueResult();
	}
	
	protected Boolean isExist(final String propertyName, final String propertyValue, final Integer id) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.neOrIsNotNull("id", id));
		criteria.add(Restrictions.eq(propertyName, propertyValue));
		criteria.setProjection(Projections.property(propertyName));
		criteria.setMaxResults(1);
		return criteria.uniqueResult() != null;
	}
	
	protected Boolean isExistByTenant(final String propertyName, final String propertyValue, final Integer id, final Integer tenantId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.neOrIsNotNull("id", id));
		criteria.add(Restrictions.eq(propertyName, propertyValue));
		criteria.add(Restrictions.eq("tenantId", tenantId));
		criteria.setProjection(Projections.property(propertyName));
		criteria.setMaxResults(1);
		return criteria.uniqueResult() != null;
	}
}