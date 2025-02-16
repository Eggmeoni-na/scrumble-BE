package com.eggmeonina.scrumble.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Profile("!prod")
@Configuration
public class SwaggerConfig {

	@Value("${swagger.server.endpoint}")
	private String endPoint;

	@Bean
	public OpenAPI openAPI() {
		io.swagger.v3.oas.models.info.Info info = new io.swagger.v3.oas.models.info.Info()
			.title("스크럼블 API 명세서")
			.description("데일리 스크럼 공유 서비스 '스크럼블' API 명세서")
			.version("v0.0.1")
			.license(new License().name("Eggmeoni-na, Git Url")
				.url("https://github.com/Eggmeoni-na")
			);

		// 스웨거는 기본적으로 http 통신하기 때문에 https 통신 가능하도록 변경
		Server server = new Server();
		server.setUrl(endPoint);

		return new OpenAPI()
			.info(info)
			.servers(List.of(server));
	}
}
