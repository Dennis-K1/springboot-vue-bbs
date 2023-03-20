package com.bbs.interceptor;

import com.bbs.exception.AccessDeniedException;
import com.bbs.exception.InvalidJwtException;
import com.bbs.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt 인증 인터셉터
 */
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

	/**
	 * jwt 인증 관련 서비스 "claims" 에 관련 정보 저장 (추가 검증용)
	 */
	private final JwtService jwtService;

	/**
	 * 요청 헤더 확인하여 jwt 인증
	 *
	 * @param request  current HTTP request
	 * @param response current HTTP response
	 * @param handler  chosen handler to execute, for type and/or instance evaluation
	 * @return
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new AccessDeniedException("로그인 필요");
		}

		String token = authorizationHeader.substring(7); // remove "Bearer "
		Claims claims = jwtService.parseToken(token);

		if (!jwtService.validateToken(token)) {
			throw new InvalidJwtException();
		}

		request.setAttribute("claims", claims);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
		ModelAndView modelAndView) {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
		Object handler, Exception ex) {
	}
}
