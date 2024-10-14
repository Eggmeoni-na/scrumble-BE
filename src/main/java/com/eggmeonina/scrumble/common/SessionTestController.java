package com.eggmeonina.scrumble.common;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggmeonina.scrumble.common.domain.ApiResponse;
import com.eggmeonina.scrumble.common.exception.ErrorCode;
import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.common.exception.SquadException;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.auth.dto.LoginMember;
import com.eggmeonina.scrumble.domain.auth.dto.LoginResponse;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;
import com.eggmeonina.scrumble.domain.member.domain.SessionKey;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMember;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberRole;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberStatus;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadMemberRepository;
import com.eggmeonina.scrumble.domain.squadmember.repository.SquadRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Profile("!prod")
@Tag(name = "데이터 생성용 간소화 테스트", description = "개발기용 - 데이터 생성용 간소화 API Controller")
@RestController
@RequestMapping("/api/test/session")
@RequiredArgsConstructor
public class SessionTestController {

	private final MemberRepository memberRepository;
	private final SquadMemberRepository squadMemberRepository;
	private final SquadRepository squadRepository;

	@PostMapping("/no-content-session")
	@Operation(summary = "세션 발급", description = "테스트 유저로 세션을 발급합니다.")
	public ApiResponse<LoginResponse> getSessionNoContents(HttpServletRequest request){
		HttpSession session = request.getSession(true);
		Member member = memberRepository.findById(1L).get();
		LoginMember loginMember = new LoginMember(member.getId(), member.getEmail(), member.getName());
		session.setAttribute(SessionKey.LOGIN_USER.name(), loginMember);
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), LoginResponse.from(loginMember));
	}

	@PostMapping("/session")
	@Operation(summary = "이메일로 세션 발급", description = "입력한 이메일로 로그인하여 세션을 발급합니다. - 회원 가입 후 진행")
	public ApiResponse<LoginResponse> getSession(@Parameter @RequestBody TestSessionMember request, HttpServletRequest servletRequest){
		HttpSession session = servletRequest.getSession(true);
		Member member = memberRepository.findByEmail(request.email).get();
		LoginMember loginMember = new LoginMember(member.getId(), member.getEmail(), member.getName());
		session.setAttribute(SessionKey.LOGIN_USER.name(), loginMember);
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), LoginResponse.from(loginMember));
	}

	@PostMapping("/join")
	@Operation(summary = "이메일로 회원 가입 및 로그인", description = "입력한 이메일로 회원 가입 및 로그인 합니다.")
	public ApiResponse<LoginResponse> join(@Parameter @RequestBody MemberCreateRequest request, HttpServletRequest servletRequest){
		HttpSession session = servletRequest.getSession(true);
		Member newMember = Member
			.create()
			.name("테스트 유저")
			.email(request.email)
			.joinedAt(LocalDateTime.now())
			.profileImage("")
			.oauthInformation(new OauthInformation("123234", OauthType.GOOGLE))
			.memberStatus(MemberStatus.JOIN)
			.build();
		memberRepository.save(newMember);
		LoginMember loginMember = new LoginMember(newMember.getId(), newMember.getEmail(), newMember.getName());
		session.setAttribute(SessionKey.LOGIN_USER.name(), loginMember);
		return ApiResponse.createSuccessResponse(HttpStatus.OK.value(), LoginResponse.from(loginMember));
	}

	@PostMapping("/invite/{squadId}")
	@Operation(summary = "로그인한 회원으로 스쿼드 가입", description = "스쿼드에 가입합니다.")
	public ApiResponse<Void> join(@PathVariable Long squadId, @Parameter(hidden = true) @com.eggmeonina.scrumble.common.anotation.Member LoginMember member) {
		Squad foundSquad = squadRepository.findByIdAndDeletedFlagNot(squadId)
			.orElseThrow(() -> new SquadException(ErrorCode.SQUAD_NOT_FOUND));
		Member foundMember = memberRepository.findById(member.getMemberId())
			.orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
		SquadMember newSquadMember = SquadMember.create()
			.member(foundMember)
			.squad(foundSquad)
			.squadMemberRole(SquadMemberRole.NORMAL)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.build();

		squadMemberRepository.save(newSquadMember);
		return ApiResponse.createSuccessWithNoContentResponse(HttpStatus.OK.value());
	}

	@Getter
	@NoArgsConstructor
	public static class TestSessionMember {
		private String email;
	}

	@Getter
	@NoArgsConstructor
	public static class MemberCreateRequest {
		private String email;
	}
}
