package com.eggmeonina.scrumble.domain.notification.service;

import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.common.exception.ErrorCode;
import com.eggmeonina.scrumble.common.exception.ExpectedException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.notification.domain.Notification;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationCreateRequest;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationResponse;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationSubScribeRequest;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationUnreadExistRequest;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationUnreadExistResponse;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationUpdateRequest;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationsRequest;
import com.eggmeonina.scrumble.domain.notification.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final MemberRepository memberRepository;
	private final NotificationRepository notificationRepository;

	// TODO : Notification 생성 중 오류가 발생한다면?
	@Transactional
	public Long createNotification(NotificationCreateRequest request) {
		Member foundMember = memberRepository.findByIdAndMemberStatusNotJOIN(request.getMemberId())
			.orElseThrow(() -> new ExpectedException(ErrorCode.MEMBER_NOT_FOUND));
		Notification newNotification = NotificationCreateRequest.to(request, foundMember);
		notificationRepository.save(newNotification);
		return newNotification.getId();
	}

	@Transactional
	public NotificationResponse updateNotification(Member member, Long notificationId,
		NotificationUpdateRequest request) {
		// 알림 조회
		Notification foundNotification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new ExpectedException(ErrorCode.NOTIFICATION_NOT_FOUND));

		// 알림 수신인 동일 여부 검증
		foundNotification.checkSameRecipient(member);

		// 읽기 여부 변경
		foundNotification.updateNotification(request.isReadFlag(), request.getNotificationStatus());

		return NotificationResponse.from(foundNotification);
	}

	@Transactional(readOnly = true)
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

	/**
	 * 최근 읽지 않은 알림 여부 확인
	 *
	 * @param request
	 * @param memberId
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean hasUnreadNotifications(NotificationSubScribeRequest request, Long memberId) {
		return notificationRepository.findAllByRecipientIdAndIdLessThanAndCreatedAtBetweenOrderByIdDesc
				(
					memberId,
					request.getLastNotificationId(),
					request.getStartDateTime(),
					request.getEndDateTime(),
					Limit.of(request.getPageSize())
				)
			.stream()
			.anyMatch(notification -> !notification.isReadFlag());
	}

	@Transactional(readOnly = true)
	public NotificationUnreadExistResponse hasUnreadNotifications
		(
			NotificationUnreadExistRequest request,
			Long memberId
		) {
		return new NotificationUnreadExistResponse
			(
				notificationRepository.findAllByRecipientIdAndCreatedAtBetweenOrderByIdDesc
						(
							memberId,
							request.getStartDateTime(),
							request.getEndDateTime()
						)
					.stream()
					.anyMatch(notification -> !notification.isReadFlag())
			);
	}
}
