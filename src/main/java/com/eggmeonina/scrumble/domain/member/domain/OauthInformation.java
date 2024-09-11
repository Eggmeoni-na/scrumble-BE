package com.eggmeonina.scrumble.domain.member.domain;

import com.eggmeonina.scrumble.domain.auth.domain.OauthType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class OauthInformation {

	@Column(name = "oauth_id")
	private String oauthId;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "oauth_type")
	private OauthType oauthType;

	public OauthInformation(final String oauthId, final OauthType oauthType) {
		this.oauthId = oauthId;
		this.oauthType = oauthType;
	}
}
