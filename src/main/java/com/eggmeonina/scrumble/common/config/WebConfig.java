package com.eggmeonina.scrumble.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
}
