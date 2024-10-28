package com.eggmeonina.scrumble.domain.notification.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationsRequest {

	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private Long lastNotificationId;
	private int pageSize;

}
