package com.eggmeonina.scrumble.domain.notification.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationConstants {
	public static final Long DEFAULT_TIMEOUT = 60L * 1000 * 30;
	public static final Long START_DELAY_SECONDS = 50L;

	public static final Long DELAY_SECONDS = 50L;
	public static final String NOTIFICATION_EVENT_NAME = "notificationEvent";
	public static final String HEARTBEAT_EVENT_NAME = "heartbeatEvent";
}
