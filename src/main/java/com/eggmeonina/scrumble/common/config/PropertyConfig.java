package com.eggmeonina.scrumble.common.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.eggmeonina.scrumble.ScrumbleApplication;

@Configuration
@PropertySource(
	value =
		{"classpath:/property/google-oauth-${spring.profiles.active}.properties",
			"classpath:/property/cors-origin.properties"},
	ignoreResourceNotFound = true
)
@ConfigurationPropertiesScan(basePackageClasses = ScrumbleApplication.class)
public class PropertyConfig {
}
