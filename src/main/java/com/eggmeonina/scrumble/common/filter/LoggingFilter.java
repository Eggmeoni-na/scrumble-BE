package com.eggmeonina.scrumble.common.filter;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		MDC.put("request_id", UUID.randomUUID().toString());

		ContentCachingRequestWrapper wrapperRequest = new ContentCachingRequestWrapper(request);

		log.info("[{}] request URI : {}", wrapperRequest.getMethod(), wrapperRequest.getRequestURI());
		long startTime = System.currentTimeMillis();
		filterChain.doFilter(request, response);
		long endTime = System.currentTimeMillis();
		log.info("request resultTime = {}ms", endTime -  startTime);

		MDC.clear();
	}

}
