package com.eggmeonina.scrumble.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggmeonina.scrumble.common.domain.ApiResponse;
import com.eggmeonina.scrumble.domain.auth.dto.LoginMember;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.SessionKey;
import com.eggmeonina.scrumble.domain.member.dto.MemberResponse;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Tag(name = "세션 발급", description = "개발기용 - 세션 발급 API Controller")
@RestController
@RequestMapping("/api/test/session")
@RequiredArgsConstructor
public class SessionTestController {

	private final MemberService memberService;
	private final MemberRepository memberRepository;

	@PostMapping("/no-content-session")
	@Operation(summary = "세션 발급", description = "테스트 유저로 세션을 발급합니다.")
	public ApiResponse<Void> getSessionNoContents(HttpServletRequest request){
		HttpSession session = request.getSession(true);
		MemberResponse response = memberService.findMember(1L);
		session.setAttribute(SessionKey.LOGIN_USER.name(), new LoginMember(1L, response.getEmail(), ""));
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}

	@PostMapping("/session")
	@Operation(summary = "이메일로 세션 발급", description = "입력한 이메일로 로그인하여 세션을 발급합니다. - 회원 가입 후 진행")
	public ApiResponse<String> getSession(@Parameter @RequestBody TestSessionMember request, HttpServletRequest servletRequest){
		HttpSession session = servletRequest.getSession(true);
		Member member = memberRepository.findByEmail(request.email).get();
		session.setAttribute(SessionKey.LOGIN_USER.name(), new LoginMember(member.getId(), member.getEmail(), member.getName()));
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}

	@Getter
	@NoArgsConstructor
	public static class TestSessionMember {
		private String email;
	}
}
