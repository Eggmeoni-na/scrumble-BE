package com.eggmeonina.scrumble.domain.membership.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eggmeonina.scrumble.domain.membership.domain.Squad;
import com.eggmeonina.scrumble.domain.membership.dto.SquadCreateRequest;
import com.eggmeonina.scrumble.domain.membership.repository.SquadRepository;

@ExtendWith(MockitoExtension.class)
class SquadServiceTest {

	@InjectMocks
	private SquadService squadService;

	@Mock
	private SquadRepository squadRepository;

	@Test
	@DisplayName("스쿼드를 생성한다")
	void createSquad_success_returnsSquadId() {
		// given
		SquadCreateRequest request = new SquadCreateRequest("테스트 스쿼드");

		Squad mockSquad = new Squad(1L, request.getSquadName(), false);

		given(squadRepository.save(any())).willReturn(mockSquad);

		// when
		Long squadId = squadService.createSquad(request);

		// then
		then(squadRepository).should(times(1)).save(any());
		assertThat(squadId).isEqualTo(mockSquad.getId());
	}

}
