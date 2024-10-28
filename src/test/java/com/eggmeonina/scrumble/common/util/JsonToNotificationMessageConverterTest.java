package com.eggmeonina.scrumble.common.util;

import static com.eggmeonina.scrumble.common.util.JsonToNotificationMessageConverter.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonToNotificationMessageConverterTest {

	@Test
	@DisplayName("메세지 변환 테스트")
	void test() {
		// given
		String jsonData = "{\"message1\": \"데이터\", \"message2\": \"두번째 데이터\"}";
		String message = "새로운 {message1}, 다음 {message2}";

		// when
		String actual = jsonToNotificationMessage(message, jsonData);

		// then
		assertThat(actual).isEqualTo("새로운 데이터, 다음 두번째 데이터");
	}

}
