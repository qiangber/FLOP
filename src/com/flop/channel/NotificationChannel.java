package com.flop.channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ServerEndpoint(value = "/notification-channel")
public class NotificationChannel {
	
	private static final Log LOG = LogFactory.getLog(NotificationChannel.class);
	
	public static final Map<Session, String> SESSIONS = new ConcurrentHashMap<Session, String>();
	
	public NotificationChannel() {}
	
	@OnOpen
	public void onOpen(final Session session) {
		String userId = (String) Channels.getHttpParameter(session, "userId"); 
		if (StringUtils.isBlank(userId)) {
            return;
		}
		if (SESSIONS.keySet().contains(session)) {
			return;
		}
		LOG.info("Websocket Start Connecting: "+ userId + session);
		SESSIONS.put(session, userId);
	}
	
	@OnMessage
	public void onMessage(final String message) {
		
	}
	
	@OnError
	public void onError(final Session session, final Throwable error) {
		LOG.error(error);
		SESSIONS.remove(session);
	}
	
	@OnClose
	public void onClose(final Session session, final CloseReason closeReason) {
		LOG.info(closeReason);
		SESSIONS.remove(session);
	}
}