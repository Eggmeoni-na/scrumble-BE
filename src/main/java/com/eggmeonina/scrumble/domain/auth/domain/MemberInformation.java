package com.eggmeonina.scrumble.domain.auth.domain;

import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class MemberInformation {
	private final String oauthId;
	private final String email;
	private final String name;
	private final String imageUrl;

	@JsonCreator
	public MemberInformation(
		@JsonProperty("id") String oauthId,
		@JsonProperty("email") String email,
		@JsonProperty("name") String name,
		@JsonProperty("picture") String imageUrl) {
		this.oauthId = oauthId;
		this.email = email;
		this.name = name;
		this.imageUrl = imageUrl;
	}

	public static Member to(MemberInformation memberInformation, OauthType type){
		return Member.create()
			.oauthInformation(new OauthInformation(memberInformation.getOauthId(), type))
			.memberStatus(MemberStatus.JOIN)
			.email(memberInformation.getEmail())
			.name(memberInformation.getName())
			.profileImage(memberInformation.getImageUrl())
			.build();
	}
}
