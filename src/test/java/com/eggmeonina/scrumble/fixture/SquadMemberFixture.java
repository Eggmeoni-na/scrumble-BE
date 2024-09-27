package com.eggmeonina.scrumble.fixture;

import java.time.LocalDateTime;

import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMember;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberRole;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberStatus;

public class SquadMemberFixture {
	public static SquadMember createSquadMember(Squad newSquad, Member newLeader, SquadMemberRole leader, SquadMemberStatus status) {
		return SquadMember.create()
			.squad(newSquad)
			.member(newLeader)
			.squadMemberRole(leader)
			.squadMemberStatus(status)
			.build();
	}

	public static Squad createSquad(String squadName) {
		return Squad.create()
			.squadName(squadName)
			.deletedFlag(false)
			.build();
	}

	public static Member createMember(String email, String name, MemberStatus status) {
		return Member.create()
			.email(email)
			.name(name)
			.memberStatus(status)
			.oauthInformation(new OauthInformation("1234567", OauthType.GOOGLE))
			.joinedAt(LocalDateTime.now())
			.build();
	}
}
