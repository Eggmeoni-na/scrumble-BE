package com.eggmeonina.scrumble.domain.membership.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggmeonina.scrumble.common.anotation.Member;
import com.eggmeonina.scrumble.common.domain.ApiResponse;
import com.eggmeonina.scrumble.domain.auth.dto.LoginMember;
import com.eggmeonina.scrumble.domain.membership.dto.SquadCreateRequest;
import com.eggmeonina.scrumble.domain.membership.dto.SquadResponse;
import com.eggmeonina.scrumble.domain.membership.facade.MembershipFacadeService;
import com.eggmeonina.scrumble.domain.membership.service.MembershipService;

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


}
