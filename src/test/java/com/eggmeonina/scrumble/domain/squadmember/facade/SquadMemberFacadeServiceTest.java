package com.eggmeonina.scrumble.domain.squadmember.facade;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eggmeonina.scrumble.domain.squadmember.dto.SquadCreateRequest;
import com.eggmeonina.scrumble.domain.squadmember.service.SquadMemberService;
import com.eggmeonina.scrumble.domain.squadmember.service.SquadService;

@ExtendWith(MockitoExtension.class)
class SquadMemberFacadeServiceTest {

	@InjectMocks
	private SquadMemberFacadeService squadMemberFacadeService;

	@Mock
	private SquadMemberService squadMemberService;

	@Mock
	private SquadService squadService;


	@Test
	@DisplayName("스쿼드를 생성하면 스쿼드번호를 반환한다")
	void createSquad_success_returnsSquadId() {
		// given
		Long squadId = 1L;
		Long memberId = 1L;
		Long membershipId = 1L;
		SquadCreateRequest request = new SquadCreateRequest("테스트 스쿼드");

		given(squadService.createSquad(any())).willReturn(squadId);
		given(squadMemberService.createMembership(anyLong(), anyLong())).willReturn(membershipId);

		// when
		Long newSquadId = squadMemberFacadeService.createSquad(memberId, request);

		// then
		assertThat(newSquadId).isEqualTo(squadId);
	}

}
