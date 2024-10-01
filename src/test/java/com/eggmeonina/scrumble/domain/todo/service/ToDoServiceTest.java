package com.eggmeonina.scrumble.domain.todo.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static com.eggmeonina.scrumble.fixture.SquadTodoFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.todo.domain.ToDoType;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoCreateRequest;
import com.eggmeonina.scrumble.domain.todo.repository.TodoRepository;

@ExtendWith(MockitoExtension.class)
class ToDoServiceTest {

	@InjectMocks
	private ToDoService toDoService;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private TodoRepository todoRepository;

	@Test
	@DisplayName("투두를 등록한다_정상")
	void createToDo_success() {
		// given
		Member newMember = createMember("testA", "test@test.com", MemberStatus.JOIN, "1234564");
		SquadTodoCreateRequest request = new SquadTodoCreateRequest(ToDoType.DAILY, "오늘의 할 일", LocalDate.now());

		given(memberRepository.findByIdAndMemberStatusNotJOIN(anyLong())).willReturn(Optional.ofNullable(newMember));

		// when
		toDoService.createToDo(1L, request);

		// then
		then(todoRepository).should(times(1)).save(any());
	}

	@Test
	@DisplayName("탈퇴한 회원이 투두를 등록한다_실패")
	void createToDoWhenWithdrawMember_fail() {
		// given
		SquadTodoCreateRequest request = new SquadTodoCreateRequest(ToDoType.DAILY, "오늘의 할 일", LocalDate.now());

		given(memberRepository.findByIdAndMemberStatusNotJOIN(anyLong())).willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(()-> toDoService.createToDo(1L, request))
			.isInstanceOf(MemberException.class)
			.hasMessageContaining(MEMBER_NOT_FOUND.getMessage());
	}

}
