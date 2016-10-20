package com.flop.service.inter;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.flop.model.News;

@Service
public interface NewsServiceInter {
	public News findById(int id);
	public List<News> findAll(int pageSize ,int pageNow);
	public int getPageCount(int pageSize);
	public void update(News news);
	public void add(News news);
	public void delete(int id);
	public List<News> search(String title, Date date);
}
