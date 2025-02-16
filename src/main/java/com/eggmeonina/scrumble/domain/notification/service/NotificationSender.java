package com.eggmeonina.scrumble.domain.notification.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.eggmeonina.scrumble.domain.notification.dto.NotificationMessage;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationSubScribeRequest;

public interface NotificationSender {

	/**
	 * 클라이언트 구독
	 */
	SseEmitter subscribe(NotificationSubScribeRequest request);

	/**
	 * 알림 메세지 전송
	 */
	void sendNotification(Long memberId, NotificationMessage message);

	/**
	 * eventName으로 알림 메세지 전송
	 */
	void sendNotification(Long memberId, String eventName, Object message);
}
