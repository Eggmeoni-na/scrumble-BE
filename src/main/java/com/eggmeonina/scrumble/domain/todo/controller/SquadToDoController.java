package com.eggmeonina.scrumble.domain.todo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggmeonina.scrumble.common.anotation.LoginMember;
import com.eggmeonina.scrumble.common.domain.ApiResponse;
import com.eggmeonina.scrumble.domain.auth.dto.MemberInfo;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoRequest;
import com.eggmeonina.scrumble.domain.todo.dto.SquadTodoResponse;
import com.eggmeonina.scrumble.domain.todo.facade.SquadToDoFacadeService;
import com.eggmeonina.scrumble.domain.todo.service.SquadTodoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Squad Todo 테스트", description = "스쿼드에 속한 투두 생성, 삭제, 조회용 API Controller")
@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class SquadToDoController {

	private final SquadToDoFacadeService squadToDoFacadeService;
	private final SquadTodoService squadTodoService;

	@GetMapping("/squads/{squadId}/members/{memberId}")
	@Operation(summary = "스쿼드 투두들을 조회한다", description = "스쿼드에 속한 투두들을 조회한다.")
	public ApiResponse<List<SquadTodoResponse>> findSquadTodos(
		@Parameter(description = "스쿼드 ID") @PathVariable Long squadId,
		@Parameter(description = "스쿼드 멤버 ID") @PathVariable Long memberId,
		@ModelAttribute SquadTodoRequest request
	) {
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(),
			squadTodoService.findSquadTodos(squadId, memberId, request));
	}

	@DeleteMapping("/{toDoId}/squads/{squadId}")
	@Operation(summary = "스쿼드에 속한 투두를 삭제한다", description = "본인의 스쿼드에 속한 투두를 삭제한다.")
	public ApiResponse<Map<String, Long>> deleteToDo(
		@Parameter(description = "투두 ID") @PathVariable Long toDoId,
		@Parameter(description = "스쿼드 ID") @PathVariable Long squadId,
		@Parameter(hidden = true) @LoginMember MemberInfo loginMember
	) {
		squadToDoFacadeService.deleteToDoAndSquadToDo(squadId, toDoId, loginMember.getMemberId());
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), Map.of("toDoId", toDoId));
	}

}
