package com.eggmeonina.scrumble.common.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.eggmeonina.scrumble.common.anotation.Member;
import com.eggmeonina.scrumble.domain.auth.dto.LoginMember;
import com.eggmeonina.scrumble.domain.member.domain.SessionKey;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		log.info("supportsParameter 실행");
		boolean hasMemberAnnotation = parameter.hasParameterAnnotation(Member.class);
		boolean hasLoginUserType = LoginMember.class.isAssignableFrom(parameter.getParameterType());
		return hasMemberAnnotation && hasLoginUserType;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		HttpSession session = request.getSession(false);
		if (session == null) {
			throw new RuntimeException("로그인하지 않은 회원입니다.");
		}
		return session.getAttribute(SessionKey.LOGIN_USER.name());
	}
}
