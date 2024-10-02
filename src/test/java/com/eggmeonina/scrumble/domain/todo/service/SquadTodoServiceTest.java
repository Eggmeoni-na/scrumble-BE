package com.eggmeonina.scrumble.domain.todo.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static com.eggmeonina.scrumble.fixture.SquadTodoFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
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
import com.eggmeonina.scrumble.domain.todo.domain.TodoStatus;
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
		ToDo newToDo = createToDo(newMember, "contents", TodoStatus.PENDING, false, LocalDate.now());
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

}
