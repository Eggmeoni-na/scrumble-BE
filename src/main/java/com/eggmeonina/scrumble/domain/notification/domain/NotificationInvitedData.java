package com.eggmeonina.scrumble.domain.notification.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationInvitedData {
	private final String memberName;
	private final String squadName;
	private final NotificationType notificationType;
	private final Long squadId;
}
