package com.flop.utils;

import java.time.LocalDateTime;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.flop.service.inter.UserServiceInter;

/**
 * 
 * @author xuqiang
 * update students' chance for appointment at fixed time.
 */
@Component
public class ScheduledTask {
	@Autowired
	private UserServiceInter userService;
	
	private static Logger logger = Logger.getLogger(ScheduledTask.class.getName());
	
	@Scheduled(cron="0 1 0 ? * MON")
	public void updateChance() {
		logger.info("update chance on " + LocalDateTime.now());
		userService.setChance();
	}
}
