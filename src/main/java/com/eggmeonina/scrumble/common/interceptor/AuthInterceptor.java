package com.eggmeonina.scrumble.common.interceptor;

import static org.springframework.http.HttpMethod.*;

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
		HttpSession session = request.getSession(false);
		// preflight 요청은 interceptor에서 무시한다
		if (session == null && !OPTIONS.name().equals(request.getMethod())) {
			throw new ExpectedException(ErrorCode.UNAUTHORIZED_ACCESS);
		}
		return true;
	}
}
