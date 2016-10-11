package com.flop.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flop.model.Resource;
import com.flop.service.inter.ResourceServiceInter;
import com.flop.utils.HibernateUtils;

@Service("resourceService")
public class ResourceServiceImpl implements ResourceServiceInter {
	
	@Override
	public Resource findById(String id) {
		String hql = "from Resource where id = ?";
		String [] parameters = {id};
		Resource r = (Resource)HibernateUtils.uniqueQuery(hql, parameters);
		return r;
	}
	
	@Override
	public List<Resource> findAll() {
		String hql = "from Resource order by id";
		List<Resource> list = HibernateUtils.executeQuery(hql, null);
		return list;
	}
	
	@Override
	public List<Resource> findAll(int pageSize ,int pageNow) {
		String hql = "from Resource order by id";
		List<Resource> list = HibernateUtils.executeQueryByPage(hql, null, pageSize, pageNow);
		return list;
	}
	
	@Override
	public void update(Resource r) {
		HibernateUtils.merge(r);
	}
	@Override
	public void add(Resource r) {
		HibernateUtils.save(r);
	}
	
	@Override
	public void delete(String id) {
		String hql="delete from Resource where id = ?";
		String [] parameters={id};
		HibernateUtils.executeUpdate(hql, parameters);	
	}
	
	@Override
	public int getPageCount(int pageSize) {
		if (pageSize <= 0) {
			return 1;
		}
		String hql = "select count(*) from Resource";
		Object object = HibernateUtils.uniqueQuery(hql, null);
		int rowCount = Integer.parseInt(object.toString());
		return (rowCount - 1) / pageSize + 1;
	}
}
