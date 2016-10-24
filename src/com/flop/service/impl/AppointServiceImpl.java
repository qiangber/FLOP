package com.flop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flop.model.Appointment;
import com.flop.model.Category;
import com.flop.model.UserInfo;
import com.flop.service.inter.AppointServiceInter;
import com.flop.service.inter.CategoryServiceInter;
import com.flop.service.inter.UserServiceInter;
import com.flop.utils.HibernateUtils;

@Service("appointService")
public class AppointServiceImpl implements AppointServiceInter {
	
	@Autowired
	private UserServiceInter userService;
	
	@Autowired
	private CategoryServiceInter categoryService;
	
	@Override
	public List<UserInfo> findTeacher(String type) {
		Session session = null;
		List<UserInfo> list = new ArrayList<UserInfo>();
		try {
			session = HibernateUtils.openSession();
			String hql = "";
			if (type.equals("writing")) {			
				hql = "select distinct a.userInfo from WritingAppointment a";
			} else if (type.equals("speaking")) {
				hql = "select distinct a.userInfo from SpeakingAppointment a";
			}
			hql = hql.concat(" where a.date >= ? and a.date <= ? and a.status != 'close'");
			DateTime today = new DateTime(); 
			list = session.createQuery(hql)
					.setDate(0, today.plusDays(2).toDate())
					.setDate(1, today.plusDays(7).toDate()).list();			
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
			String hql = "";
			if (type.equals("writing")) {			
				hql = "select distinct a.category from WritingAppointment a where a.userInfo.id = ?";
			} else if (type.equals("speaking")) {
				hql = "select distinct a.category from SpeakingAppointment a where a.userInfo.id = ?";
			} else if (type.equals("lab")) {
				hql = "select distinct a.category from LabAppointment a where a.userInfo.id = ?";
				teacherId = "1";
			}
			hql = hql.concat(" and a.date >= ? and a.date <= ? and a.status != 'close'");
			session = HibernateUtils.openSession();
			DateTime today = new DateTime();
			list = session.createQuery(hql).setString(0, teacherId)
					.setDate(1, today.plusDays(2).toDate())
					.setDate(2, today.plusDays(7).toDate()).list();			
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
			String hql = "from Appointment where type = ? and category.id = ? and userInfo.id = ?"
					+ " and date >= ? and a.date <= ? and status != 'close' order by date, lesson";
			session = HibernateUtils.openSession();
			DateTime today = new DateTime();
			appoints = session.createQuery(hql).setString(0, type).setString(1, categoryId)
					.setString(2, teacherId)
					.setDate(3, today.plusDays(2).toDate())
					.setDate(4, today.plusDays(7).toDate())
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
					+ "order by date, lesson";		
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
					+ "order by date, lesson";		
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
			String hql="from Appointment where date = ? and lesson = ? and userInfo.id = ?";
			List<Appointment> appointList = (List<Appointment>)session.createQuery(hql).setDate(0, appoint.getDate())
					.setInteger(1, appoint.getLesson())
					.setString(2, appoint.getUserId()).list();
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
	public String add(Appointment obj) {
		String status = find(obj);
		if (status.equals("false")) {
			obj.setUserInfo(userService.findById(obj.getUserId()).getUserInfo());
			obj.setCategory(categoryService.findById(obj.getCategoryId()));
			return HibernateUtils.save(obj) == true ? "success" : "error";			
		} else {
			return status;
		}
	}

	@Override
	public int getPageCount(int pageSize) {
		Session session = null;
		int rowCount = 0;
		try {
			session = HibernateUtils.openSession();;
			String hql = "select count(*) from Appointment where userInfo.id = ? and type = ? and date >= ?";		
			Object object = session.createQuery(hql)
					.setString(0, "1").setString(1, "lab").setDate(2, new Date()).uniqueResult();
			rowCount = Integer.parseInt(object.toString());						
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return (rowCount-1)/pageSize+1  ;
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
}
