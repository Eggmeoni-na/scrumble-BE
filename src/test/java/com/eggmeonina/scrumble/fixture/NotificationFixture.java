package com.eggmeonina.scrumble.fixture;

import java.time.LocalDateTime;

import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;
import com.eggmeonina.scrumble.domain.todo.notification.domain.Notification;
import com.eggmeonina.scrumble.domain.todo.notification.domain.NotificationType;

public class NotificationFixture {

	public static Notification createNotification(Member newMember, NotificationType notificationType,
		boolean readFlag) {
		return Notification.create()
			.recipient(newMember)
			.notificationType(notificationType)
			.readFlag(readFlag)
			.build();
	}

	public static Member createMember(String email, String test, MemberStatus memberStatus) {
		return Member.create()
			.email(email)
			.name(test)
			.memberStatus(memberStatus)
			.joinedAt(LocalDateTime.now())
			.oauthInformation(new OauthInformation("1232345", OauthType.GOOGLE))
			.build();
	}
}
