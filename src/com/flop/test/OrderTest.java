package com.flop.test;

import java.util.List;

import org.hibernate.Session;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.flop.model.About;
import com.flop.model.Appointment;
import com.flop.model.News;
import com.flop.model.Order;
import com.flop.model.User;
import com.flop.service.impl.AppointServiceImpl;
import com.flop.service.impl.NewsServiceImpl;
import com.flop.service.impl.OrderServiceImpl;
import com.flop.service.inter.AppointServiceInter;
import com.flop.service.inter.OrderServiceInter;
import com.flop.utils.HibernateUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mvc.xml"})
public class OrderTest {
	
	private Session session;
	
	@Autowired
	private OrderServiceInter orderService;
	
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
	
	@Test
	public void save() {
		Order order = new Order();
		order.setUserId("20");
		order.setTime(new DateTime().toString("YYYY-MM-dd HH:mm:ss"));
		order.setStatus("verify");
		order.setAppointmentId("287");
		orderService.add(order);
	}
	
	@Test
	public void test() {
		session.createQuery("update Appointment set num = num - 1 where id = :id")
			.setString("id", "301")
			.executeUpdate();
	}
}
