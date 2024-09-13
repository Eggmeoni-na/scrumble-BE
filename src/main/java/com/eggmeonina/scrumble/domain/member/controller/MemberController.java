package com.eggmeonina.scrumble.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggmeonina.scrumble.common.anotation.Member;
import com.eggmeonina.scrumble.common.domain.ApiResponse;
import com.eggmeonina.scrumble.domain.auth.dto.LoginMember;
import com.eggmeonina.scrumble.domain.member.dto.MemberResponse;
import com.eggmeonina.scrumble.domain.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Member 테스트", description = "회원조회, 회원탈퇴 등 API Controller")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/me")
	@Operation(summary = "나의 회원 정보 조회", description = "나의 회원 정보를 조회한다.")
	public ApiResponse<MemberResponse> findMember(@Member LoginMember member){
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), memberService.findMember(member.getMemberId()));
	}

}
