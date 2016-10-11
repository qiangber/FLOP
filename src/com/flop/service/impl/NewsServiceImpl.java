package com.flop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.flop.model.News;
import com.flop.service.inter.NewsServiceInter;
import com.flop.utils.HibernateUtils;

@Service("newsService")
public class NewsServiceImpl implements NewsServiceInter {
	
	@Override
	public News findById(int id) {
		String hql = "from News where id=?";
		String [] parameters = {id+""};
		List<News> list = HibernateUtils.executeQuery(hql, parameters);
		return list.get(0);
	}
	
	@Override
	public List<News> findAll(int pageSize ,int pageNow) {
		String hql = "from News order by id desc";
		List<News> list = HibernateUtils.executeQueryByPage(hql, null, pageSize, pageNow);
		return list;
	}
	
	@Override
	public void update(News news) {
		HibernateUtils.merge(news);
	}
	
	@Override
	public void add(News news) {
		HibernateUtils.save(news);
	}
	
	@Override
	public void delete(int id) {
		String hql="delete from News where id=?";
		String [] parameters={id+""};
		HibernateUtils.executeUpdate(hql, parameters);	
	}
	
	@Override
	public int getPageCount(int pageSize) {
		if (pageSize <= 0) {
			return 1;
		}
		String hql = "select count(*) from News";
		Object object = HibernateUtils.uniqueQuery(hql, null);
		int rowCount = Integer.parseInt(object.toString());
		return (rowCount - 1) / pageSize + 1;
	}
	
	@Override
	public List<News> search(String title, Date date) {
		Session session = null;
		List<News> list = new ArrayList<News>();
		try {
			session = HibernateUtils.openSession();
			Criteria c = session.createCriteria(News.class);
			if (title != null) {
				c.add(Restrictions.like("title", "%"+title+"%"));
			}
			if (date != null) {
				c.add(Restrictions.eq("date", date));
			}
			list = c.list();			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return list;
	}
}
