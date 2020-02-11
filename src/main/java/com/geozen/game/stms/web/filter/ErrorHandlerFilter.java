package com.geozen.game.stms.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geozen.game.stms.dto.ResultBean;
import com.geozen.game.stms.exception.BusinessException;
import com.geozen.game.stms.util.LogUtil;

@Component
public class ErrorHandlerFilter extends OncePerRequestFilter {
	@Autowired
	private ObjectMapper mapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			String traceId = LogUtil.getTraceId();
			if (!e.getMessage().toLowerCase().contains("reset by peer")) {
				logger.error("error-[" + traceId + "]: " + e.getMessage(), e);
			}
			String msg = null;
			if (!response.isCommitted()) {
				if (BusinessException.class.isAssignableFrom(e.getClass())) {
					BusinessException ex = (BusinessException) e;
					msg = ex.getMessage();
				} else if (ValidationException.class.isAssignableFrom(e.getClass())) {
					if (ConstraintViolationException.class.isAssignableFrom(e.getClass())) {
						ConstraintViolationException cve = (ConstraintViolationException) e;
						for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
							msg = cv.getMessage();
							break;
						}
					}
				} else if (NestedServletException.class.isAssignableFrom(e.getClass())) {
					Throwable cause = ((NestedServletException) e).getRootCause();
					out: do {
						if (BusinessException.class.isAssignableFrom(cause.getClass())) {
							BusinessException ex = (BusinessException) cause;
							msg = ex.getMessage();
							break;
						} else if (ValidationException.class.isAssignableFrom(cause.getClass())) {
							if (ConstraintViolationException.class.isAssignableFrom(cause.getClass())) {
								ConstraintViolationException cve = (ConstraintViolationException) cause;
								for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
									msg = cv.getMessage();
									break out;
								}
							}
						} else if (ConstraintViolationException.class.isAssignableFrom(cause.getClass())) {
							ConstraintViolationException cve = (ConstraintViolationException) cause;
							for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
								msg = cv.getMessage();
								break out;
							}
						}
						cause = cause.getCause();
					} while (cause != null);

				}
				if (msg == null) {
					msg = "系统错误";
				}
				String result = mapper.writeValueAsString(new ResultBean<>(traceId, msg));
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(result);
				response.getWriter().flush();
				response.getWriter().close();
			}
		} finally {
			MDC.clear();
		}
	}

}
