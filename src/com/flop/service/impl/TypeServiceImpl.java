package com.flop.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flop.model.Type;
import com.flop.service.inter.TypeServiceInter;
import com.flop.utils.HibernateUtils;

@Service("typeService")
public class TypeServiceImpl implements TypeServiceInter {

	@Override
	public boolean update(Type type) {		
		String hql = "update Type set num = ? where name = ?";
		String[] parameters = {type.getNum() + "", type.getName()};
		return HibernateUtils.executeUpdate(hql, parameters);
	}

	@Override
	public int getLimit(String name) {
		String hql = "select num from Type where name = ?";
		String[] parameters = {name};
		Integer limit = (Integer) HibernateUtils.uniqueQuery(hql, parameters);
		return limit;
	}

	@Override
	public List<Type> findAll() {
		String hql = "from Type order by id";
		return (List<Type>) HibernateUtils.executeQuery(hql, null);
	}
	
}
