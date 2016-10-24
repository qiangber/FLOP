package com.flop.service.inter;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.flop.model.Notification;

@Service
public interface NotificationServiceInter {
	public List<Map<String, String>> getNotifications(String userId, int pageSize, int pageNow);
	public int getNotificationsCount(String userId, int pageSize);
	public int getNewNotificationsCount(String userId);
	public void addNotification(Notification notification);
	public void mergeNotification(Notification notification);
	public Notification find(String userId, String orderId);
	public void notifyUser(String userId);
}
