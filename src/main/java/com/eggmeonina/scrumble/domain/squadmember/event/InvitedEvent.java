package com.eggmeonina.scrumble.domain.squadmember.event;

import com.eggmeonina.scrumble.domain.notification.domain.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class InvitedEvent {
	private final Long recipientId;
	private final String recipientName;
	private final Long squadId;
	private final String squadName;
	private final NotificationType notificationType;
}
