package com.eggmeonina.scrumble.common.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

import com.eggmeonina.scrumble.ScrumbleApplication;

@Configuration
@ConfigurationPropertiesScan(basePackageClasses = ScrumbleApplication.class)
public class PropertyConfig {
}
