package com.eggmeonina.scrumble.domain.todo.service;

import static com.eggmeonina.scrumble.fixture.SquadTodoFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadRepository;
import com.eggmeonina.scrumble.domain.todo.domain.SquadToDo;
import com.eggmeonina.scrumble.domain.todo.domain.ToDo;
import com.eggmeonina.scrumble.domain.todo.domain.TodoStatus;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoRequest;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoResponse;
import com.eggmeonina.scrumble.domain.todo.repository.SquadTodoRepository;
import com.eggmeonina.scrumble.domain.todo.repository.TodoRepository;
import com.eggmeonina.scrumble.helper.IntegrationTestHelper;

class SquadTodoServiceIntegrationTest extends IntegrationTestHelper {

	@Autowired
	private SquadTodoService squadTodoService;

	@Autowired
	private SquadTodoRepository squadTodoRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private SquadRepository squadRepository;

	@Autowired
	private TodoRepository todoRepository;

	@Nested
	@DisplayName("스쿼드 투두들을 조회한다")
	class FindSquadTodosTest{
		private Squad newSquad;
		private Member newMember;
		@BeforeEach
		void setUp() {
			newMember = createMember("testA", "test@test.com", MemberStatus.JOIN, "12345567");
			newSquad = createSquad("테스트 스쿼드", false);
			ToDo newTodo1 = createToDo(newMember, "삭제된 투두1", TodoStatus.PENDING, true, LocalDate.now().minusDays(2));
			ToDo newTodo2 = createToDo(newMember, "테스트 투두2", TodoStatus.PENDING, false, LocalDate.now().minusDays(1));
			ToDo newTodo3 = createToDo(newMember, "테스트 투두3", TodoStatus.PENDING, false, LocalDate.now());
			SquadToDo newSquadToDo1 = createSquadTodo(newSquad, newTodo1, true);
			SquadToDo newSquadToDo2 = createSquadTodo(newSquad, newTodo2, false);
			SquadToDo newSquadToDo3 = createSquadTodo(newSquad, newTodo3, false);

			memberRepository.save(newMember);
			squadRepository.save(newSquad);
			todoRepository.save(newTodo1);
			todoRepository.save(newTodo2);
			todoRepository.save(newTodo3);
			squadTodoRepository.save(newSquadToDo1);
			squadTodoRepository.save(newSquadToDo2);
			squadTodoRepository.save(newSquadToDo3);
		}

		@Test
		@DisplayName("당일 데이터만 조회할 때_성공")
		void findSquadTodos_success() {
			// given
			SquadTodoRequest request = new SquadTodoRequest(LocalDate.now(), LocalDate.now());

			// when
			List<SquadTodoResponse> squadTodos = squadTodoService.findSquadTodos(newSquad.getId(), newMember.getId(),request);

			// then
			assertThat(squadTodos).hasSize(1);
		}

		@Test
		@DisplayName("투두가 삭제되었을 때_성공")
		void findSquadTodosWhenTodoDeleted_success() {
			// given
			LocalDate date = LocalDate.now().minusDays(2);
			SquadTodoRequest request = new SquadTodoRequest(date, date);

			// when
			List<SquadTodoResponse> squadTodos = squadTodoService.findSquadTodos(newSquad.getId(), newMember.getId(),request);

			// then
			assertThat(squadTodos).isEmpty();
		}

		@Test
		@DisplayName("기간으로 조회할 때_성공")
		void findSquadTodosInPeriod_success() {
			// given
			LocalDate startDate = LocalDate.now().minusDays(1);
			LocalDate endDate = LocalDate.now();
			SquadTodoRequest request = new SquadTodoRequest(startDate, endDate);

			// when
			List<SquadTodoResponse> squadTodos = squadTodoService.findSquadTodos(newSquad.getId(), newMember.getId(),request);

			// then
			assertThat(squadTodos).hasSize(2);
		}

	}



}
