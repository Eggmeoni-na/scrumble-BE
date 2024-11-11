package com.eggmeonina.scrumble.domain.auth.dto;

import com.eggmeonina.scrumble.domain.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInfo {
	private Long memberId;
	private String email;
	private String name;

	public static MemberInfo from(Member member){
		return new MemberInfo(member.getId(), member.getEmail(), member.getName());
	}
}
