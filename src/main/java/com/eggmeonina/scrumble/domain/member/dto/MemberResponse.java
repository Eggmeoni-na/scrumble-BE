package com.eggmeonina.scrumble.domain.member.dto;

import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
	private OauthType oauthType;
	private String email;

	public static MemberResponse from(Member member){
		return new MemberResponse(member.getOauthInformation().getOauthType(), member.getEmail());
	}
}
