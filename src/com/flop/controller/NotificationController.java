package com.flop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.flop.service.inter.NotificationServiceInter;

@Controller
@RequestMapping("/notification")
public class NotificationController {
	
	@Autowired
	private NotificationServiceInter notificationService;
	
	@RequestMapping("/json/list")
	public @ResponseBody Map list(@RequestParam(value="userId", required=true) String userId,
			@RequestParam(value="page", required=false, defaultValue="1") Integer page,
			@RequestParam(value="pageSize", required=false, defaultValue="10") Integer pageSize) {
		Map map = new HashMap(16);
		map.put("notifications", notificationService.getNotifications(userId, pageSize, page));
		map.put("pageCount", notificationService.getNotificationsCount(userId, pageSize));
		return map;
	}
	
	@RequestMapping("/json/unreadCount")
	public @ResponseBody int getUnreadCount(@RequestParam(value="userId", required=true) String userId) {
		return notificationService.getNewNotificationsCount(userId);
	}
}
