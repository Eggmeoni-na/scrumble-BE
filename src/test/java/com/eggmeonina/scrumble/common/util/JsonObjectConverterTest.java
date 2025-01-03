package com.eggmeonina.scrumble.common.util;

import static com.eggmeonina.scrumble.common.util.JsonObjectConverter.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.eggmeonina.scrumble.domain.notification.domain.NotificationInvitedData;
import com.eggmeonina.scrumble.domain.notification.domain.NotificationType;

class JsonObjectConverterTest {

	@Test
	@DisplayName("json 타입을 Notification 메세지로 변환한다_성공")
	void jsonToNotificationMessage_success() {
		// given
		String jsonData = "{\"message1\": \"데이터\", \"message2\": \"두번째 데이터\"}";
		String message = "새로운 {message1}, 다음 {message2}";

		// when
		String actual = jsonToNotificationMessage(message, jsonData);

		// then
		assertThat(actual).isEqualTo("새로운 데이터, 다음 두번째 데이터");
	}

	@Test
	@DisplayName("오브젝트를 json 타입의 String으로 변환한다_성공")
	void objectToJsonString_success() {
		// given
		String 테스트유저 = "테스트유저";
		String 테스트스쿼드 = "테스트스쿼드";
		long squadId = 1L;
		String jsonData = String.format("{\"memberName\":\"%s\",\"squadName\":\"%s\",\"notificationType\":\"%s\",\"squadId\":%s}",
			테스트유저, 테스트스쿼드, NotificationType.INVITE_REQUEST.name(), squadId);
		NotificationInvitedData data = new NotificationInvitedData(
			테스트유저, 테스트스쿼드, NotificationType.INVITE_REQUEST, squadId);
		// when
		String actual = objectToJsonString(data);

		// then
		assertThat(actual).isEqualTo(jsonData);
	}

}
