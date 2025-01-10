package com.eggmeonina.scrumble.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import com.eggmeonina.scrumble.common.exception.ErrorCode;
import com.eggmeonina.scrumble.common.exception.ExpectedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		log.info("authorizationInterceptor start");
		HttpSession session = request.getSession(false);
		if (session == null) {
			log.info("미인증 사용자 요청 URI : {}", request.getRequestURI());
			throw new ExpectedException(ErrorCode.UNAUTHORIZED_ACCESS);
		}
		return true;
	}
}
