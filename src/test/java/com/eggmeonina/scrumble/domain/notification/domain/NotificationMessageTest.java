package com.eggmeonina.scrumble.domain.notification.domain;

import static com.eggmeonina.scrumble.common.util.JsonObjectConverter.*;
import static com.eggmeonina.scrumble.domain.notification.domain.NotificationMessage.*;
import static com.eggmeonina.scrumble.domain.notification.domain.NotificationType.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

class NotificationMessageTest {

	@Test
	@DisplayName("초대 요청 메세지를 가져온다_성공")
	void getInviteRequestMessage_success() {
		// given
		JsonObject json = new JsonObject();
		json.addProperty("userName", "테스트 유저");
		json.addProperty("squadName", "테스트 스쿼드");

		// when
		String message = getNotificationMessage(INVITE_REQUEST, json.toString());

		// then
		assertThat(message).isEqualTo(jsonToNotificationMessage(INVITE_REQUEST_MESSAGE.getMessage(), json.toString()));
	}

	@Test
	@DisplayName("초대 수락 메세지를 가져온다_성공")
	void getInviteAcceptMessage_success2() {
		// given
		JsonObject json = new JsonObject();
		json.addProperty("userName", "테스트 유저");
		json.addProperty("squadName", "테스트 스쿼드");

		// when
		String message = getNotificationMessage(INVITE_ACCEPT, json.toString());

		// then
		assertThat(message).isEqualTo(jsonToNotificationMessage(INVITE_ACCEPT_MESSAGE.getMessage(), json.toString()));
	}

}
