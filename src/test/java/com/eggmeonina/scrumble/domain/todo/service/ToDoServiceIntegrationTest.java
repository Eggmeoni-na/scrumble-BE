package com.eggmeonina.scrumble.domain.todo.service;

import static com.eggmeonina.scrumble.fixture.SquadTodoFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;

import java.time.LocalDate;
import java.util.List;

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
import com.eggmeonina.scrumble.domain.todo.dto.ToDoRequest;
import com.eggmeonina.scrumble.domain.todo.dto.ToDoResponse;
import com.eggmeonina.scrumble.domain.todo.repository.SquadTodoRepository;
import com.eggmeonina.scrumble.domain.todo.repository.TodoRepository;
import com.eggmeonina.scrumble.helper.IntegrationTestHelper;

class ToDoServiceIntegrationTest extends IntegrationTestHelper {

	@Autowired
	private ToDoService toDoService;

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private SquadTodoRepository squadTodoRepository;

	@Autowired
	private SquadRepository squadRepository;


	@Nested
	@DisplayName("나의 투두 리스트를 조회한다")
	class findToDos{
		@Test
		@DisplayName("투두가 1개일 때_성공")
		void whenExistsOne_success() {
			// given
			Member newMember = createMember("test", "test@test.com", MemberStatus.JOIN, "123234");
			Squad newSquad = createSquad("스쿼드1", false);
			ToDo newToDo = createToDo(newMember, "여행", TodoStatus.PENDING, false, LocalDate.now());
			SquadToDo newSquadToDo = createSquadTodo(newSquad, newToDo, false);

			memberRepository.save(newMember);
			squadRepository.save(newSquad);
			todoRepository.save(newToDo);
			squadTodoRepository.save(newSquadToDo);

			ToDoRequest request = new ToDoRequest(LocalDate.now(), LocalDate.now(), 9999999L);

			// when
			List<ToDoResponse> toDos = toDoService.findToDos(newMember.getId(), request);

			// then
			assertThat(toDos).hasSize(1);
		}

		@Test
		@DisplayName("투두가 N개일 때_성공")
		void whenExistToDos_success() {
			// given
			Member newMember = createMember("test", "test@test.com", MemberStatus.JOIN, "123234");
			Squad newSquad = createSquad("스쿼드1", false);
			ToDo newToDo1 = createToDo(newMember, "여행", TodoStatus.PENDING, false, LocalDate.now());
			ToDo newToDo2 = createToDo(newMember, "프로젝트", TodoStatus.PENDING, false, LocalDate.now());
			ToDo newToDo3 = createToDo(newMember, "알고리즘", TodoStatus.PENDING, false, LocalDate.now());
			SquadToDo newSquadToDo1 = createSquadTodo(newSquad, newToDo1, false);
			SquadToDo newSquadToDo2 = createSquadTodo(newSquad, newToDo2, false);
			SquadToDo newSquadToDo3 = createSquadTodo(newSquad, newToDo3, false);

			memberRepository.save(newMember);
			squadRepository.save(newSquad);
			todoRepository.saveAll(List.of(newToDo1, newToDo2, newToDo3));
			squadTodoRepository.saveAll(List.of(newSquadToDo1, newSquadToDo2, newSquadToDo3));

			ToDoRequest request = new ToDoRequest(LocalDate.now(), LocalDate.now(), 9999999L);

			// when
			List<ToDoResponse> toDos = toDoService.findToDos(newMember.getId(), request);

			// then
			assertThat(toDos).hasSize(1);
			assertThat(toDos.get(0).getToDoDetails()).hasSize(3);
		}

		@Test
		@DisplayName("스쿼드가 N개일 때_성공")
		void whenExistSquads_success() {
			// given
			Member newMember = createMember("test", "test@test.com", MemberStatus.JOIN, "123234");
			Squad newSquad1 = createSquad("스쿼드1", false);
			Squad newSquad2 = createSquad("두번째 스쿼드", false);
			ToDo newToDo1 = createToDo(newMember, "여행", TodoStatus.PENDING, false, LocalDate.now());
			ToDo newToDo2 = createToDo(newMember, "프로젝트", TodoStatus.PENDING, false, LocalDate.now());
			ToDo newToDo3 = createToDo(newMember, "알고리즘", TodoStatus.PENDING, false, LocalDate.now());
			SquadToDo newSquadToDo1 = createSquadTodo(newSquad1, newToDo1, false);
			SquadToDo newSquadToDo2 = createSquadTodo(newSquad2, newToDo2, false);
			SquadToDo newSquadToDo3 = createSquadTodo(newSquad1, newToDo3, false);

			memberRepository.save(newMember);
			squadRepository.save(newSquad1);
			squadRepository.save(newSquad2);
			todoRepository.saveAll(List.of(newToDo1, newToDo2, newToDo3));
			squadTodoRepository.saveAll(List.of(newSquadToDo1, newSquadToDo2, newSquadToDo3));

			ToDoRequest request = new ToDoRequest(LocalDate.now(), LocalDate.now(), 9999999L);

			// when
			List<ToDoResponse> toDos = toDoService.findToDos(newMember.getId(), request);

			// then
			assertThat(toDos).hasSize(1);
			assertThat(toDos.get(0).getToDoDetails()).hasSize(3);
		}

		@Test
		@DisplayName("날짜가 여러개일 때_성공")
		void whenExistDays_success() {
			// given
			Member newMember = createMember("test", "test@test.com", MemberStatus.JOIN, "123234");
			Squad newSquad1 = createSquad("스쿼드1", false);
			Squad newSquad2 = createSquad("두번째 스쿼드", false);
			ToDo newToDo1 = createToDo(newMember, "여행", TodoStatus.PENDING, false, LocalDate.now().minusDays(1));
			ToDo newToDo2 = createToDo(newMember, "프로젝트", TodoStatus.PENDING, false, LocalDate.now());
			ToDo newToDo3 = createToDo(newMember, "알고리즘", TodoStatus.PENDING, false, LocalDate.now());
			SquadToDo newSquadToDo1 = createSquadTodo(newSquad1, newToDo1, false);
			SquadToDo newSquadToDo2 = createSquadTodo(newSquad2, newToDo2, false);
			SquadToDo newSquadToDo3 = createSquadTodo(newSquad1, newToDo3, false);

			memberRepository.save(newMember);
			squadRepository.save(newSquad1);
			squadRepository.save(newSquad2);
			todoRepository.saveAll(List.of(newToDo1, newToDo2, newToDo3));
			squadTodoRepository.saveAll(List.of(newSquadToDo1, newSquadToDo2, newSquadToDo3));

			ToDoRequest request = new ToDoRequest(LocalDate.now().minusDays(1), LocalDate.now(), 9999999L);

			// when
			List<ToDoResponse> toDos = toDoService.findToDos(newMember.getId(), request);

			// then
			assertSoftly(softly -> {
				softly.assertThat(toDos).hasSize(2);
				assertThat(toDos.get(0).getToDoDetails()).hasSize(2);
				assertThat(toDos.get(1).getToDoDetails()).hasSize(1);
			});
		}
	}

}
