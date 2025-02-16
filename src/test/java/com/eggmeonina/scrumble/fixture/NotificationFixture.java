package com.eggmeonina.scrumble.fixture;

import static com.eggmeonina.scrumble.domain.notification.domain.NotificationType.*;

import java.time.LocalDateTime;

import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;
import com.eggmeonina.scrumble.domain.notification.domain.Notification;
import com.eggmeonina.scrumble.domain.notification.domain.NotificationStatus;
import com.eggmeonina.scrumble.domain.notification.domain.NotificationType;
import com.google.gson.JsonObject;

public class NotificationFixture {

	public static Notification createNotification(Member newMember, NotificationType notificationType,
		boolean readFlag, NotificationStatus notificationStatus) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("userName", newMember.getName());
		jsonObject.addProperty("squadName", "테스트 스쿼드");

		if(notificationType.equals(INVITE_REQUEST)){
			jsonObject.addProperty("squadId", 1L);
		}

		return Notification.create()
			.recipient(newMember)
			.notificationData(jsonObject.toString())
			.notificationType(notificationType)
			.readFlag(readFlag)
			.notificationStatus(notificationStatus)
			.build();
	}

	public static Member createMember(String email, String name, MemberStatus memberStatus, String oauthId) {
		return Member.create()
			.email(email)
			.name(name)
			.memberStatus(memberStatus)
			.joinedAt(LocalDateTime.now())
			.oauthInformation(new OauthInformation(oauthId, OauthType.GOOGLE))
			.build();
	}

	public static Member createMemberWithMemberId(Long memberId, String email, String name, MemberStatus memberStatus,
		String oauthId){
		return new Member(memberId, email, name, null, new OauthInformation(oauthId, OauthType.GOOGLE), memberStatus,
			LocalDateTime.of(2024, 11, 11, 16, 6));
	}
}
