package com.eggmeonina.scrumble.common.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

	private final List<String> origins;

	public String[] getOriginArr(){
		return origins.toArray(new String[0]);
	}
}
