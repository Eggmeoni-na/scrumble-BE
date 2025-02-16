package com.eggmeonina.scrumble.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.eggmeonina.scrumble.common.interceptor.AuthInterceptor;
import com.eggmeonina.scrumble.common.resolver.MemberArgumentResolver;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private MemberRepository memberRepository;
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new MemberArgumentResolver(memberRepository));
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AuthInterceptor())
			// 인터셉터는 컨트롤러 이전에 작동하기 때문에 정적 리소스는 제외
			.excludePathPatterns("/", "/api/auth/**", "/api/test/**", "/error",
				"/swagger-ui/**", "/api-docs/**");
	}
}
