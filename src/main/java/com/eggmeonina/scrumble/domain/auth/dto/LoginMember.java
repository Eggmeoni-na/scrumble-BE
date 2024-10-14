package com.eggmeonina.scrumble.domain.auth.dto;

import com.eggmeonina.scrumble.domain.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginMember {
	private Long memberId;
	private String email;
	private String name;

	public static LoginMember from(Member member){
		return new LoginMember(member.getId(), member.getEmail(), member.getName());
	}
}
