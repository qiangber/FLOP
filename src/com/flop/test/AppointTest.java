package com.flop.test;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.flop.model.SpeakingAppointment;
import com.flop.service.impl.AppointServiceImpl;
import com.flop.service.inter.AppointServiceInter;
import com.flop.utils.HibernateUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mvc.xml"})
public class AppointTest {
	
	private Session session;
	
	@Autowired
	private AppointServiceInter appointService;
	
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
		SpeakingAppointment a = new SpeakingAppointment();
		a.setDate(new Date());
		a.setLesson(6);
		a.setUserId("20");
		a.setCategoryId("1");
		a.setPublishTime(new Date());
		a.setPlace("kb212");
		appointService.add(a);
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
