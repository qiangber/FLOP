package com.flop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flop.model.Appointment;
import com.flop.model.Category;
import com.flop.model.UserInfo;
import com.flop.service.inter.AppointServiceInter;
import com.flop.service.inter.CategoryServiceInter;
import com.flop.service.inter.TypeServiceInter;
import com.flop.service.inter.UserServiceInter;
import com.flop.utils.HibernateUtils;

@Service("appointService")
public class AppointServiceImpl implements AppointServiceInter {
	
	@Autowired
	private UserServiceInter userService;
	
	@Autowired
	private CategoryServiceInter categoryService;
	
	@Autowired
	private TypeServiceInter typeService;
	
	@Override
	public List<UserInfo> findTeacher(String type, String categoryId) {
		Session session = null;
		List<UserInfo> list = new ArrayList<UserInfo>();
		try {
			session = HibernateUtils.openSession();
			String hql = "select distinct a.userInfo from Appointment a where "
					+ "a.date >= :start and a.date <= :end and a.type = :type "
					+ "and a.status != 'close' and a.num > 0";
			if (categoryId != null && !categoryId.equals("")) {
				hql = hql.concat(" and a.categoryId = :categoryId");
			}
			DateTime today = new DateTime();
			Query query = session.createQuery(hql).setString("type", type)
					.setDate("start", today.plusDays(2).toDate())
					.setDate("end", today.plusDays(7).toDate());
			if (categoryId != null && !categoryId.equals("")) {
				query.setParameter("categoryId", categoryId);
			}
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
	
	@Override
	public List<Category> findCategory(String type, String teacherId) {
		Session session = null;
		List<Category> list = new ArrayList<Category>();
		try {
			if (type.equals("lab")) {
				teacherId = "1";
			}
			String hql = "select distinct a.category from Appointment a where "
					+ "a.date >= :start and a.date <= :end and a.type = :type "
					+ "and a.status != 'close' and a.num > 0";
			if (teacherId != null && !teacherId.equals("")) {
				hql = hql.concat(" and a.userId = :teacherId");
			}
			session = HibernateUtils.openSession();
			DateTime today = new DateTime();
			int end = type.equals("lab") ? 30 : 7;
			Query query = session.createQuery(hql).setString("type", type)
					.setDate("start", today.plusDays(2).toDate())
					.setDate("end", today.plusDays(end).toDate());
			if (teacherId != null && !teacherId.equals("")) {
				query.setString("teacherId", teacherId);
			}			
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
	
	@Override
	public List<Appointment> findAppoint(String type, String teacherId, String categoryId) {
		Session session = null;
		List<Appointment> appoints = new ArrayList<Appointment>();
		try {			
			if (type.equals("lab")) {
				teacherId = "1";
			}
			String hql = "from Appointment where type = :type and category.id = :categoryId and userInfo.id = :teacherId"
					+ " and date >= :start and date <= :end and status != 'close' and num > 0 order by date, lesson";
			session = HibernateUtils.openSession();
			DateTime today = new DateTime();
			int end = type.equals("lab") ? 30 : 7;
			appoints = session.createQuery(hql).setString("type", type).setString("categoryId", categoryId)
					.setString("teacherId", teacherId)
					.setDate("start", today.plusDays(2).toDate())
					.setDate("end", today.plusDays(end).toDate())
					.list();			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return appoints;
	}

	@Override
	public Appointment findById(String id) {
		String hql="from Appointment where id = ?";
		String [] parameters={id};
		Appointment appoint = (Appointment)HibernateUtils.uniqueQuery(hql, parameters);
		return appoint;
	}
	
	@Override
	public List<Appointment> findByUserIdAndType(String userId, String type) {
		Session session = null;
		List<Appointment> list = new ArrayList<Appointment>();
		try {
			session = HibernateUtils.openSession();
			String hql = "from Appointment where userInfo.id = ? and type = ? and date >= ? "
					+ "and status != 'close' order by date, lesson";		
			list = session.createQuery(hql)
					.setString(0, userId).setString(1, type).setDate(2, new Date()).list();			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return list;
	}
	
	@Override
	public List<Appointment> findByUserIdAndType(String userId, String type, int pageSize ,int pageNow) {
		Session session = null;
		List<Appointment> list = new ArrayList<Appointment>();
		try {
			session = HibernateUtils.openSession();
			String hql = "from Appointment where userInfo.id = ? and type = ? and date >= ? "
					+ "and status != 'close' order by publishTime desc";		
			list = session.createQuery(hql)
					.setString(0, userId).setString(1, type).setDate(2, new Date())
					.setFirstResult((pageNow - 1) * pageSize).setMaxResults(pageSize).list();			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return list;
	}
	
	@Override
	public String find(Appointment appoint) {
		Session session = null;
		String flag = "false";
		try {
			session = HibernateUtils.openSession();
			String hql;
			List<Appointment> appointList;
			if (appoint.getUserId().equals("1")) {
				hql = "from Appointment where date = ? and lesson = ? and userId = ? and categoryId = ? and status != 'close'";
				appointList = (List<Appointment>)session.createQuery(hql).setDate(0, appoint.getDate())
						.setInteger(1, appoint.getLesson())
						.setString(2, appoint.getUserId())
						.setString(3, appoint.getCategoryId()).list();
			} else {
				hql = "from Appointment where date = ? and lesson = ? and userId = ? and status != 'close'";
				appointList = (List<Appointment>)session.createQuery(hql).setDate(0, appoint.getDate())
						.setInteger(1, appoint.getLesson())
						.setString(2, appoint.getUserId()).list();				
			}
			if (appointList.size() > 0) {
				flag = appointList.get(0).getCategory().getType();
			} else {
				flag = "false";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return flag;
	}

	@Override
	public String add(Appointment appoint) {
		String status = find(appoint); //查重
		if (status.equals("false")) {
			appoint.setUserInfo(userService.findById(appoint.getUserId()).getUserInfo());
			appoint.setCategory(categoryService.findById(appoint.getCategoryId()));
			appoint.setNum(typeService.getLimit(appoint.getType()));
			return HibernateUtils.save(appoint) ? "success" : "error";			
		} else {
			return status;
		}
	}

	@Override
	public int getPageCount(String userId, String type, int pageSize) {
		Session session = null;
		int rowCount = 0;
		try {
			session = HibernateUtils.openSession();;
			String hql = "select count(*) from Appointment where userInfo.id = ? and type = ? and date >= ? and status != 'close'";		
			Object object = session.createQuery(hql)
					.setString(0, userId).setString(1, type).setDate(2, new Date()).uniqueResult();
			rowCount = Integer.parseInt(object.toString());						
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return (rowCount-1) / pageSize + 1  ;
	}
	
	@Override
	public List<String> getDate() {
		DateTime today = new DateTime();
		List<String> dateList = new ArrayList<String>();
		for (int i = 3; i < 8; i++) {
			DateTime date = today.plusDays(i);
			if (date.getDayOfWeek() != 6 && date.getDayOfWeek() != 7) {
				dateList.add(date.toString("yyyy/MM/dd"));				
			}
		}
		return dateList;
	}
	
	@Override
	public List<Appointment> getAppointByDate(DateTime start, int plus, String type, String userId) {
		Session session = null;
		List<Appointment> list = new ArrayList<>();
		try {
			String hql = "from Appointment where date >= :start and date <= :end "
					+ "and type = :type and userId = :userId";
			session = HibernateUtils.openSession();
			Query query = session.createQuery(hql).setString("type", type)
					.setString("userId", userId)
					.setDate("start", start.toDate())
					.setDate("end", start.plusDays(plus).toDate());
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
