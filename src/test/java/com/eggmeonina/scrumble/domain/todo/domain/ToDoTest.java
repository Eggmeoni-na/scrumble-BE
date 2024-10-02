package com.eggmeonina.scrumble.domain.todo.domain;

import static com.eggmeonina.scrumble.fixture.SquadTodoFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;

class ToDoTest {

	@Test
	@DisplayName("투두를 삭제한다_성공")
	void delete_success() {
		// given
		Member newMember = createMember("test", "test@test.com", MemberStatus.JOIN, "123235");
		ToDo newToDo = createToDo(newMember, "모각코", TodoStatus.PENDING, false, LocalDate.now());

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
		ToDo newToDo = createToDo(newMember, "모각코", TodoStatus.PENDING, false, LocalDate.now());

		String newContents = "수정된 할 일";
		TodoStatus newToDoStatus = TodoStatus.COMPLETED;

		// when
		newToDo.update(newContents, newToDoStatus, newToDo.getTodoAt());

		// then
		assertThat(newToDo.getTodoStatus()).isEqualTo(newToDoStatus);
		assertThat(newToDo.getContents()).isEqualTo(newContents);
	}

}
