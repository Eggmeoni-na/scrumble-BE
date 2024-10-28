package com.eggmeonina.scrumble.domain.notification.service;

import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import com.eggmeonina.scrumble.domain.notification.domain.Notification;
import com.eggmeonina.scrumble.domain.notification.domain.NotificationType;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationsRequest;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationResponse;
import com.eggmeonina.scrumble.domain.notification.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
	private final NotificationRepository notificationRepository;

	public Long createNotification(Long memberId, NotificationType notificationType) {
		return null;
	}

	public List<NotificationResponse> findNotifications(Long memberId, NotificationsRequest request) {
		List<Notification> notifications
			= notificationRepository.findAllByRecipientIdAndIdLessThanAndCreatedAtBetweenOrderByIdDesc
			(
				memberId,
				request.getLastNotificationId(),
				request.getStartDateTime(),
				request.getEndDateTime(),
				Limit.of(request.getPageSize())
			);
		return notifications.stream()
			.map(NotificationResponse::from)
			.toList();
	}
}
