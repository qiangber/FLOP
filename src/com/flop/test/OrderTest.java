package com.flop.test;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.flop.model.About;
import com.flop.model.Appointment;
import com.flop.model.News;
import com.flop.model.Order;
import com.flop.model.User;
import com.flop.model.WritingAppointment;
import com.flop.service.impl.AppointServiceImpl;
import com.flop.service.impl.NewsServiceImpl;
import com.flop.service.impl.OrderServiceImpl;
import com.flop.service.inter.AppointServiceInter;
import com.flop.service.inter.OrderServiceInter;
import com.flop.utils.HibernateUtils;

public class OrderTest {
	
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
	public void getAndDelete() {
		OrderServiceInter orderService = new OrderServiceImpl();
		Order order = orderService.findById("152");
		orderService.delete("152");
		System.out.println(order.getId());
	}
}
