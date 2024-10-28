package com.eggmeonina.scrumble.domain.notification.domain;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static com.eggmeonina.scrumble.domain.notification.domain.NotificationType.*;
import static com.eggmeonina.scrumble.fixture.NotificationFixture.*;
import static org.assertj.core.api.Assertions.*;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.eggmeonina.scrumble.common.exception.ExpectedException;
import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;

class NotificationTest {

	@Test
	@DisplayName("알림을 생성한다_성공")
	void constructor_success() {
		// given
		Member newMember = createMember("test@test.com", "test", MemberStatus.JOIN);

		// when
		Notification newNotification = createNotification(newMember, INVITE_REQUEST, false);

		// then
		assertThat(newNotification.getNotificationType()).isEqualTo(INVITE_REQUEST);
		assertThat(newNotification.isReadFlag()).isFalse();
	}

	@Test
	@DisplayName("수신자 없이 알림을 생성한다_실패")
	void constructorWithoutRecipient_success() {
		// when
		assertThatThrownBy(() -> Notification.create()
			.notificationType(INVITE_REQUEST)
			.readFlag(false)
			.build()
		)
			.isInstanceOf(MemberException.class)
			.hasMessageContaining(MEMBER_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("알림 타입 없이 알림을 생성한다_실패")
	void constructorWithoutNotificationType_success() {
		//given
		Member newMember = createMember("test@test.com", "test", MemberStatus.JOIN);

		// when
		assertThatThrownBy(() -> Notification.create()
			.recipient(newMember)
			.readFlag(false)
			.build()
		)
			.isInstanceOf(ExpectedException.class)
			.hasMessageContaining(NOTIFICATION_TYPE_NOT_NULL.getMessage());
	}

	@Test
	@DisplayName("알림 읽음 처리한다_정상")
	void read_success() {
		// given
		Member newMember = createMember("test@test.com", "test", MemberStatus.JOIN);
		Notification newNotification = createNotification(newMember, INVITE_REQUEST, false);

		// when
		newNotification.read();

		// then
		assertThat(newNotification.isReadFlag()).isTrue();
	}



}
