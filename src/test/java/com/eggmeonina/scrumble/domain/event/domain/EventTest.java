package com.eggmeonina.scrumble.domain.event.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EventTest {

	@Test
	@DisplayName("이벤트를 발행한다_success")
	void publish_success() {
		// given
		Event newEvent = Event.create()
			.domainType("SquadMember")
			.eventType("Invite")
			.publishedAt(LocalDateTime.now())
			.publishedFlag(false)
			.eventData(null)
			.build();

		// when
		newEvent.publish();

		// then
		assertThat(newEvent.isPublishedFlag()).isTrue();
	}

}
