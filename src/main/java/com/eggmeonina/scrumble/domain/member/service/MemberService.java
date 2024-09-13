package com.eggmeonina.scrumble.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.auth.dto.LoginMember;
import com.eggmeonina.scrumble.domain.auth.domain.MemberInformation;
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
			.orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
		return MemberResponse.from(foundMember);
	}
}
