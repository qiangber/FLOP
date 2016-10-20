package com.flop.service.inter;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flop.model.User;
import com.flop.model.UserInfo;

@Service
public interface UserServiceInter {
	
	public UserInfo CheckUser(User user);
	public UserInfo CheckAdminUser(User user);
	public boolean CheckUsername(String username);
	public List<User> findAll(int pageSize ,int pageNow, String type);
	public List<User> findAll(int pageSize ,int pageNow, String type, String name, String num);
	public User findById(String id);
	public void update(User user);
	public void delete(String id);
	public void add(Object obj);
	public int getPageCount(int pageSize, String type);
	public int getPageCount(int pageSize, String type, String name, String num);
}
