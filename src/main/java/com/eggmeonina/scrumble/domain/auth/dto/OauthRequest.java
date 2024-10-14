package com.eggmeonina.scrumble.domain.auth.dto;

import com.eggmeonina.scrumble.domain.auth.domain.OauthType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OauthRequest {
	private OauthType oauthType;
	private String code;
	private String scope;
}
