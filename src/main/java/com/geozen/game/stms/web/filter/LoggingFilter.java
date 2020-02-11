package com.geozen.game.stms.web.filter;

import java.io.IOException;
import java.net.InetAddress;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.geozen.game.stms.web.request.MultiReadHttpServletRequest;

@Component
public class LoggingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		String url = req.getRequestURI();
		if (req.getQueryString() != null) {
			url += "?" + req.getQueryString();
		}
		if (MediaType.APPLICATION_JSON_VALUE.equals(req.getContentType())) {
			MultiReadHttpServletRequest reqWrapper = new MultiReadHttpServletRequest(req);
			if (reqWrapper.getBodyByteArray() != null) {
				MDC.put("requestBody", new String(reqWrapper.getBodyByteArray()));
			}
			req = reqWrapper;
		}
		if (req.getCookies() != null) {
			StringBuilder cookies = new StringBuilder("{");
			Cookie[] cookieArr = req.getCookies();
			for (int i = 0; i < cookieArr.length; i++) {
				Cookie cookie = cookieArr[i];
				cookies.append(cookie.getName()).append(": ").append(cookie.getValue());
				if (i < cookieArr.length - 1) {
					cookies.append(", ");
				}
			}
			cookies.append("}");
			MDC.put("cookies", cookies.toString());
		}
		MDC.put("queryString", req.getQueryString());
		MDC.put("requestUri", url);
		String clientIP = req.getHeader("X-Forwarded-For");
		if (StringUtils.isEmpty(clientIP)) {
			clientIP = req.getRemoteAddr();
		}
		MDC.put("clientIP", clientIP);
		MDC.put("hostIP", InetAddress.getLocalHost().getHostAddress());
		filterChain.doFilter(req, response);
	}

}
