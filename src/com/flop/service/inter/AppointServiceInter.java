package com.flop.service.inter;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.flop.model.Appointment;
import com.flop.model.Category;
import com.flop.model.UserInfo;

@Service
public interface AppointServiceInter {
	
	public List<UserInfo> findTeacher(String type, String categoryId);
	public List<Category> findCategory(String type, String teacherId);
	public List<Appointment> findAppoint(String type, String teacherId, String categoryId);
	public Appointment findById(String id);
	public String find(Appointment appoint);
	public List<Appointment> findByUserIdAndType(String userId, String type);
	public List<Appointment> findByUserIdAndType(String userId, String type, int pageSize ,int pageNow);
	public String add(Appointment appoint);
	public int getPageCount(String userId, String type, int pageSize);
	public List<String> getDate();
	public List<Appointment> getAppointByDate(DateTime start, int plus, String type, String userId);
}
