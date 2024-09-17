package com.eggmeonina.scrumble.domain.membership.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MembershipStatus {
	INVITING("초대 중"),
	JOIN("가입"),
	WITHDRAW("탈퇴"),
	CANCEL("초대 취소");

	private final String desc;
}
