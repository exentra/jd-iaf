package com.deere.tools.logging;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;

/**
 * Add http data (url, method, user) to log context in order to be able to trace
 * source of log message
 */
public class LogFilter implements Filter {

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		// nothing to do here
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {

		final HttpServletRequest httpRequest = (HttpServletRequest) request;

		final String userId = Optional.ofNullable(httpRequest.getUserPrincipal()).map(Principal::getName)
				.orElse("anonymous");

		final String ip = Optional.ofNullable(httpRequest.getHeader("x-forwarded-for")).orElse(httpRequest.getRemoteAddr());

		MDC.put("http.url", httpRequest.getRequestURI());
		MDC.put("http.method", httpRequest.getMethod());
		MDC.put("client.ip", ip);
		MDC.put("user", userId);

		chain.doFilter(request, response);

		MDC.remove("http.url");
		MDC.remove("http.method");
		MDC.remove("client.ip");
		MDC.remove("user");
	}

	@Override
	public void destroy() {
		// nothing to do here
	}

}
