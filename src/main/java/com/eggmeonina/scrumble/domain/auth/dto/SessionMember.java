package com.eggmeonina.scrumble.domain.auth.dto;

import com.eggmeonina.scrumble.domain.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionMember {
	private Long memberId;
	private String email;

	public static SessionMember from(Member member){
		return new SessionMember(member.getId(), member.getEmail());
	}
}
