package com.eggmeonina.scrumble.domain.member.dto;

import com.eggmeonina.scrumble.domain.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInvitationResponse {

	private Long memberId;
	private String name;
	private String profileImage;

	public static MemberInvitationResponse from(Member member){
		return new MemberInvitationResponse(member.getId(), member.getName(), member.getProfileImage());
	}
}
