package com.eggmeonina.scrumble.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
	boolean hasUnreadMessages;
}
