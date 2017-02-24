package com.flop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flop.model.Appointment;
import com.flop.model.Notification;
import com.flop.model.Order;
import com.flop.model.Status;
import com.flop.service.inter.AppointServiceInter;
import com.flop.service.inter.NotificationServiceInter;
import com.flop.service.inter.OrderServiceInter;
import com.flop.service.inter.UserServiceInter;
import com.flop.utils.HibernateUtils;

@Service("orderService")
public class OrderServiceImpl implements OrderServiceInter {

	@Autowired
	private UserServiceInter userService;
	
	@Autowired
	private AppointServiceInter appointService;
	
	@Autowired
	private NotificationServiceInter notificationService;
	
	@Override
	public boolean deal(String status, String id) {
		boolean flag = false;
		try {
			Order order = findById(id);
			order.setStatus(status);
			if (status.equals("absent")) {
				userService.decreaseCredit(order.getUserId());
			}
			flag = HibernateUtils.merge(order);
			notificationService.addNotification(new Notification(
					new Date(),
					Notification.ORDER_CLIENT,
					"0",
					order.getUserId(),
					order));
			notificationService.notifyUser(order.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	@Override
	public List<Order> findAll(int pageSize ,int pageNow, String type) {
		String hql = "select distinct o from Order o where "
				+ "o.appoint.type = ? order by o.time desc, o.appoint.lesson";
		String[] parameters = {type};
		List<Order> orders = HibernateUtils.executeQueryByPage(hql, parameters, pageSize, pageNow);
		return orders;
	}
	
	@Override
	public List<Order> findAll(int pageSize ,int pageNow, String type, String number) {
		String hql = "select distinct o from Order o where "
				+ "o.appoint.type = ? and o.userInfo.username = ? "
				+ "order by o.time desc, o.appoint.lesson";
		String[] parameters = {type, number};
		List<Order> orders = HibernateUtils.executeQueryByPage(hql, parameters, pageSize, pageNow);
		return orders;
	}
	
	@Override
	public List<Order> findAllWriting() {
		String hql = "select distinct o from Order o where o.appoint.type = 'writing' "
				+ "order by o.time desc";
		List<Order> orders = HibernateUtils.executeQuery(hql, null);
		return orders;
	}

	@Override
	public Order findById(String id) {
		String hql = "from Order where id=?";
		String [] parameters = {id};
		Order order = (Order)HibernateUtils.uniqueQuery(hql, parameters);
		return order;
	}
	
	@Override
	public Map<String, Object> findToDeal(String userId, String type, int pageSize ,int pageNow) {
		Session session = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {			
			session = HibernateUtils.openSession();
			String hql = "from Order where appoint.userInfo.id = ? and "
					+ "appoint.type = ? and "
					+ "appoint.status != 'close' and appoint.date >= ? order by appoint.date, appoint.lesson";
			List<Order> orders = session.createQuery(hql).setString(0, userId).setString(1, type).setDate(2, new Date())
					.setFirstResult((pageNow - 1) * pageSize).setMaxResults(pageSize).list();
			map = new HashMap<String, Object>(2);
			map.put("orders", orders);
			map.put("pageCount", getPageCount(pageSize, type, userId)+"");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return map;
	}
	
	@Override
	public List<Order> findToDeal(int pageSize ,int pageNow) {
		Session session = null;
		List<Order> labOrders = new ArrayList<Order>();
		try {
			session = HibernateUtils.openSession();;
			String hql = "from Order where appoint.type = 'lab'"
					+ " and appoint.status != 'close' and appoint.date >= ? and status != 'cancel'"
					+ " order by appoint.date, appoint.lesson";
			labOrders = session.createQuery(hql).setDate(0, new Date())
					.setFirstResult((pageNow-1)*pageSize).setMaxResults(pageSize).list();			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		
		return labOrders;
	}
	
	@Override
	public List<Order> findToView(String userId, String type) {
		Session session = null;
		List<Order> orders = new ArrayList<Order>();
		try {
			session = HibernateUtils.openSession();
			String hql = "from Order where userInfo.id = ? and appoint.type = ?"
					+ " and appoint.date >= ? order by appoint.date, appoint.lesson";
			orders = session.createQuery(hql).setString(0, userId).setString(1, type).setDate(2, new Date()).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return orders;
	}

	@Override
	public boolean delete(String id) {
		String hql = "delete from Order where id = ?";
		String [] parameters = {id};
		return HibernateUtils.executeUpdate(hql, parameters);		
	}

	@Override
	public boolean find(Order order) {
		String hql = "from Order where userInfo.id = ? and appoint.id = ? and status != 'cancle' and status != 'reject'";
		String[] parameters = {order.getUserId(), order.getAppointmentId()};
		return HibernateUtils.uniqueQuery(hql, parameters) == null;
	}
	
	@Override
	public Status add(Order order) {
		if (find(order)) {
			Session session = null;
			Transaction tx = null;
			try {
				session = HibernateUtils.getCurrentSession();
				tx = session.beginTransaction();
				int num = (Integer) session.createQuery("select num from Appointment where id = :id")
						.setString("id", order.getAppointmentId())
						.uniqueResult();
				if (num > 0) {
					order.setUserInfo(userService.findById(order.getUserId()).getUserInfo());
					order.setAppoint(appointService.findById(order.getAppointmentId()));
					if (order.getAppoint().getType().equals("lab")) {
						order.setStatus("verify");
					} else {
						order.setStatus("accept");
					}
					session.save(order);
					session.createQuery("update Appointment set num = num - 1 where id = :id")
						.setString("id", order.getAppointmentId())
						.executeUpdate();
					tx.commit();
					return new Status("success", "预约成功!");
				} else {
					return new Status("empty", "");
				}
			} catch (HibernateException e) {
				e.printStackTrace();
				tx.rollback();
				return new Status("error", "预约失败！");
			} finally {
				if (session != null && session.isOpen()) {
					session.close();
				}
			}			
		} else {
			return new Status("exist", "已预约过该时段！");
		}
	}

	@Override
	public int getPageCount(int pageSize, String type, String userId) {
		Session session = null;
		int rowCount = 0;
		try {
			session = HibernateUtils.openSession();
			String hql = "select count(*) from Order where appoint.category.type = ?"
					+ " and appoint.date >= ? and appoint.userInfo.id = ?"
					+ " and status != 'cancel'";
			Object object= session.createQuery(hql).setString(0, type)
					.setDate(1, new Date()).setString(2, userId).uniqueResult();
			rowCount = Integer.parseInt(object.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return (rowCount-1)/pageSize + 1;
	}
	
	/**
	 * 根据预约人的学号搜索相关预约
	 */
	@Override
	public int getPageCountByNum(int pageSize, String type, String number) {
		Session session = null;
		int rowCount = 0;
		try {
			session = HibernateUtils.openSession();
			String hql = "select count(*) from Order where appoint.category.type = ?"
					+ " and userInfo.username = ?";
			Object object= session.createQuery(hql).setString(0, type)
					.setString(1, number).uniqueResult();
			rowCount = Integer.parseInt(object.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return (rowCount-1)/pageSize + 1;
	}
	
	@Override
	public int getAllPageCount(int pageSize, String type) {
		Session session = null;
		int rowCount = 0;
		try {
			session = HibernateUtils.openSession();
			String hql = "select count(*) from Order where appoint.category.type = ?";
			Object object= session.createQuery(hql).setString(0, type).uniqueResult();
			rowCount = Integer.parseInt(object.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return (rowCount-1)/pageSize + 1;
	}
	
	@Override
	public Status cancel(String orderId) {
		String[] parameters = {orderId};
		String hql = "select a from Appointment a, Order o where o.id = ? and a.id = o.appointmentId";
		Appointment appoint = (Appointment)HibernateUtils.uniqueQuery(hql, parameters);		
		Date date = new Date();
		System.out.println(appoint.getDate() + ":" + date);
		long intervalMilli = appoint.getDate().getTime() - date.getTime();
		long intervalDays = intervalMilli / (24 * 60 * 60 * 1000);
		if (intervalDays >= 1) {
			Order order = findById(orderId);
			order.setStatus("cancel");			
			if (HibernateUtils.merge(order)) {
				Notification notification = notificationService.find(order.getAppoint().getUserId(), Integer.toString(order.getId()));
				notification.setHasRead("0");
				notification.setOrder(order);
				notificationService.mergeNotification(notification);
				notificationService.notifyUser(order.getAppoint().getUserId());
				return new Status("success", "成功取消！");
			} else {
				return new Status("error", "取消失败！");
			}
		} else {
			return new Status("error", "已超过时限，无法取消！");
		}
	}
	
	@Override
	public boolean close(String appointId) {
		String hql = "from Order where appoint.id = ?";
		String[] parameters = {appointId};
		List<Order> orderList = HibernateUtils.executeQuery(hql, parameters);
		boolean flag = true;
		for (Order order : orderList) {
			order.setStatus("close");
			if (HibernateUtils.merge(order)) {
				Notification notify = notificationService.find(order.getUserId(), Integer.toString(order.getId()));
				if (notify != null) {
					notify.setHasRead("0");
					notificationService.mergeNotification(notify);
				} else {
					notify = new Notification(
							new Date(), Notification.ORDER_CLIENT,
							"0", order.getUserId(), order);
					notificationService.addNotification(notify);
				}
				notificationService.notifyUser(order.getUserId());					
				flag = flag && true;
			}
		}
		return flag;
	}
	
	@Override
	public List<Order> findByAppointId(String appointId) {
		Session session = null;
		List<Order> list = new ArrayList<>();
		try {
			String hql = "from Order where appointmentId = :appointmentId";
			session = HibernateUtils.openSession();
			Query query = session.createQuery(hql).setString("appointmentId", appointId);
			list = query.list();			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}			
		}
		return list;
	}
}