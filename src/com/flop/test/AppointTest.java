package com.flop.test;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.flop.model.Category;
import com.flop.model.User;
import com.flop.model.WritingAppointment;
import com.flop.service.impl.AppointServiceImpl;
import com.flop.service.inter.AppointServiceInter;
import com.flop.utils.HibernateUtils;

public class AppointTest {
	
	private Session session;
	
	@Before
	public void setUp() throws Exception {
		 session = HibernateUtils.openSession(); // 生成一个session
		 session.beginTransaction();
	}

	@After
	public void tearDown() throws Exception {
		session.getTransaction().commit(); // 提交事务
	    session.close(); // 关闭session
	}
	
	@Test
	public void testSave() {
		WritingAppointment a = new WritingAppointment();
		a.setUserInfo(((User)session.get(User.class, 20)).getUserInfo());
		a.setCategory((Category)session.get(Category.class, 1));
		a.setDate(new Date());
		a.setLesson(4);
		session.merge(a);		
	}
	
	@Test
	public void get() {
		AppointServiceInter s = new AppointServiceImpl();
		List<String> list = s.getDate();
		for (String date : list) {
			System.out.println(date);
		}
	}
}
