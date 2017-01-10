package com.flop.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flop.model.Category;
import com.flop.service.inter.CategoryServiceInter;
import com.flop.utils.HibernateUtils;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryServiceInter {

	@Override
	public List<Category> findByType(String type) {
		String hql = "from Category where type=?";
		String [] parameters = {type};
		List<Category> list = HibernateUtils.executeQuery(hql, parameters);
		return list;
	}
	
	@Override
	public List<Category> findAll(int pageSize ,int pageNow) {
		String hql = "from Category";
		List<Category> list = HibernateUtils.executeQueryByPage(hql, null, pageSize, pageNow);
		return list;
	}
	
	@Override
	public Category findById(String id) {
		String hql = "from Category where id=?";
		String [] parameters = {id};
		Category category = (Category)HibernateUtils.uniqueQuery(hql, parameters);
		return category;
	}

	@Override
	public boolean delete(int id) {
		String hql="delete from Category where id=?";
		String [] parameters={id+""};
		return HibernateUtils.executeUpdate(hql, parameters);		
	}

	@Override
	public boolean update(Category category) {
		String hql = "update Category set name=?,type=? where id=?";
		String [] parameters = {category.getName(),category.getType(),category.getId()+""};
		return HibernateUtils.executeUpdate(hql, parameters);
	}
	
	@Override
	public boolean add(Object obj) {
		return HibernateUtils.save(obj);
	}

	@Override
	public int getPageCount(int pageSize) {
		String hql = "select count(*) from Category";
		Object object = HibernateUtils.uniqueQuery(hql, null);
		int rowCount = Integer.parseInt(object.toString());
		return (rowCount-1)/pageSize+1  ;
	}
	
	@Override
	public Integer findIdByName(String name, String type) {
		String hql = "select id from Category where name = ? and type = ?";
		String [] parameters = {name, type};
		return (Integer) HibernateUtils.uniqueQuery(hql, parameters);
	}
}