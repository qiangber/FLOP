package com.flop.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flop.model.User;
import com.flop.model.UserInfo;
import com.flop.service.inter.UserServiceInter;
import com.flop.utils.HibernateUtils;

@Service("userService")
public class UserServiceImpl implements UserServiceInter {

	@Override
	public UserInfo CheckUser(User user) {
		String hql="from User where username=? and password=?";
		String []parameters ={user.getUsername(),user.getPassword()};
		List<User> list = HibernateUtils.executeQuery(hql, parameters);
		if(list.size()==1){
			return findById(list.get(0).getId() + "").getUserInfo();
		}else{
			return null;			
		}
	}
	
	/**
	 * @param username 用户名
	 * @return true 该用户名不存在,false 该用户名已存在
	 */
	@Override
	public boolean CheckUsername(String username) {
		String hql="from User where username = ?";
		String []parameters ={username};
		List<User> list = HibernateUtils.executeQuery(hql, parameters);
		if(list.size()==1){
			return false;
		}else{
			return true;			
		}
	}
	
	@Override
	public UserInfo CheckAdminUser(User user) {
		String hql="from User where username=? and password=? and userInfo.type = 'admin'";
		String []parameters ={user.getUsername(),user.getPassword()};
		List<User> list = HibernateUtils.executeQuery(hql, parameters);
		if(list.size()==1){
			return findById(list.get(0).getId() + "").getUserInfo();
		}else{
			return null;			
		}
	}

	@Override
	public List<User> findAll(int pageSize ,int pageNow, String type) {
		String hql="from User where userInfo.type = ? order by id";
		String[] params = {type};
		List<User> list =HibernateUtils.executeQueryByPage(hql, params, pageSize, pageNow);
		return list;
	}
	
	@Override
	public List<User> findAll(int pageSize ,int pageNow, String type, String name, String num) {
		StringBuilder hql = new StringBuilder("from User where userInfo.type = ?");
		if (name != null) {
			hql.append(" and userInfo.name like '%").append(name).append("%'");					
		}
		if (num != null) {
			hql.append(" and userInfo.username like '%").append(num).append("%'"); 
		}
		hql.append(" order by id");
		String[] params = {type};
		List<User> list =HibernateUtils.executeQueryByPage(hql.toString(), params, pageSize, pageNow);
		return list;
	}

	@Override
	public User findById(String id) {
		String hql="from User where id=?";
		String [] parameters={id};
		return (User)HibernateUtils.uniqueQuery(hql, parameters);		
	}

	@Override
	public void update(User user) {
		HibernateUtils.merge(user);
	}

	@Override
	public void delete(String id) {
		String hql="delete from User where id=?";
		String [] parameters={id};
		HibernateUtils.executeUpdate(hql, parameters);		
	}

	@Override
	public void add(Object obj) {
		HibernateUtils.save(obj);		
	}

	@Override
	public int getPageCount(int pageSize, String type) {
		String hql = "select count(*) from UserInfo where type = ?";
		String[] params = {type};
		Object object = HibernateUtils.uniqueQuery(hql, params);
		int rowCount = Integer.parseInt(object.toString());
		return (rowCount-1)/pageSize+1  ;
	}
	
	@Override
	public int getPageCount(int pageSize, String type, String name, String num) {
		StringBuilder hql = new StringBuilder("select count(*) from UserInfo where type = ?");
		if (name != null) {
			hql.append(" and name like '%").append(name).append("%'");					
		}
		if (num != null) {
			hql.append(" and username like '%").append(num).append("%'"); 
		}
		hql.append(" order by id");
		String[] params = {type};
		Object object = HibernateUtils.uniqueQuery(hql.toString(), params);
		int rowCount = Integer.parseInt(object.toString());
		return (rowCount-1)/pageSize+1  ;
	}
}