package com.eggmeonina.scrumble.domain.todo.domain;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static com.eggmeonina.scrumble.fixture.SquadTodoFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.eggmeonina.scrumble.common.exception.SquadToDoException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;

class SquadToDoTest {

	@Test
	@DisplayName("스쿼드 투두를 생성한다_성공")
	void newSquadToDo_success() {
		// given
		Member newMember = createMember("testA", "test@test.com", MemberStatus.JOIN, "!23245");
		Squad newSquad = createSquad("테스트 스쿼드", false);
		ToDo newToDo = createToDo(newMember, "모각코", ToDoStatus.PENDING, false, LocalDate.now());

		// when
		SquadToDo newSquadToDo = SquadToDo.create()
			.squad(newSquad)
			.toDo(newToDo)
			.deletedFlag(false)
			.build();

		Squad actualSquad = newSquadToDo.getSquad();
		ToDo actualToDo = newSquadToDo.getToDo();

		// then
		assertSoftly(softly -> {
			softly.assertThat(actualSquad.getSquadName()).isEqualTo(newSquad.getSquadName());
			softly.assertThat(actualToDo.getToDoType()).isEqualTo(newToDo.getToDoType());
			softly.assertThat(actualToDo.getContents()).isEqualTo(newToDo.getContents());
			softly.assertThat(actualToDo.getToDoAt()).isEqualTo(newToDo.getToDoAt());
		});
	}

	@Test
	@DisplayName("스쿼드 없이 스쿼드 투두를 생성한다_실패")
	void newSquadToDoWhenNotExistsSquad_fail() {
		// given
		Member newMember = createMember("testA", "test@test.com", MemberStatus.JOIN, "!23245");
		ToDo newToDo = createToDo(newMember, "모각코", ToDoStatus.PENDING, false, LocalDate.now());

		// when, then
		assertThatThrownBy(() -> SquadToDo.create()
			.toDo(newToDo)
			.deletedFlag(false)
			.build())
			.isInstanceOf(SquadToDoException.class)
			.hasMessageContaining(SQUAD_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("투두 없이 스쿼드 투두를 생성한다_실패")
	void newSquadToDoWhenNotExistsToDo_fail() {
		// given
		Squad newSquad = createSquad("테스트 스쿼드", false);

		// when, then
		assertThatThrownBy(() -> SquadToDo.create()
			.squad(newSquad)
			.deletedFlag(false)
			.build())
			.isInstanceOf(SquadToDoException.class)
			.hasMessageContaining(TODO_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("스쿼드를 삭제한다_성공")
	void delete_success() {
		// given
		Member newMember = createMember("testA", "test@test.com", MemberStatus.JOIN, "!23245");
		Squad newSquad = createSquad("테스트 스쿼드", false);
		ToDo newToDo = createToDo(newMember, "모각코", ToDoStatus.PENDING, false, LocalDate.now());
		SquadToDo newSquadToDo = createSquadTodo(newSquad, newToDo, false);

		// when
		newSquadToDo.delete();

		// then
		assertThat(newSquadToDo.isDeletedFlag()).isTrue();
	}

}
