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
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadRepository;
import com.eggmeonina.scrumble.domain.todo.domain.ToDo;
import com.eggmeonina.scrumble.domain.todo.domain.ToDoType;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoCreateRequest;
import com.eggmeonina.scrumble.domain.todo.repository.SquadTodoRepository;
import com.eggmeonina.scrumble.domain.todo.repository.TodoRepository;
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

	@Test
	@DisplayName("스쿼드가 존재하지 않는 투두를 등록한다_실패")
	void createToDoWhenNotExistsSquad_fail() {
		// given
		Member newMember = createMember("memberA", "test@test.com", MemberStatus.JOIN, "12345677");
		Squad newSquad = createSquad("테스트 스쿼드", true);

		memberRepository.save(newMember);
		squadRepository.save(newSquad);

		SquadTodoCreateRequest request = new SquadTodoCreateRequest(ToDoType.DAILY, "테스트 작성하기", LocalDate.now());

		// when, then
		assertThatThrownBy(()->squadToDoFacadeService.createToDoAndSquadToDo(newSquad.getId(), newMember.getId(), request))
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

		SquadTodoCreateRequest request = new SquadTodoCreateRequest(ToDoType.DAILY, "테스트 작성하기", LocalDate.now());

		// when
		Long newTodoId = squadToDoFacadeService.createToDoAndSquadToDo(newSquad.getId(), newMember.getId(), request);
		ToDo foundToDo = todoRepository.findById(newTodoId).get();

		// then
		assertSoftly(softly -> {
			softly.assertThat(foundToDo.getContents()).isEqualTo(request.getContents());
			softly.assertThat(foundToDo.getToDoType()).isEqualTo(request.getToDoType());
			softly.assertThat(foundToDo.getTodoAt()).isEqualTo(request.getTodoAt());
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

		SquadTodoCreateRequest request = new SquadTodoCreateRequest(ToDoType.DAILY, "테스트 작성하기", LocalDate.now());

		// when, then
		assertThatThrownBy(()->squadToDoFacadeService.createToDoAndSquadToDo(newSquad.getId(), newMember.getId(), request))
			.isInstanceOf(MemberException.class)
			.hasMessageContaining(MEMBER_NOT_FOUND.getMessage());
		assertThat(todoRepository.count()).isZero();
		assertThat(squadTodoRepository.count()).isZero();
	}

}
