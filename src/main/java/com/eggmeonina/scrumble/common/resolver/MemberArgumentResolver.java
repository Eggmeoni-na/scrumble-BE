package com.eggmeonina.scrumble.common.resolver;

import static com.eggmeonina.scrumble.common.exception.ErrorCode.*;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.eggmeonina.scrumble.common.anotation.LoginMember;
import com.eggmeonina.scrumble.common.exception.AuthException;
import com.eggmeonina.scrumble.domain.auth.dto.MemberInfo;
import com.eggmeonina.scrumble.domain.member.domain.Member;
import com.eggmeonina.scrumble.domain.member.domain.SessionKey;
import com.eggmeonina.scrumble.domain.member.repository.MemberRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

	private final MemberRepository memberRepository;

	public MemberArgumentResolver(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		log.debug("supportsParameter 실행");
		boolean hasParameterAnnotation = parameter.hasParameterAnnotation(LoginMember.class);
		boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
		return hasParameterAnnotation && hasMemberType;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		HttpSession session = request.getSession(false);
		if (session == null) {
			throw new AuthException(UNAUTHORIZED_ACCESS);
		}
		MemberInfo member = (MemberInfo) session.getAttribute(SessionKey.LOGIN_USER.name());
		return memberRepository.findByIdAndMemberStatusNotJOIN(member.getMemberId())
			.orElseThrow(() -> new AuthException(UNAUTHORIZED_ACCESS));
	}
}
