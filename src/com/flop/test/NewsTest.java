package com.flop.test;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.flop.model.News;
import com.flop.service.impl.NewsServiceImpl;
import com.flop.utils.HibernateUtils;

public class NewsTest {
	
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
	public void testSaveNews() {
		News news = new News("title2", "content2", new Date());		 
		session.save(news);
	}
	
	@Test
	public void test() {
		NewsServiceImpl service = new NewsServiceImpl();
		List<News> list = service.search("啊", new Date());
		System.out.println(list.size());
		for (News news : list) {
			System.out.println(news.getTitle());
		}
	}
}
