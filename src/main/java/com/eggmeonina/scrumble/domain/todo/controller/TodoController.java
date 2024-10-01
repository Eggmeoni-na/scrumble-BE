package com.eggmeonina.scrumble.domain.todo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggmeonina.scrumble.common.anotation.Member;
import com.eggmeonina.scrumble.common.domain.ApiResponse;
import com.eggmeonina.scrumble.domain.auth.dto.LoginMember;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoCreateRequest;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoRequest;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoResponse;
import com.eggmeonina.scrumble.domain.todo.facade.SquadToDoFacadeService;
import com.eggmeonina.scrumble.domain.todo.service.SquadTodoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Todo 테스트", description = "todo 생성, 삭제, 조회용 API Controller")
@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

	private final SquadToDoFacadeService squadToDoFacadeService;
	private final SquadTodoService squadTodoService;

	@GetMapping("/squads/{squadId}/members/{memberId}")
	@Operation(summary = "스쿼드 투두들을 조회한다", description = "스쿼드에 속한 투두들을 조회한다.")
	public ApiResponse<List<SquadTodoResponse>> findSquadTodos(
		@Parameter(description = "스쿼드 ID") @PathVariable Long squadId,
		@Parameter(description = "스쿼드 멤버 ID") @PathVariable Long memberId,
		@ModelAttribute SquadTodoRequest request
	){
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(),squadTodoService.findSquadTodos(squadId, memberId, request));
	}

	@PostMapping("/squads/{squadId}")
	@Operation(summary = "투두를 생성한다", description = "투두를 생성한다. (스쿼드에 속한 투두)")
	public ApiResponse<Map<String, Long>> createToDo(
		@Parameter(description = "스쿼드 ID") @PathVariable Long squadId,
		@Parameter(hidden = true) @Member LoginMember member,
		@RequestBody SquadTodoCreateRequest request
	){
		Long toDoId = squadToDoFacadeService.createToDoAndSquadToDo(squadId, member.getMemberId(), request);
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), Map.of("toDoId", toDoId));
	}

}
