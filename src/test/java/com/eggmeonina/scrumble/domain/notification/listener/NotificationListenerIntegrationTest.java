package com.eggmeonina.scrumble.domain.notification.listener;

import static java.time.LocalDateTime.*;
import static org.assertj.core.api.SoftAssertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.concurrent.Executor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;

import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.notification.domain.Notification;
import com.eggmeonina.scrumble.domain.notification.domain.NotificationType;
import com.eggmeonina.scrumble.domain.notification.repository.NotificationRepository;
import com.eggmeonina.scrumble.domain.notification.service.NotificationSender;
import com.eggmeonina.scrumble.domain.squadmember.event.InvitedEvent;
import com.eggmeonina.scrumble.helper.IntegrationTestHelper;

class NotificationListenerIntegrationTest extends IntegrationTestHelper {

	@Autowired
	private NotificationListener notificationListener;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@MockBean
	private NotificationSender notificationSender;

	@TestConfiguration
	static class TestConfig {
		/**
		 * 이벤트 리스너가 비동기로 실행되어, 테스트 이후에 메소드가 실행될 수 있어
		 * executor를 동기로 변경한다.
		 * @return
		 */
		@Bean
		@Primary
		public Executor executor() {
			return new SyncTaskExecutor();
		}
	}

	@Test
	@DisplayName("이벤트가 발행되면 알림을 생성한다_성공")
	void handleSquadMemberInvitedEvent_success() {
		// given
		Member 테스트유저 = Member.create()
			.email("test@test.com")
			.name("테스트유저")
			.oauthInformation(new OauthInformation("123234", OauthType.GOOGLE))
			.memberStatus(MemberStatus.JOIN)
			.joinedAt(now())
			.build();

		memberRepository.save(테스트유저);

		InvitedEvent event = new InvitedEvent(테스트유저.getId(), 테스트유저.getName(), 2L, "테스트스쿼드", NotificationType.INVITE_REQUEST);
		doNothing().when(notificationSender).sendNotification(anyLong(), any());

		// when
		notificationListener.handleSquadMemberInvitedEvent(event);
		List<Notification> notifications = notificationRepository.findAll();

		// then
		assertSoftly(softly -> {
			softly.assertThat(notifications).hasSize(1);
			softly.assertThatCode(() -> verify(notificationSender, times(1)).sendNotification(anyLong(), any()))
				.doesNotThrowAnyException();
		});

	}

	@Test
	@DisplayName("이벤트가 발생할 때 알림 생성 중에 예외가 발생한다_실패")
	void handleSquadMemberInvitedEventWhenNotFoundMember_fail() {
		// given
		InvitedEvent event = new InvitedEvent(1L, "테스트유저", 2L, "테스트스쿼드", NotificationType.INVITE_REQUEST);
		doNothing().when(notificationSender).sendNotification(anyLong(), any());

		// when
		notificationListener.handleSquadMemberInvitedEvent(event);
		List<Notification> notifications = notificationRepository.findAll();

		// then
		assertSoftly(softly -> {
			// notification를 생성하지 않는다
			softly.assertThat(notifications).hasSize(0);
			// notificationSender를 호출하지 않는다
			softly.assertThatCode(() -> verify(notificationSender, never()).sendNotification(anyLong(), any()))
				.doesNotThrowAnyException();
		});
	}

}
