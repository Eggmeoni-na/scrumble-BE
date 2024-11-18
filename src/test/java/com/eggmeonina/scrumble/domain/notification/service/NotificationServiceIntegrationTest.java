package com.eggmeonina.scrumble.domain.notification.service;

import static com.eggmeonina.scrumble.fixture.NotificationFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.notification.domain.Notification;
import com.eggmeonina.scrumble.domain.notification.domain.NotificationType;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationResponse;
import com.eggmeonina.scrumble.domain.notification.dto.NotificationsRequest;
import com.eggmeonina.scrumble.domain.notification.repository.NotificationRepository;
import com.eggmeonina.scrumble.helper.IntegrationTestHelper;

class NotificationServiceIntegrationTest extends IntegrationTestHelper {

	@Autowired
	private NotificationService notificationService;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private NotificationRepository notificationRepository;

	@Test
	@DisplayName("알림 리스트를 조회한다_성공")
	void findNotifications_success() {
		// given
		Member newMember = createMember("test@test.com", "테스트유저", MemberStatus.JOIN, "1232345");
		Notification notification = createNotification(newMember, NotificationType.INVITE_REQUEST, false);

		memberRepository.save(newMember);
		notificationRepository.save(notification);

		NotificationsRequest request =
			new NotificationsRequest(LocalDateTime.now().minusDays(7), LocalDateTime.now(), 9999L, 10);

		// when
		List<NotificationResponse> notifications = notificationService.findNotifications(newMember.getId(), request);

		// then
		NotificationResponse result = notifications.get(0);
		assertSoftly(softly -> {
			softly.assertThat(notifications).hasSize(1);
			softly.assertThat(result.getNotificationId()).isEqualTo(notification.getId());
			softly.assertThat(result.getNotificationData()).isEqualTo(notification.getNotificationData());
			softly.assertThat(result.getNotificationType()).isEqualTo(notification.getNotificationType());
		});
	}

	@Nested
	@DisplayName("페이징 테스트")
	class NotificationPagingTest {
		@Test
		@DisplayName("pageSize보다 데이터가 적을 때, 저장된 개수만큼 조회한다_성공")
		void findNotificationsWhenLessThanPageSize_success() {
			// given
			Member newMember = createMember("test@test.com", "테스트유저", MemberStatus.JOIN, "1232345");
			Notification notification = createNotification(newMember, NotificationType.INVITE_REQUEST, false);

			memberRepository.save(newMember);
			notificationRepository.save(notification);

			NotificationsRequest request =
				new NotificationsRequest(LocalDateTime.now().minusDays(7), LocalDateTime.now(), 9999L, 3);

			// when
			List<NotificationResponse> notifications = notificationService.findNotifications(newMember.getId(), request);

			// then
			assertThat(notifications).hasSize(1);
		}

		@Test
		@DisplayName("pageSize보다 데이터가 많을 때, pageSize만큼 조회한다_성공")
		void findNotificationsWhenMoreThanPageSize_success() {
			// given
			Member newMember = createMember("test@test.com", "테스트유저", MemberStatus.JOIN, "1232345");
			Notification notification1 = createNotification(newMember, NotificationType.INVITE_REQUEST, false);
			Notification notification2 = createNotification(newMember, NotificationType.INVITE_REQUEST, false);
			Notification notification3 = createNotification(newMember, NotificationType.INVITE_REQUEST, false);
			Notification notification4 = createNotification(newMember, NotificationType.INVITE_ACCEPT, false);

			memberRepository.save(newMember);
			notificationRepository.saveAll(List.of(notification1, notification2, notification3, notification4));

			int pageSize = 3;
			NotificationsRequest request =
				new NotificationsRequest(LocalDateTime.now().minusDays(7), LocalDateTime.now(), 9999L, pageSize);

			// when
			List<NotificationResponse> notifications = notificationService.findNotifications(newMember.getId(), request);

			// then
			assertThat(notifications).hasSize(pageSize);
		}

	}

	@Test
	@DisplayName("알림 읽기 여부를 변경한다_성공")
	void readNotification_success() {
		// given
		Member newMember = createMember("test@test.com", "테스트유저", MemberStatus.JOIN, "1232345");
		Member anotherMember = createMember("another@test.com", "다른테스트유저", MemberStatus.JOIN, "34543534");
		Notification notification = createNotification(newMember, NotificationType.INVITE_REQUEST, false);
		Notification antoherNotification = createNotification(anotherMember, NotificationType.INVITE_REQUEST, false);

		memberRepository.saveAll(List.of(newMember, anotherMember));
		notificationRepository.saveAll(List.of(notification, antoherNotification));

		// when
		notificationService.readNotification(newMember, notification.getId());
		Notification foundNotification = notificationRepository.findById(notification.getId()).get();

		// then
		assertThat(foundNotification.isReadFlag()).isTrue();

	}

}
