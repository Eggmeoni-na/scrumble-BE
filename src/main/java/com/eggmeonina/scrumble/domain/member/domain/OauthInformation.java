package com.eggmeonina.scrumble.domain.member.domain;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		OauthInformation that = (OauthInformation)o;
		return Objects.equals(getOauthId(), that.getOauthId()) && getOauthType() == that.getOauthType();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getOauthId(), getOauthType());
	}
}
