package com.flop.service.inter;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.flop.model.Order;
import com.flop.model.Status;

@Service
public interface OrderServiceInter {
	
	public boolean deal(String status, String id);
	public List<Order> findAll(int pageSize ,int pageNow, String type);
	public List<Order> findAll(int pageSize ,int pageNow, String type, String number);
	public List<Order> findAll(int pageSize ,int pageNow, String type,
			Date date, String place, int lesson, String obj, String num);
	public List<Order> findAllWriting();
	public Order findById(String id);
	public boolean find(Order order);
	public Map<String, Object> findToDeal(String userId, String type, int pageSize ,int pageNow);
	public List<Order> findToDeal(int pageSize ,int pageNow);
	public List<Order> findToView(String userId, String type);
	public boolean delete(String id);
	public Status add(Order order);
	public int getPageCount(int pageSize, String type, String userId);
	public int getPageCountByNum(int pageSize, String type, String num);
	public int getSearchPageCount(int pageSize, String type, 
			Date date, String place, int lesson, String obj, String num);
	public int getAllPageCount(int pageSize, String type);
	public Status cancel(String orderId);
	public boolean close(String appointId);
	public List<Order> findByAppointId(String appointId);
}
