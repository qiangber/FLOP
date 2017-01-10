package com.flop.service.inter;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flop.model.Type;

@Service
public interface TypeServiceInter {
	public boolean update(Type type);
	public int getLimit(String name);
	public List<Type> findAll();
}
