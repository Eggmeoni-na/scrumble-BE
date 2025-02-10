package com.eggmeonina.scrumble.fixture;

import java.time.LocalDateTime;

import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;

public class MemberFixture {

	public static Member createJOINMember(String email, String name, String oauthId) {
		return Member.create()
			.email(email)
			.name(name)
			.memberStatus(MemberStatus.JOIN)
			.joinedAt(LocalDateTime.now())
			.oauthInformation(new OauthInformation(oauthId, OauthType.GOOGLE))
			.build();
	}

	public static Member createWITHDRAWMember(String email, String name, String oauthId) {
		return Member.create()
			.email(email)
			.name(name)
			.memberStatus(MemberStatus.WITHDRAW)
			.joinedAt(LocalDateTime.now())
			.oauthInformation(new OauthInformation(oauthId, OauthType.GOOGLE))
			.build();
	}
}
