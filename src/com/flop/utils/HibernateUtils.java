package com.flop.utils;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtils {
	private static SessionFactory sessionFactory = null;
	// 使用线程局部模式
	private static ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();

	private HibernateUtils() {
	};

	static {
		Configuration configuration = new Configuration().configure();
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}

	// 获取全新的session
	public static Session openSession() {
		return sessionFactory.openSession();
	}

	// 获取和线程关联的session
	public static Session getCurrentSession() {

		Session session = threadLocal.get();
		// 判断是否得到
		if (session == null) {
			session = sessionFactory.openSession();
			// 把session对象设置到 threadLocal,相当于该session已经和线程绑定
			threadLocal.set(session);
		}
		return session;
	}
	
	public static void closeSession() {
		Session session = threadLocal.get();
		if (session != null && session.isOpen()) {
			session.close();
		}
		threadLocal.set(null);
	}

	public static List executeQuery(String hql, String[] parameters) {
		Session session = null;
		List list = null;
		try {
			session = HibernateUtils.openSession();
			Query query = session.createQuery(hql);
			// 链式写法
			if (parameters != null && parameters.length > 0) {
				for (int i = 0; i < parameters.length; i++) {
					query.setString(i, parameters[i]);
				}
			}
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return list;
	}

	public static List executeQueryByPage(String hql, String[] parameters,
			int pageSize, int pageNow) {
		Session session = null;
		List list = null;
		try {
			session = HibernateUtils.openSession();
			Query query = session.createQuery(hql);
			if (parameters != null && parameters.length > 0) {
				for (int i = 0; i < parameters.length; i++) {
					query.setString(i, parameters[i]);
				}
			}
			list = query.setFirstResult((pageNow - 1) * pageSize)
					.setMaxResults(pageSize).list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return list;
	}

	public static boolean executeUpdate(String hql, String[] parameters) {
		boolean flag = true;
		Session session = null;
		Transaction transaction = null;
		try {
			session = openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			if (parameters != null && parameters.length > 0) {
				for (int i = 0; i < parameters.length; i++) {
					query.setString(i, parameters[i]);
				}
			}
			query.executeUpdate();
			transaction.commit();

		} catch (Exception e) {
			flag = false;
			throw new RuntimeException(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return flag;
	}

	public static boolean save(Object object) {
		boolean flag = true;
		Session session = null;
		Transaction transaction = null;
		try {
			session = openSession();
			transaction = session.beginTransaction();
			session.save(object);
			transaction.commit();
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return flag;
	}
	
	public static boolean merge(Object object) {
		boolean flag = true;
		Session session = null;
		Transaction transaction = null;
		try {
			session = openSession();
			transaction = session.beginTransaction();
			session.merge(object);
			transaction.commit();
		} catch (Exception e) {
			flag = false;
			throw new RuntimeException(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return flag;
	}

	public static Object uniqueQuery(String hql, String[] parameters) {
		Session session = null;
		Object object = null;
		try {
			session = openSession();
			Query query = session.createQuery(hql);
			if (parameters != null && parameters.length > 0) {
				for (int i = 0; i < parameters.length; i++) {
					query.setString(i, parameters[i]);
				}
			}
			object = query.uniqueResult();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return object;
	}
}
