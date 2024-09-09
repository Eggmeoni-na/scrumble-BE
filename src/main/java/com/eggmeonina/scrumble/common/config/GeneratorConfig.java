package com.eggmeonina.scrumble.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eggmeonina.scrumble.domain.auth.controller.generator.GoogleGenerator;
import com.eggmeonina.scrumble.domain.auth.properties.GoogleProperties;
import com.eggmeonina.scrumble.domain.auth.controller.generator.OauthGenerator;

@Configuration
public class GeneratorConfig {

	private final GoogleProperties googleProperties;

	@Autowired
	public GeneratorConfig(GoogleProperties googleProperties) {
		this.googleProperties = googleProperties;
	}

	@Bean
	public OauthGenerator googleGenerator(){
		return new GoogleGenerator(googleProperties);
	}
}
