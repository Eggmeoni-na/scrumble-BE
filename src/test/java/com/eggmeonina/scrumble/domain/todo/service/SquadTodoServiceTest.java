package com.eggmeonina.scrumble.domain.todo.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static com.eggmeonina.scrumble.fixture.SquadTodoFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eggmeonina.scrumble.common.exception.ToDoException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.todo.domain.SquadToDo;
import com.eggmeonina.scrumble.domain.todo.domain.ToDo;
import com.eggmeonina.scrumble.domain.todo.domain.ToDoStatus;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoCountRequest;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoCountResponse;
import com.eggmeonina.scrumble.domain.todo.repository.SquadTodoRepository;

@ExtendWith(MockitoExtension.class)
class SquadTodoServiceTest {

	@InjectMocks
	private SquadTodoService squadTodoService;

	@Mock
	private SquadTodoRepository squadTodoRepository;

	@Test
	@DisplayName("스쿼드 투두를 삭제한다_성공")
	void deleteSquadToDo_success() {
		// given
		Member newMember = createMember("testA", "test@test.com", MemberStatus.JOIN, "!@335435");
		Squad newSquad = createSquad("테스트 스쿼드", false);
		ToDo newToDo = createToDo(newMember, "contents", ToDoStatus.PENDING, false, LocalDate.now());
		SquadToDo newSquadToDo = createSquadTodo(newSquad, newToDo, false);
		given(squadTodoRepository.findByToDoIdAndSquadIdAndDeletedFlagNot(anyLong(), anyLong()))
			.willReturn(Optional.ofNullable(newSquadToDo));

		// when
		squadTodoService.deleteSquadToDo(1L, 1L);

		// then
		assertThat(newSquadToDo.isDeletedFlag()).isTrue();
	}

	@Test
	@DisplayName("삭제된 스쿼드 투두를 삭제한다_실패")
	void deleteSquadToDoWhenDeletedSquadToDo_fail() {
		// given
		given(squadTodoRepository.findByToDoIdAndSquadIdAndDeletedFlagNot(anyLong(), anyLong()))
			.willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(()-> squadTodoService.deleteSquadToDo(1L, 1L))
			.isInstanceOf(ToDoException.class)
			.hasMessageContaining(SQUAD_TODO_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("특정 유저의 일자별 투두 완료 개수를 조회한다_성공")
	void countCompletedSquadTodos_success() {
		// given
		Long memberId = 1L;
		Long squadMemberId = 1L;
		LocalDate startDate = LocalDate.of(2024, 1, 1);
		LocalDate endDate = LocalDate.of(2024, 1, 7);
		SquadTodoCountRequest request = new SquadTodoCountRequest(startDate, endDate, "Daily");

		List<SquadTodoCountResponse> expectedResponse = Arrays.asList(
			new SquadTodoCountResponse(LocalDate.of(2024, 1, 1), 5L, 3L),
			new SquadTodoCountResponse(LocalDate.of(2024, 1, 2), 4L, 2L),
			new SquadTodoCountResponse(LocalDate.of(2024, 1, 3), 6L, 4L)
		);

		given(squadTodoRepository.getSquadTodoCountSummary(memberId, squadMemberId, request))
			.willReturn(expectedResponse);

		// when
		List<SquadTodoCountResponse> result = squadTodoService.getSquadTodoCountSummary(memberId, squadMemberId, request);

		// then
		assertThat(result).hasSize(3);
		assertThat(result.get(0).getTodoAt()).isEqualTo(LocalDate.of(2024, 1, 1));
		assertThat(result.get(0).getTotalCount()).isEqualTo(5L);
		assertThat(result.get(0).getCompletedCount()).isEqualTo(3L);

		assertThat(result.get(1).getTodoAt()).isEqualTo(LocalDate.of(2024, 1, 2));
		assertThat(result.get(1).getTotalCount()).isEqualTo(4L);
		assertThat(result.get(1).getCompletedCount()).isEqualTo(2L);

		assertThat(result.get(2).getTodoAt()).isEqualTo(LocalDate.of(2024, 1, 3));
		assertThat(result.get(2).getTotalCount()).isEqualTo(6L);
		assertThat(result.get(2).getCompletedCount()).isEqualTo(4L);

		verify(squadTodoRepository, times(1)).getSquadTodoCountSummary(memberId, squadMemberId, request);
	}

	@Test
	@DisplayName("특정 유저의 일자별 투두 완료 개수를 조회한다_빈 결과")
	void countCompletedSquadTodos_emptyResult() {
		// given
		Long memberId = 1L;
		Long squadMemberId = 1L;
		LocalDate startDate = LocalDate.of(2024, 1, 1);
		LocalDate endDate = LocalDate.of(2024, 1, 7);
		SquadTodoCountRequest request = new SquadTodoCountRequest(startDate, endDate, "Daily");

		given(squadTodoRepository.getSquadTodoCountSummary(memberId, squadMemberId, request))
			.willReturn(List.of());

		// when
		List<SquadTodoCountResponse> result = squadTodoService.getSquadTodoCountSummary(memberId, squadMemberId, request);

		// then
		assertThat(result).isEmpty();
		verify(squadTodoRepository, times(1)).getSquadTodoCountSummary(memberId, squadMemberId, request);
	}

}
