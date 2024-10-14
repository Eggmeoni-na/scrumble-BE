package com.eggmeonina.scrumble.domain.member.controller;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggmeonina.scrumble.common.anotation.Member;
import com.eggmeonina.scrumble.common.domain.ApiResponse;
import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.domain.auth.dto.LoginMember;
import com.eggmeonina.scrumble.domain.member.dto.MemberInvitationResponse;
import com.eggmeonina.scrumble.domain.member.dto.MemberResponse;
import com.eggmeonina.scrumble.domain.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Tag(name = "Member 테스트", description = "회원조회, 회원탈퇴 등 API Controller")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final MemberService memberService;

	@GetMapping("/me")
	@Operation(summary = "나의 회원 정보 조회", description = "나의 회원 정보를 조회한다.")
	public ApiResponse<MemberResponse> findMember(@Parameter(hidden = true) @Member LoginMember member){
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), memberService.findMember(member.getMemberId()));
	}

	@DeleteMapping
	@Operation(summary = "회원 탈퇴", description = "회원 탈퇴 및 세션을 만료한다")
	public ApiResponse<Void> withdrawMember(@Parameter(hidden = true) @Member LoginMember member,
		HttpServletRequest servletRequest){
		HttpSession session = servletRequest.getSession(false);
		if (session == null) {
			throw new MemberException(UNAUTHORIZED_ACCESS);
		}
		memberService.withdraw(member.getMemberId());
		session.invalidate();
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}

	@GetMapping("/{email}")
	@Operation(summary = "회원 정보 조회(초대용)", description = "회원의 정보를 조회한다.")
	public ApiResponse<MemberInvitationResponse> findMember(@PathVariable String email){
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), memberService.findMember(email));
	}

}
