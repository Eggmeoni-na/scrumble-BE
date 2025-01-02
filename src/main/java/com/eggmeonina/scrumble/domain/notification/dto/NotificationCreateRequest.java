package com.eggmeonina.scrumble.domain.notification.dto;

import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.notification.domain.Notification;
import com.eggmeonina.scrumble.domain.notification.domain.NotificationStatus;
import com.eggmeonina.scrumble.domain.notification.domain.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationCreateRequest {
	private final Long memberId;
	private final Long squadId;
	private final String squadName;
	private final NotificationType notificationType;
	private final String notificationData;

	public static Notification to(NotificationCreateRequest request, Member member) {
		return Notification.create()
			.recipient(member)
			.notificationType(request.getNotificationType())
			.readFlag(false)
			.notificationData(request.getNotificationData())
			.build();
	}
}
