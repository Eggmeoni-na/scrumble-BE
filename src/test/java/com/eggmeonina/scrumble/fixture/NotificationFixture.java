package com.eggmeonina.scrumble.fixture;

import static com.eggmeonina.scrumble.domain.notification.domain.NotificationType.*;

import java.time.LocalDateTime;

import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;
import com.eggmeonina.scrumble.domain.notification.domain.Notification;
import com.eggmeonina.scrumble.domain.notification.domain.NotificationType;
import com.google.gson.JsonObject;

public class NotificationFixture {

	public static Notification createNotification(Member newMember, NotificationType notificationType,
		boolean readFlag) {
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
			.readFlag(readFlag).build();
	}

	public static Member createMember(String email, String name, MemberStatus memberStatus) {
		return Member.create()
			.email(email)
			.name(name)
			.memberStatus(memberStatus)
			.joinedAt(LocalDateTime.now())
			.oauthInformation(new OauthInformation("1232345", OauthType.GOOGLE))
			.build();
	}
}
