package com.eggmeonina.scrumble.domain.notification.service;

import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.common.exception.ErrorCode;
import com.eggmeonina.scrumble.common.exception.ExpectedException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
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

	@Transactional
	public NotificationResponse readNotification(Member member, Long notificationId){
		// 알림 조회
		Notification foundNotification = notificationRepository.findByIdAndReadFlagNot(notificationId)
			.orElseThrow(() -> new ExpectedException(ErrorCode.NOTIFICATION_NOT_FOUND));

		// 알림 수신인 동일 여부 검증
		foundNotification.checkSameRecipient(member);

		// 읽기 여부 변경
		foundNotification.read();

		return NotificationResponse.from(foundNotification);
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
