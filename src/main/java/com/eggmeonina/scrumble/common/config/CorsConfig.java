package com.eggmeonina.scrumble.common.config;

import static org.springframework.http.HttpMethod.*;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.eggmeonina.scrumble.common.properties.CorsProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile({"dev"})
@RequiredArgsConstructor
@Configuration
public class CorsConfig implements WebMvcConfigurer {

	private final CorsProperties corsProperties;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
			.allowedOrigins(corsProperties.getOriginArr())
			.allowedMethods(
				GET.name(),
				HEAD.name(),
				POST.name(),
				PUT.name(),
				DELETE.name(),
				OPTIONS.name())
			.allowCredentials(true);
	}
}
