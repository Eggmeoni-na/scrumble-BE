package com.eggmeonina.scrumble.domain.todo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggmeonina.scrumble.common.anotation.LoginMember;
import com.eggmeonina.scrumble.common.domain.ApiResponse;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoCreateRequest;
import com.eggmeonina.scrumble.domain.todo.dto.ToDoCommandResponse;
import com.eggmeonina.scrumble.domain.todo.dto.ToDoRequest;
import com.eggmeonina.scrumble.domain.todo.dto.ToDoResponse;
import com.eggmeonina.scrumble.domain.todo.dto.ToDoUpdateRequest;
import com.eggmeonina.scrumble.domain.todo.facade.SquadToDoFacadeService;
import com.eggmeonina.scrumble.domain.todo.service.ToDoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Todo 테스트", description = "todo 생성, 삭제, 조회용 API Controller")
@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

	private final SquadToDoFacadeService squadToDoFacadeService;
	private final ToDoService toDoService;

	@PostMapping("/squads/{squadId}")
	@Operation(summary = "투두를 생성한다", description = "투두를 생성한다. (스쿼드에 속한 투두)")
	public ApiResponse<ToDoCommandResponse> createToDo(
		@Parameter(description = "스쿼드 ID") @PathVariable Long squadId,
		@Parameter(hidden = true) @LoginMember Member member,
		@RequestBody SquadTodoCreateRequest request
	) {
		ToDoCommandResponse response
			= squadToDoFacadeService.createToDoAndSquadToDo(squadId, member.getId(), request);
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), response);
	}

	@PutMapping("/{toDoId}")
	@Operation(summary = "투두를 수정한다", description = "본인이 작성한 투두를 수정한다.")
	public ApiResponse<ToDoCommandResponse> updateToDo(
		@Parameter(description = "투두 ID") @PathVariable Long toDoId,
		@Parameter(hidden = true) @LoginMember Member member,
		@RequestBody ToDoUpdateRequest request
	) {
		ToDoCommandResponse response = toDoService.updateToDo(member.getId(), toDoId, request);
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), response);
	}

	@GetMapping("/me")
	@Operation(summary = "나의 투두들을 조회한다", description = "내가 작성한 투두들을 조회한다.")
	public ApiResponse<List<ToDoResponse>> getToDos(
		@Valid @ModelAttribute ToDoRequest request,
		@Parameter(hidden = true) @LoginMember Member member
	) {
		List<ToDoResponse> toDos = toDoService.findToDos(member.getId(), request);
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), toDos);
	}

}
