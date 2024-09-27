package com.eggmeonina.scrumble.domain.squadmember.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SquadTest {

	@Test
	@DisplayName("스쿼드명을 변경한다_성공")
	void rename_success_doNothing() {
		// given
		Squad newSquad = Squad.create()
			.squadName("테스트 스쿼드")
			.deletedFlag(false)
			.build();

		String newName = "새로운 스쿼드";

		// when
		newSquad.rename(newName);

		// then
		assertThat(newSquad.getSquadName()).isEqualTo(newName);
	}

}
