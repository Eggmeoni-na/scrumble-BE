package com.eggmeonina.scrumble.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationUnreadExistResponse {
	private boolean hasUnreadMessages;
}
