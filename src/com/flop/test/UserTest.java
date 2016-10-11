package com.flop.test;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.flop.model.About;
import com.flop.model.News;
import com.flop.model.User;
import com.flop.model.UserInfo;
import com.flop.service.impl.NewsServiceImpl;
import com.flop.utils.HibernateUtils;

public class UserTest {
	
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
		UserInfo ui = new UserInfo();
		ui.setType("Teacher");
		ui.setName("liao");
		ui.setUsername("4");
		
		User u = new User(ui.getUsername(), "4");
		u.setUserInfo(ui);
		
		session.save(u);
	}
	
	@Test
	public void testDelete() {
		User u = (User)session.get(User.class, 0);
		session.delete(u);
	}
	
	@Test
	public void testGet() {
		User u = (User)session.get(User.class, 1);
		System.out.println(u.getUserInfo().getType());
	}
	
	@Test
	public void testUpdate() {
		UserInfo ui = (UserInfo)session.get(UserInfo.class, 10);
		ui.setName("liaoT");
		ui.setUsername("111");
		
		User u = (User)session.get(User.class, 10);
		u.setUserInfo(ui);
		u.setUsername(ui.getUsername());
		
		session.merge(u);
	}
}
