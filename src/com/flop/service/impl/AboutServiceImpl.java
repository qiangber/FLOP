package com.flop.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flop.model.About;
import com.flop.service.inter.AboutServiceInter;
import com.flop.utils.HibernateUtils;

@Service("aboutService")
public class AboutServiceImpl implements AboutServiceInter {
	
	@Override
	public About findByType(String type) {
		String hql = "from About where type=?";
		String [] parameters = {type+""};
		About about = (About)HibernateUtils.uniqueQuery(hql, parameters);
		return about;
	}
	
	@Override
	public List<About> findAll() {
		String hql = "from About order by id";
		List<About> list = HibernateUtils.executeQuery(hql, null);
		return list;
	}
	
	@Override
	public void update(About about) {
		HibernateUtils.merge(about);
	}
}
