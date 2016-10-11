package com.flop.test;

import java.util.Date;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.flop.model.Notification;
import com.flop.model.Order;
import com.flop.utils.HibernateUtils;

public class NotificationTest {

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
		Order order = (Order)session.get(Order.class, 32);
		Notification n = new Notification();
		n.setDate(new Date());
		n.setOrder(order);
		n.setUserId("6");
		n.setHasRead("0");
		n.setType(Notification.ORDER_CLIENT);
		session.save(n);
	}

}
