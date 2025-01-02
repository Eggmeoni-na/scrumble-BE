package com.eggmeonina.scrumble.domain.notification.listener;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eggmeonina.scrumble.common.exception.ErrorCode;
import com.eggmeonina.scrumble.common.exception.ExpectedException;
import com.eggmeonina.scrumble.domain.notification.domain.NotificationType;
import com.eggmeonina.scrumble.domain.notification.service.NotificationSender;
import com.eggmeonina.scrumble.domain.notification.service.NotificationService;
import com.eggmeonina.scrumble.domain.squadmember.event.InvitedEvent;

@ExtendWith(MockitoExtension.class)
class NotificationListenerTest {

	@InjectMocks
	private NotificationListener notificationListener;
	@Mock
	private NotificationService notificationService;

	@Mock
	private NotificationSender notificationSender;

	@Test
	@DisplayName("InvitedEvent가 발생했을 때 알림을 생성하고 전송한다_성공")
	void handleSquadMemberInvitedEvent_success() {
		// given
		InvitedEvent event = new InvitedEvent(2L, "테스트유저", 1L, "테스트 스쿼드", NotificationType.INVITE_REQUEST);

		given(notificationService.createNotification(any()))
			.willReturn(1L);
		doNothing().when(notificationSender).sendNotification(anyLong(), any());

		// when
		notificationListener.handleSquadMemberInvitedEvent(event);

		// then
		then(notificationService).should(times(1)).createNotification(any());
		then(notificationSender).should(times(1)).sendNotification(anyLong(), any());
	}

	@Test
	@DisplayName("InvitedEvent가 발생했을 때 알림 생성 중 오류가 발생한다_실패")
	void handleSquadMemberInvitedEvent_fail() {
		// given
		InvitedEvent event = new InvitedEvent(2L, "테스트유저", 1L, "테스트 스쿼드", NotificationType.INVITE_REQUEST);

		doThrow(new ExpectedException(ErrorCode.MEMBER_NOT_FOUND)).when(notificationService).createNotification(any());

		// when
		notificationListener.handleSquadMemberInvitedEvent(event);

		// then
		then(notificationService).should(times(1)).createNotification(any());
		then(notificationSender).should(never()).sendNotification(anyLong(), any());
	}

}
