package com.flop.test;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.flop.model.About;
import com.flop.model.News;
import com.flop.service.impl.NewsServiceImpl;
import com.flop.utils.HibernateUtils;

public class AboutTest {
	
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
		About about = new About();
		about.setType("question");
		about.setContent("content");
		 
		session.save(about);
	}
}
