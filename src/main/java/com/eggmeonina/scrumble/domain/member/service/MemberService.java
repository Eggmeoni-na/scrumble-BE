package com.eggmeonina.scrumble.domain.member.service;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.common.exception.MemberException;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.auth.dto.LoginMember;
import com.eggmeonina.scrumble.domain.auth.domain.MemberInformation;
import com.eggmeonina.scrumble.domain.member.dto.MemberInvitationResponse;
import com.eggmeonina.scrumble.domain.member.dto.MemberResponse;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional
	public LoginMember login(MemberInformation memberInformation, OauthType oauthType){
		Member foundMember = memberRepository.findByOauthId(memberInformation.getOauthId())
			.orElseGet(() -> memberRepository.save(MemberInformation.to(memberInformation, oauthType)));
		return LoginMember.from(foundMember);
	}

	public MemberResponse findMember(Long memberId){
		Member foundMember = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
		return MemberResponse.from(foundMember);
	}

	@Transactional
	public void withdraw(Long memberId) {
		Member foundMember = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
		foundMember.withdraw();
	}

	public MemberInvitationResponse findMember(String email){
		Member foundMember = memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
		return MemberInvitationResponse.from(foundMember);
	}
}
