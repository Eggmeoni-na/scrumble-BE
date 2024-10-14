package com.eggmeonina.scrumble.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;


@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		io.swagger.v3.oas.models.info.Info info = new io.swagger.v3.oas.models.info.Info()
			.title("스크럼블 API 명세서")
			.description("데일리 스크럼 공유 서비스 '스크럼블' API 명세서")
			.version("v0.0.1")
			.license(new License().name("Eggmeoni-na, Git Url")
				.url("https://github.com/Eggmeoni-na")
			);
		return new OpenAPI()
			.info(info);
	}
}
