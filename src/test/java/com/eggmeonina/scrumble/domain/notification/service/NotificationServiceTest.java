package com.eggmeonina.scrumble.domain.notification.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static com.eggmeonina.scrumble.fixture.NotificationFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eggmeonina.scrumble.common.exception.ExpectedException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.notification.repository.NotificationRepository;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

	@InjectMocks
	private NotificationService notificationService;

	@Mock
	private NotificationRepository notificationRepository;

	@Test
	@DisplayName("알림이 존재하지 않을 때 읽기 여부를 변경한다_실패")
	void readNotificationWhenNotFoundNotification_fail() {
		// given
		Member newMember = createMember("test@test.com", "test", MemberStatus.JOIN, "123435345");

		given(notificationRepository.findByIdAndReadFlagNot(anyLong()))
			.willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(() -> notificationService.readNotification(newMember, 1L))
			.isInstanceOf(ExpectedException.class)
			.hasMessageContaining(NOTIFICATION_NOT_FOUND.getMessage());

	}

}
