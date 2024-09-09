package com.eggmeonina.scrumble.domain.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OauthType {
	GOOGLE("googleGenerator");

	private final String generatorName;

}
