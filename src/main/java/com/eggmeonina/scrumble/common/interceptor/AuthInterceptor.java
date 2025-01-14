package com.eggmeonina.scrumble.common.interceptor;

import static org.springframework.http.HttpMethod.*;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		log.debug("authorizationInterceptor start");
		HttpSession session = request.getSession(false);
		// preflight 요청은 interceptor에서 무시한다
		if (session == null && !OPTIONS.name().equals(request.getMethod())) {
			log.debug("미인증 사용자 요청 URI : {}", request.getRequestURI());
			// throw new ExpectedException(ErrorCode.UNAUTHORIZED_ACCESS);
		}
		return true;
	}
}
