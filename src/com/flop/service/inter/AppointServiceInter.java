package com.flop.service.inter;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flop.model.Appointment;
import com.flop.model.Category;
import com.flop.model.UserInfo;

@Service
public interface AppointServiceInter {
	
	public List<UserInfo> findTeacher(String type);
	public List<Category> findCategory(String type, String teacherId);
	public List<Appointment> findAppoint(String type, String teacherId, String categoryId);
	public Appointment findById(String id);
	public String find(Appointment id);
	public List<Appointment> findByUserIdAndType(String userId, String type);
	public List<Appointment> findByUserIdAndType(String userId, String type, int pageSize ,int pageNow);
	public String add(Appointment obj);
	public int getPageCount(int pageSize);
	public List<String> getDate(); 
}
