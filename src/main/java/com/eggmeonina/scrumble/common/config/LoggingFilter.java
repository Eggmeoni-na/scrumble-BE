package com.eggmeonina.scrumble.common.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter implements Filter {
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
		IOException,
		ServletException {

		ContentCachingRequestWrapper httpServletRequest =
			new ContentCachingRequestWrapper((HttpServletRequest)servletRequest);
		ContentCachingResponseWrapper httpServletResponse =
			new ContentCachingResponseWrapper((HttpServletResponse)servletResponse);

		try {
			log.info("request URI ========= {}", httpServletRequest.getRequestURI());
			log.info("========= filter start ========= ");
			filterChain.doFilter(httpServletRequest, httpServletResponse);

		} catch (Exception e) {
			log.error("{}", e.getMessage());
		} finally {
			log.info("========= filter end ========= ");
			Collection<String> headerNames = httpServletResponse.getHeaderNames();
			for (String headerName : headerNames) {
				log.info("headerName = {}, headerValue = {}", headerName, httpServletResponse.getHeader(headerName));
			}
			httpServletResponse.copyBodyToResponse();
		}
	}
}
