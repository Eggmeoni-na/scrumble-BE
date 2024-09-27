package com.eggmeonina.scrumble.domain.squadmember.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggmeonina.scrumble.common.anotation.Member;
import com.eggmeonina.scrumble.common.domain.ApiResponse;
import com.eggmeonina.scrumble.domain.auth.dto.LoginMember;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadCreateRequest;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadDetailResponse;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadResponse;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadUpdateRequest;
import com.eggmeonina.scrumble.domain.squadmember.facade.SquadMemberFacadeService;
import com.eggmeonina.scrumble.domain.squadmember.service.SquadMemberService;
import com.eggmeonina.scrumble.domain.squadmember.service.SquadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Squad 테스트", description = "squad 생성, 삭제, 조회용 API Controller")
@RestController
@RequestMapping("/api/squads")
@RequiredArgsConstructor
public class SquadController {

	private final SquadMemberFacadeService squadMemberFacadeService;
	private final SquadMemberService squadMemberService;
	private final SquadService squadService;

	@PostMapping
	@Operation(summary = "스쿼드를 생성한다", description = "스쿼드를 생성하면서 스쿼드장을 같이 생성한다.")
	public ApiResponse<Map<String, Long>> createSquad(
		@Parameter(hidden = true) @Member LoginMember member,
		@RequestBody @Valid SquadCreateRequest request
	) {
		Long squadId = squadMemberFacadeService.createSquad(member.getMemberId(), request);
		return ApiResponse.createSuccessResponse(HttpStatus.CREATED.value(), Map.of("squadId", squadId));
	}

	@GetMapping
	@Operation(summary = "나의 스쿼드를 조회한다", description = "내가 속해있는 스쿼드들을 조회한다.")
	public ApiResponse<List<SquadResponse>> findSquads(
		@Parameter(hidden = true) @Member LoginMember member
	) {
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), squadMemberService.findBySquads(member.getMemberId()));
	}

	@GetMapping("/{squadId}")
	@Operation(summary = "스쿼드를 상세 조회한다", description = "스쿼드와 속한 멤버들을 조회한다.")
	public ApiResponse<SquadDetailResponse> findSquad(@PathVariable Long squadId){
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), squadService.findSquadAndMembers(squadId));
	}

	@PutMapping("/{squadId}")
	@Operation(summary = "스쿼드명을 수정한다", description = "스쿼드 리더만 스쿼드명을 수정한다.")
	public ApiResponse<Void> updateSquad(
		@Parameter(hidden = true) @Member LoginMember member,
		@PathVariable Long squadId,
		@RequestBody @Valid SquadUpdateRequest request
	){
		squadMemberService.updateSquad(member.getMemberId(), squadId, request);
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}

	@PutMapping("/{squadId}/members/{memberId}")
	@Operation(summary = "스쿼드 리더를 위임한다", description = "스쿼드 리더가 리더를 위임한다.")
	@Parameters({
		@Parameter(name = "member", hidden = true),
		@Parameter(name = "squadId", description = "스쿼드의 ID, path variable"),
		@Parameter(name = "memberId", description = "새로운 리더의 ID, path variable")
	})
	public ApiResponse<Void> assignLeader(
		@Member LoginMember member,
		@PathVariable("squadId") Long squadId,
		@PathVariable("memberId") Long newLeaderId
	){
		squadMemberService.assignLeader(squadId, member.getMemberId(), newLeaderId);
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}

	@DeleteMapping("/{squadId}/members/")
	@Operation(summary = "스쿼드를 탈퇴한다", description = "스쿼드를 탈퇴한다. 단, 리더인 경우 스쿼드 멤버가 없어야 탈퇴 가능하다.")
	public ApiResponse<Void> leaveSquad(
		@Parameter(hidden = true) @Member LoginMember member,
		@PathVariable("squadId") Long squadId
	){
		squadMemberService.leaveSquad(squadId,member.getMemberId());
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}
}
