package com.flop.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.flop.model.Type;
import com.flop.service.inter.TypeServiceInter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mvc.xml"})
public class TypeTest {
	
	@Autowired
	TypeServiceInter typeService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		System.out.println(typeService.findAll());
	}

	@Test
	public void get() {
		System.out.println(typeService.getLimit("writing"));
	}
	
	@Test
	public void update() {
		Type type = new Type();
		type.setName("writing");
		type.setNum(50);
		typeService.update(type);
	}
}
