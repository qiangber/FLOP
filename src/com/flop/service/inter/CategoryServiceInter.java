package com.flop.service.inter;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flop.model.Category;

@Service
public interface CategoryServiceInter {
	
	public List<Category> findAll(int pageSize ,int pageNow);
	public Category findById(String id);
	public List<Category> findByType(String type);
	public boolean update(Category obj);
	public boolean delete(int id);
	public boolean add(Object obj);
	public int getPageCount(int pageSize);

}
