package com.eggmeonina.scrumble.domain.todo.domain;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static com.eggmeonina.scrumble.fixture.SquadTodoFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.common.exception.ToDoException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;

class ToDoTest {

	@Test
	@DisplayName("투두를 삭제한다_성공")
	void delete_success() {
		// given
		Member newMember = createMember("test", "test@test.com", MemberStatus.JOIN, "123235");
		ToDo newToDo = createToDo(newMember, "모각코", ToDoStatus.PENDING, false, LocalDate.now());

		// when
		newToDo.delete();

		// then
		assertThat(newToDo.isDeletedFlag()).isTrue();
	}

	@Test
	@DisplayName("투두를 수정한다_성공")
	void update_success() {
		// given
		Member newMember = createMember("test", "test@test.com", MemberStatus.JOIN, "123235");
		ToDo newToDo = createToDo(newMember, "모각코", ToDoStatus.PENDING, false, LocalDate.now());

		String newContents = "수정된 할 일";
		ToDoStatus newToDoStatus = ToDoStatus.COMPLETED;

		// when
		newToDo.update(newContents, newToDoStatus, newToDo.getToDoAt(), 1L);

		// then
		assertThat(newToDo.getToDoStatus()).isEqualTo(newToDoStatus);
		assertThat(newToDo.getContents()).isEqualTo(newContents);
	}

	@Test
	@DisplayName("투두를 생성한다_성공")
	void constructor_success() {
		// given
		Member newMember = createMember("test", "test@test.com", MemberStatus.JOIN, "123235");
		String contents = "모각코";
		ToDoType daily = ToDoType.DAILY;
		ToDoStatus pendingStatus = ToDoStatus.PENDING;

		// when
		ToDo newToDo = ToDo.create()
			.member(newMember)
			.toDoType(daily)
			.toDoAt(LocalDate.now())
			.contents(contents)
			.toDoStatus(pendingStatus)
			.build();

		// then
		assertSoftly(softly -> {
			softly.assertThat(newToDo.getContents()).isEqualTo(contents);
			softly.assertThat(newToDo.getToDoType()).isEqualTo(daily);
			softly.assertThat(newToDo.getToDoStatus()).isEqualTo(pendingStatus);
		});
	}

	@Test
	@DisplayName("회원 없이 투두를 생성한다_실패")
	void constructorWhenNotExistsMember_success() {
		// when, then
		assertThatThrownBy(()->ToDo.create()
			.toDoAt(LocalDate.now())
			.build())
			.isInstanceOf(MemberException.class)
			.hasMessageContaining(MEMBER_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("컨텐츠 없이 투두를 생성한다_실패")
	void constructorWithoutContents_success() {
		// given
		Member newMember = createMember("test", "test@test.com", MemberStatus.JOIN, "123235");
		ToDoType daily = ToDoType.DAILY;
		ToDoStatus pendingStatus = ToDoStatus.PENDING;

		// when, then
		assertThatThrownBy(()->ToDo.create()
			.member(newMember)
			.toDoType(daily)
			.toDoAt(LocalDate.now())
			.toDoStatus(pendingStatus)
			.build())
			.isInstanceOf(ToDoException.class)
			.hasMessageContaining(TODO_CONTENTS_NOT_BLANK.getMessage());
	}

	@Test
	@DisplayName("컨텐츠가 blank일 때 투두를 생성한다_실패")
	void constructorWithBlackContents_success() {
		// given
		Member newMember = createMember("test", "test@test.com", MemberStatus.JOIN, "123235");
		String contents = "";
		ToDoType daily = ToDoType.DAILY;
		ToDoStatus pendingStatus = ToDoStatus.PENDING;

		// when, then
		assertThatThrownBy(()->ToDo.create()
			.member(newMember)
			.contents(contents)
			.toDoType(daily)
			.toDoAt(LocalDate.now())
			.toDoStatus(pendingStatus)
			.build())
			.isInstanceOf(ToDoException.class)
			.hasMessageContaining(TODO_CONTENTS_NOT_BLANK.getMessage());
	}

	@Test
	@DisplayName("ToDoType이 없을 때 투두를 생성한다_실패")
	void constructorWithoutToDoType_success() {
		// given
		Member newMember = createMember("test", "test@test.com", MemberStatus.JOIN, "123235");
		String contents = "모각코";
		ToDoStatus pendingStatus = ToDoStatus.PENDING;

		// when, then
		assertThatThrownBy(()->ToDo.create()
			.member(newMember)
			.contents(contents)
			.toDoAt(LocalDate.now())
			.toDoStatus(pendingStatus)
			.build())
			.isInstanceOf(ToDoException.class)
			.hasMessageContaining(TODO_TYPE_NOT_NULL.getMessage());
	}

}
