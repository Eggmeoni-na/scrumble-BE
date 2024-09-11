package com.eggmeonina.scrumble.common.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.eggmeonina.scrumble.common.anotation.Member;
import com.eggmeonina.scrumble.domain.member.domain.SessionKey;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean hasMemberAnnotation = parameter.hasParameterAnnotation(Member.class);
		boolean hasLoginUserType = Member.class.isAssignableFrom(parameter.getParameterType());
		return hasMemberAnnotation && hasLoginUserType;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		return session.getAttribute(SessionKey.LOGIN_USER.name());
	}
}
