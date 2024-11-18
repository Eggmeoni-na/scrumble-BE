package com.eggmeonina.scrumble.domain.squadmember.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggmeonina.scrumble.common.anotation.LoginMember;
import com.eggmeonina.scrumble.common.domain.ApiResponse;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadMemberAssignRequest;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadMemberInvitationAcceptRequest;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadMemberInvitationRequest;
import com.eggmeonina.scrumble.domain.squadmember.dto.SquadMemberKickRequest;
import com.eggmeonina.scrumble.domain.squadmember.service.SquadMemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "SquadMember 테스트", description = "스쿼드에 속한 회원의 생성, 삭제, 조회용 API Controller")
@RestController
@RequestMapping("/api/squads")
@RequiredArgsConstructor
public class SquadMemberController {

	private final SquadMemberService squadMemberService;

	@PutMapping("/{squadId}/leader")
	@Operation(summary = "스쿼드 리더를 위임한다", description = "스쿼드 리더가 리더를 위임한다.")
	@Parameter(name = "member", hidden = true)
	@Parameter(name = "squadId", description = "스쿼드의 ID, path variable")
	public ApiResponse<Void> assignLeader(
		@LoginMember Member member,
		@PathVariable("squadId") Long squadId,
		@RequestBody @Valid SquadMemberAssignRequest request
	) {
		squadMemberService.assignLeader(squadId, member.getId(), request.getNewLeaderId());
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}

	@DeleteMapping("/{squadId}/withdraw")
	@Operation(summary = "스쿼드를 탈퇴한다", description = "스쿼드를 탈퇴한다. 단, 리더인 경우 스쿼드 멤버가 없어야 탈퇴 가능하다.")
	public ApiResponse<Void> leaveSquad(
		@Parameter(hidden = true) @LoginMember Member member,
		@PathVariable("squadId") Long squadId
	) {
		squadMemberService.leaveSquad(squadId, member.getId());
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}

	@DeleteMapping("/{squadId}/kick")
	@Parameter(name = "member", hidden = true)
	@Parameter(name = "squadId", description = "스쿼드의 ID, path variable")
	@Operation(summary = "스쿼드 멤버를 강퇴한다", description = "스쿼드 멤버를 강퇴한다. 단, 리더인 경우에만 강퇴가 가능하다.")
	public ApiResponse<Void> kickSquadMember(
		@Parameter(hidden = true) @LoginMember Member member,
		@PathVariable("squadId") Long squadId,
		@RequestBody @Valid SquadMemberKickRequest request
	) {
		squadMemberService.kickSquadMember(squadId, member.getId(), request.getKickedMemberId());
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}

	@PostMapping("/{squadId}/invitations")
	@Operation(summary = "스쿼드 멤버를 초대(추가)한다", description = "스쿼드 멤버를 초대(추가)한다.")
	public ApiResponse<Void> inviteMember(
		@PathVariable("squadId") Long squadId,
		@RequestBody @Valid SquadMemberInvitationRequest request
	) {
		squadMemberService.inviteSquadMember(request.getNewMemberId(), squadId);
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}

	@PutMapping("/{squadId}/invitations/accept")
	@Operation(summary = "스쿼드 초대를 수락한다", description = "스쿼드 초대를 수락한다.")
	public ApiResponse<Void> inviteMember(
		@PathVariable("squadId") Long squadId,
		@Parameter(hidden = true) @LoginMember Member member,
		@RequestBody @Valid SquadMemberInvitationAcceptRequest request
	) {
		squadMemberService.responseInvitation(squadId, member.getId(), request.getResponseStatus());
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}

}
