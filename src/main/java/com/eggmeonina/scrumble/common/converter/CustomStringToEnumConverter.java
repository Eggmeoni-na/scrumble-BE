package com.eggmeonina.scrumble.common.converter;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.eggmeonina.scrumble.common.exception.ExpectedException;
import com.eggmeonina.scrumble.domain.auth.domain.OauthType;

@Component
public class CustomStringToEnumConverter implements Converter<String, OauthType> {
	@Override
	public OauthType convert(String s) {
		try {
			return OauthType.valueOf(s.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new ExpectedException(TYPE_NOT_SUPPORTED);
		}
	}
}
