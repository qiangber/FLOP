package com.flop.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import org.springframework.stereotype.Service;

import com.flop.channel.NotificationChannel;
import com.flop.controller.AppointmentController;
import com.flop.model.Notification;
import com.flop.service.inter.NotificationServiceInter;
import com.flop.utils.HibernateUtils;

@Service("notificationService")
public class NotificationServiceImpl implements NotificationServiceInter {
	
	@Override
	public List<Map<String, String>> getNotifications(String userId, int pageSize, int pageNow) {
		List<Map<String, String>> list = new LinkedList<Map<String,String>>();
		String hql = "from Notification where userId = ? order by hasRead, date desc, order.appoint.date desc";
		String[] parameters = {userId};
		List<Notification> l = HibernateUtils.executeQueryByPage(hql, parameters, pageSize, pageNow);
		for (Notification notification : l) {
			Map<String, String> map = new HashMap<String, String>(8);
			StringBuilder msg = new StringBuilder();
			if (notification.getType().equals(Notification.ORDER_CLIENT)) {
				msg.append(notification.getOrder().getAppoint().getUserInfo().getName());
				switch (notification.getOrder().getStatus()) {
				case "close":
					msg.append("关闭了");
					break;
				case "accept":
					msg.append("接受了你的");
					break;
				case "reject":
					msg.append("拒绝了你的");
					break;
				case "absent":
					msg.append("考勤-未到");
					break;
				case "arrived":
					msg.append("考勤-已到");
					break;
				default:
					break;
				}
			} else {
				msg.append(String.format("%s(%s)", notification.getOrder().getUserInfo().getName(), notification.getOrder().getUserInfo().getUsername()));
				msg.append(notification.getOrder().getStatus().equals("cancel") ? "取消了你的" : "向你发起了");
			}
			String orderType = notification.getOrder().getAppoint().getCategory().getType();
			if (orderType.equals("writing")) {
				msg.append("写作预约（");
			} else if (orderType.equals("speaking")) {
				msg.append("口语预约（");
			} else {
				msg.append("实验室预约（");
			}
			msg.append(notification.getOrder().getAppoint().getCategory().getName()).append(" ");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
			msg.append(sdf2.format(notification.getOrder().getAppoint().getDate())).append("  ");
			msg.append(AppointmentController.map.get(notification.getOrder().getAppoint().getLesson() + "")).append("）");
			map.put("msg", msg.toString());
			map.put("date", sdf1.format(notification.getDate()));
			list.add(map);
			notification.setHasRead("1");
			HibernateUtils.merge(notification);
		}
		return list;
	}
	
	@Override
	public int getNotificationsCount(String userId, int pageSize) {
		String hql = "select count(*) from Notification where userId = ?";
		String[] parameters = {userId};
		int count = Integer.parseInt(HibernateUtils.uniqueQuery(hql, parameters).toString());		
		return (count - 1) / pageSize + 1;
	}
	
	@Override
	public int getNewNotificationsCount(String userId) {
		String hql = "select count(*) from Notification where userId = ? and hasRead = 0";
		String[] parameters = {userId};
		Object object = HibernateUtils.uniqueQuery(hql, parameters);
		return Integer.parseInt(object.toString());
	}
	
	@Override
	public void addNotification(Notification notification) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateTime = sdf.format(notification.getDate());
		try {
			notification.setDate(sdf.parse(dateTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		HibernateUtils.save(notification);
	}
	
	@Override
	public void mergeNotification(Notification notification) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateTime = sdf.format(new Date());
		try {
			notification.setDate(sdf.parse(dateTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		HibernateUtils.merge(notification);
	}
	
	@Override
	public Notification find(String userId, String orderId) {
		String hql = "from Notification where userId = ? and order.id = ?";
		String[] parameters = {userId, orderId};
		Notification notification = (Notification)HibernateUtils.uniqueQuery(hql, parameters);
		return notification;
	}
	
	@Override
	public void notifyUser(final String userId) {
		for (final Map.Entry<Session, String> entry : NotificationChannel.SESSIONS.entrySet()) {
            final Session session = entry.getKey();
            final String id = entry.getValue();

            if (!id.equals(userId)) {
                continue;
            }

            if (session.isOpen()) {
                session.getAsyncRemote().sendText("1");
            }
        }
	}
}
