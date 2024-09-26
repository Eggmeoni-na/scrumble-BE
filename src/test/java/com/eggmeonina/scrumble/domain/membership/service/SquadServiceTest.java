package com.eggmeonina.scrumble.domain.membership.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eggmeonina.scrumble.common.exception.ErrorCode;
import com.eggmeonina.scrumble.common.exception.SquadException;
import com.eggmeonina.scrumble.domain.membership.domain.Squad;
import com.eggmeonina.scrumble.domain.membership.dto.SquadCreateRequest;
import com.eggmeonina.scrumble.domain.membership.dto.SquadDetailResponse;
import com.eggmeonina.scrumble.domain.membership.dto.SquadMemberResponse;
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

	@Test
	@DisplayName("스쿼드를 조회한다_성공")
	void findSquadAndMember_success_returnSquadAndMember() {
		// given
		Long squadId = 1L;
		List<SquadMemberResponse> members = new ArrayList<>();
		SquadMemberResponse memberResponse = new SquadMemberResponse(1L, "http://-", "testA");
		members.add(memberResponse);

		SquadDetailResponse mockResponse = new SquadDetailResponse(squadId, "테스트 스쿼드", members);

		given(squadRepository.findSquadAndMembers(anyLong())).willReturn(Optional.of(mockResponse));

		// when
		SquadDetailResponse actualResponse = squadService.findSquadAndMembers(squadId);

		// then
		assertSoftly(softly -> {
			softly.assertThat(actualResponse.getSquadId()).isEqualTo(mockResponse.getSquadId());
			softly.assertThat(actualResponse.getSquadName()).isEqualTo(mockResponse.getSquadName());
			softly.assertThat(actualResponse.getSquadMembers()).hasSize(mockResponse.getSquadMembers().size());
		});
	}

	@Test
	@DisplayName("스쿼드를 조회한다_실패_예외 발생")
	void findSquadAndMember_fail_throwSquadException() {
		// given
		Long squadId = 1L;

		given(squadRepository.findSquadAndMembers(anyLong())).willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(()-> squadService.findSquadAndMembers(squadId))
			.isInstanceOf(SquadException.class)
			.hasMessageContaining(ErrorCode.SQUAD_NOT_FOUND.getMessage());
	}

}
