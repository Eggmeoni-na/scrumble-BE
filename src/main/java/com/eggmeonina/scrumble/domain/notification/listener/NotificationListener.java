package com.eggmeonina.scrumble.domain.notification.listener;

import static com.eggmeonina.scrumble.common.util.JsonObjectConverter.*;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.eggmeonina.scrumble.domain.notification.domain.NotificationInvitedData;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationCreateRequest;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationMessage;
import com.eggmeonina.scrumble.domain.notification.service.NotificationSender;
import com.eggmeonina.scrumble.domain.notification.service.NotificationService;
import com.eggmeonina.scrumble.domain.squadmember.event.InvitedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationListener {

	private final NotificationService notificationService;
	private final NotificationSender notificationSender;

	/**
	 * 스쿼드 멤버 초대 시 이벤트 핸들링 메소드
	 * - notification 생성
	 * - notification 전송
	 *
	 * @param event
	 */
	@Async
	@TransactionalEventListener
	public void handleSquadMemberInvitedEvent(InvitedEvent event) {
		log.info("[NotificationListener - sendNotification] event = {}", event);
		try {
			// notification data 생성
			NotificationInvitedData invitedData =
				new NotificationInvitedData(event.getRecipientName(), event.getSquadName(), event.getNotificationType(), event.getSquadId());

			NotificationCreateRequest request =
				new NotificationCreateRequest
					(
						event.getRecipientId(),
						event.getSquadId(),
						event.getSquadName(),
						event.getNotificationType(),
						objectToJsonString(invitedData)
					);

			// notification DB 저장
			notificationService.createNotification(request);
			// notification 전송
			notificationSender.sendNotification(event.getRecipientId(), createNotificationMessage());
		} catch (Exception e){
			log.error("알림 이벤트 처리 실패, event = {}, e = {}", event, e.getMessage());
		}
	}

	private NotificationMessage createNotificationMessage() {
		return new NotificationMessage(true);
	}
}
