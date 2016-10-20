package com.flop.service.inter;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flop.model.About;

@Service
public interface AboutServiceInter {
	public About findByType(String type);
	public List<About> findAll();
	public void update(About a);
}
