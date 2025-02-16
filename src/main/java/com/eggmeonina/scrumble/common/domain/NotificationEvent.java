package com.eggmeonina.scrumble.common.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationEvent {
	INVITE_REQUEST("초대 요청");

	private final String desc;
}
