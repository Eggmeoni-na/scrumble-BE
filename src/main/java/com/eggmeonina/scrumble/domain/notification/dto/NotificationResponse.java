package com.eggmeonina.scrumble.domain.notification.dto;

import java.time.LocalDateTime;

import com.eggmeonina.scrumble.common.util.JsonObjectConverter;
import com.eggmeonina.scrumble.domain.notification.domain.Notification;
import com.eggmeonina.scrumble.domain.notification.domain.NotificationInvitedData;
import com.eggmeonina.scrumble.domain.notification.domain.NotificationMessage;
import com.eggmeonina.scrumble.domain.notification.domain.NotificationStatus;
import com.eggmeonina.scrumble.domain.notification.domain.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

	private Long notificationId;
	private NotificationType notificationType;
	private String notificationMessage;
	private LocalDateTime createdAt;
	private boolean isRead;
	private NotificationStatus notificationStatus;
	private NotificationData notificationData;

	public static NotificationResponse from(Notification notification) {
		NotificationInvitedData notificationData =
			(NotificationInvitedData)JsonObjectConverter.JsonToObject(notification.getNotificationData(), NotificationInvitedData.class);
		return new NotificationResponse
			(
				notification.getId(),
				notification.getNotificationType(),
				NotificationMessage.getNotificationMessage(notification.getNotificationType(), notification.getNotificationData()),
				notification.getCreatedAt(),
				notification.isReadFlag(),
				notification.getNotificationStatus(),
				new NotificationData(notificationData.getSquadId())
			);
	}
}
