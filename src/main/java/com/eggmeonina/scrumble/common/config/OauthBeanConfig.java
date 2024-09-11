package com.eggmeonina.scrumble.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eggmeonina.scrumble.domain.auth.client.AuthClient;
import com.eggmeonina.scrumble.domain.auth.client.GoogleAuthClient;
import com.eggmeonina.scrumble.domain.auth.client.strategy.GoogleRequestResourceStrategy;
import com.eggmeonina.scrumble.domain.auth.client.strategy.GoogleRequestTokenStrategy;
import com.eggmeonina.scrumble.domain.auth.controller.generator.GoogleGenerator;
import com.eggmeonina.scrumble.domain.auth.properties.GoogleProperties;
import com.eggmeonina.scrumble.domain.auth.controller.generator.OauthGenerator;

@Configuration
public class OauthBeanConfig {

	private final GoogleProperties googleProperties;
	private final GoogleRequestTokenStrategy googleRequestTokenStrategy;

	private final GoogleRequestResourceStrategy googleRequestResourceStrategy;

	@Autowired
	public OauthBeanConfig(
		GoogleProperties googleProperties,
		GoogleRequestTokenStrategy googleRequestTokenStrategy,
		GoogleRequestResourceStrategy googleRequestResourceStrategy
	) {
		this.googleProperties = googleProperties;
		this.googleRequestTokenStrategy = googleRequestTokenStrategy;
		this.googleRequestResourceStrategy = googleRequestResourceStrategy;
	}

	// generator bean init
	@Bean
	public OauthGenerator googleGenerator() {
		return new GoogleGenerator(googleProperties);
	}

	// auth client bean init
	@Bean
	public AuthClient googleAuthClient() {
		return new GoogleAuthClient(googleProperties, googleRequestTokenStrategy, googleRequestResourceStrategy);
	}
}
