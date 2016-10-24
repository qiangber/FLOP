package com.flop.service.inter;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flop.model.Resource;

@Service
public interface ResourceServiceInter {
	public Resource findById(String id);
	public List<Resource> findAll();
	public List<Resource> findAll(int pageSize ,int pageNow);
	public void update(Resource Resource);
	public void add(Resource Resource);
	public void delete(String id);
	public int getPageCount(int pageSize);
}
