package com.eggmeonina.scrumble.fixture;

import java.time.LocalDateTime;
import java.util.List;

import com.eggmeonina.scrumble.domain.auth.domain.OauthType;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.MemberStatus;
import com.eggmeonina.scrumble.domain.member.domain.OauthInformation;
import com.eggmeonina.scrumble.domain.squadmember.domain.Squad;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMember;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberRole;
import com.eggmeonina.scrumble.domain.squadmember.domain.SquadMemberStatus;

public class SquadMemberFixture {
	public static SquadMember createSquadMember(Squad newSquad, Member member, SquadMemberRole role, SquadMemberStatus status) {
		return SquadMember.create()
			.squad(newSquad)
			.member(member)
			.squadMemberRole(role)
			.squadMemberStatus(status)
			.build();
	}
	public static SquadMember createNormalSquadMember(Member newMember, Squad squad) {
		return SquadMember.create()
			.member(newMember)
			.squad(squad)
			.squadMemberStatus(SquadMemberStatus.JOIN)
			.squadMemberRole(SquadMemberRole.NORMAL)
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

	public static List<SquadMember> createSquadMembers(SquadMember... squadMember){
		return List.of(squadMember);
	}
}
