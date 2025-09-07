package com.eggmeonina.scrumble.domain.todo.facade;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;
import static com.eggmeonina.scrumble.fixture.SquadTodoFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.common.exception.SquadException;
import com.eggmeonina.scrumble.common.exception.ToDoException;
import com.eggmeonina.scrumble.domain.category.repository.CategoryRepository;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadRepository;
import com.eggmeonina.scrumble.domain.todo.domain.SquadToDo;
import com.eggmeonina.scrumble.domain.todo.domain.ToDo;
import com.eggmeonina.scrumble.domain.todo.domain.ToDoStatus;
import com.eggmeonina.scrumble.domain.todo.domain.ToDoType;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoCreateRequest;
import com.eggmeonina.scrumble.domain.todo.dto.ToDoCommandResponse;
import com.eggmeonina.scrumble.domain.todo.repository.SquadTodoRepository;
import com.eggmeonina.scrumble.domain.todo.repository.TodoRepository;
import com.eggmeonina.scrumble.fixture.CategoryFixture;
import com.eggmeonina.scrumble.helper.IntegrationTestHelper;

class SquadToDoFacadeServiceIntegrationTest extends IntegrationTestHelper {

	@Autowired
	private SquadToDoFacadeService squadToDoFacadeService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private SquadRepository squadRepository;

	@Autowired
	private SquadTodoRepository squadTodoRepository;

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	@DisplayName("스쿼드가 존재하지 않는 투두를 등록한다_실패")
	void createToDoWhenNotExistsSquad_fail() {
		// given
		Member newMember = createMember("memberA", "test@test.com", MemberStatus.JOIN, "12345677");
		Squad newSquad = createSquad("테스트 스쿼드", true);

		memberRepository.save(newMember);
		squadRepository.save(newSquad);

		var newCategory = CategoryFixture.createCategory(newMember.getId());
		categoryRepository.save(newCategory);

		SquadTodoCreateRequest request = new SquadTodoCreateRequest(ToDoType.DAILY, "테스트 작성하기", LocalDate.now(), newCategory.getId());

		// when, then
		assertThatThrownBy(
			() -> squadToDoFacadeService.createToDoAndSquadToDo(newSquad.getId(), newMember.getId(), request))
			.isInstanceOf(SquadException.class)
			.hasMessageContaining(SQUAD_NOT_FOUND.getMessage());
		assertThat(todoRepository.count()).isZero();
		assertThat(squadTodoRepository.count()).isZero();
	}

	@Test
	@DisplayName("투두와 스쿼드 투두를 등록한다_정상")
	void createToDo_success() {
		// given
		Member newMember = createMember("memberA", "test@test.com", MemberStatus.JOIN, "12345677");
		Squad newSquad = createSquad("테스트 스쿼드", false);

		memberRepository.save(newMember);
		squadRepository.save(newSquad);

		var newCategory = CategoryFixture.createCategory(newMember.getId());
		categoryRepository.save(newCategory);

		SquadTodoCreateRequest request = new SquadTodoCreateRequest(ToDoType.DAILY, "테스트 작성하기", LocalDate.now(), newCategory.getId());

		// when
		ToDoCommandResponse response = squadToDoFacadeService.createToDoAndSquadToDo(newSquad.getId(),
			newMember.getId(), request);
		ToDo foundToDo = todoRepository.findById(response.getToDoId()).get();

		// then
		assertSoftly(softly -> {
			softly.assertThat(foundToDo.getContents()).isEqualTo(request.getContents());
			softly.assertThat(foundToDo.getToDoType()).isEqualTo(request.getToDoType());
			softly.assertThat(foundToDo.getToDoAt()).isEqualTo(request.getToDoAt());
		});

	}

	@Test
	@DisplayName("탈퇴한 회원의 투두를 등록한다_실패")
	void createToDoWhenNotExistsMember_fail() {
		// given
		Member newMember = createMember("memberA", "test@test.com", MemberStatus.WITHDRAW, "12345677");
		Squad newSquad = createSquad("테스트 스쿼드", false);

		memberRepository.save(newMember);
		squadRepository.save(newSquad);

		var newCategory = CategoryFixture.createCategory(newMember.getId());
		categoryRepository.save(newCategory);

		SquadTodoCreateRequest request = new SquadTodoCreateRequest(ToDoType.DAILY, "테스트 작성하기", LocalDate.now(), newCategory.getId());

		// when, then
		assertThatThrownBy(
			() -> squadToDoFacadeService.createToDoAndSquadToDo(newSquad.getId(), newMember.getId(), request))
			.isInstanceOf(MemberException.class)
			.hasMessageContaining(MEMBER_NOT_FOUND.getMessage());
		assertThat(todoRepository.count()).isZero();
		assertThat(squadTodoRepository.count()).isZero();
	}

	@Test
	@DisplayName("스쿼드 투두와 투두를 삭제한다_성공")
	void deleteToDoAndSquadToDo_success() {
		// given
		Member newMember = createMember("memberA", "test@test.com", MemberStatus.WITHDRAW, "12345677");
		Squad newSquad = createSquad("테스트 스쿼드", false);
		ToDo newToDo = createToDo(newMember, "모각코", ToDoStatus.PENDING, false, LocalDate.now());
		SquadToDo newSquadToDo = createSquadTodo(newSquad, newToDo, false);

		memberRepository.save(newMember);
		squadRepository.save(newSquad);
		todoRepository.save(newToDo);
		squadTodoRepository.save(newSquadToDo);

		// when
		squadToDoFacadeService.deleteToDoAndSquadToDo(newSquad.getId(), newToDo.getId(), newMember.getId());
		ToDo deletedToDo = todoRepository.findById(newToDo.getId()).get();
		SquadToDo deletedSquadToDo = squadTodoRepository.findById(newSquadToDo.getId()).get();

		// then
		assertSoftly(softly -> {
			softly.assertThat(deletedToDo.isDeletedFlag()).isTrue();
			softly.assertThat(deletedSquadToDo.isDeletedFlag()).isTrue();
		});
	}

	@Test
	@DisplayName("존재하지 않는 투두를 삭제한다_실패")
	void deleteToDoAndSquadToDoWhenDeletedToDo_fail() {
		// given
		Member newMember = createMember("memberA", "test@test.com", MemberStatus.WITHDRAW, "12345677");
		Squad newSquad = createSquad("테스트 스쿼드", false);
		ToDo newToDo = createToDo(newMember, "모각코", ToDoStatus.PENDING, true, LocalDate.now());
		SquadToDo newSquadToDo = createSquadTodo(newSquad, newToDo, false);

		memberRepository.save(newMember);
		squadRepository.save(newSquad);
		todoRepository.save(newToDo);
		squadTodoRepository.save(newSquadToDo);

		SquadToDo deletedSquadToDo = squadTodoRepository.findById(newSquadToDo.getId()).get();

		// when, then
		assertSoftly(softly -> {
			softly.assertThatThrownBy(
					() -> squadToDoFacadeService.deleteToDoAndSquadToDo(newSquad.getId(), newToDo.getId(), newMember.getId()))
				.isInstanceOf(ToDoException.class)
				.hasMessageContaining(TODO_NOT_FOUND.getMessage());
			softly.assertThat(deletedSquadToDo.isDeletedFlag()).isFalse();
		});
	}

}
