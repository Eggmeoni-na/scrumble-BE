package com.eggmeonina.scrumble.common.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

import com.eggmeonina.scrumble.domain.auth.client.GoogleAuthClient;

@Configuration
@EnableFeignClients(basePackageClasses = GoogleAuthClient.class)
public class FeignClientConfig {
}
