package com.eggmeonina.scrumble.domain.membership.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
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
import com.eggmeonina.scrumble.domain.membership.dto.SquadCreateRequest;
import com.eggmeonina.scrumble.domain.membership.dto.SquadDetailResponse;
import com.eggmeonina.scrumble.domain.membership.dto.SquadResponse;
import com.eggmeonina.scrumble.domain.membership.dto.SquadUpdateRequest;
import com.eggmeonina.scrumble.domain.membership.facade.MembershipFacadeService;
import com.eggmeonina.scrumble.domain.membership.service.MembershipService;
import com.eggmeonina.scrumble.domain.membership.service.SquadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Squad 테스트", description = "squad 생성, 삭제, 조회용 API Controller")
@RestController
@RequestMapping("/api/squads")
@RequiredArgsConstructor
public class SquadController {

	private final MembershipFacadeService membershipFacadeService;
	private final MembershipService membershipService;
	private final SquadService squadService;

	@PostMapping
	@Operation(summary = "스쿼드를 생성한다", description = "스쿼드를 생성하면서 스쿼드장을 같이 생성한다.")
	public ApiResponse<Map<String, Long>> createSquad(
		@Parameter(hidden = true) @Member LoginMember member,
		@RequestBody @Valid SquadCreateRequest request
	) {
		Long squadId = membershipFacadeService.createSquad(member.getMemberId(), request);
		return ApiResponse.createSuccessResponse(HttpStatus.CREATED.value(), Map.of("squadId", squadId));
	}

	@GetMapping
	@Operation(summary = "나의 스쿼드를 조회한다", description = "내가 속해있는 스쿼드들을 조회한다.")
	public ApiResponse<List<SquadResponse>> findSquads(
		@Parameter(hidden = true) @Member LoginMember member
	) {
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), membershipService.findBySquads(member.getMemberId()));
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
		membershipService.updateSquad(member.getMemberId(), squadId, request);
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}
}
