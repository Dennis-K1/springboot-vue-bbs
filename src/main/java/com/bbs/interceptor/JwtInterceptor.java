package com.bbs.interceptor;

import com.bbs.exception.AccessDeniedException;
import com.bbs.exception.InvalidJwtException;
import com.bbs.service.JwtService;
import com.bbs.validation.NoAuthentication;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
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
	 * 레디스 접근 객체
	 */
	private final RedisTemplate<String, Object> redisTemplate;

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
		// Handle preflight request
		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
			response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
			response.setHeader("Access-Control-Max-Age", "3600");
			return true;
		}

		// NoAuthentication 어노테이션 붙은 메소드 제외
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if (handlerMethod.getMethod().isAnnotationPresent(NoAuthentication.class)) {
				return true;
			}
		}

		// 토큰값 검증
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new AccessDeniedException();
		}

		String accessToken = authorizationHeader.substring(7); // remove "Bearer "

		// 이미 로그아웃 처리된 토근인지 확인
		String logoutFlag = (String) redisTemplate.opsForValue().get("logoutList:" + accessToken);
		if (logoutFlag != null && "logout".equals(logoutFlag) ) {
			throw new InvalidJwtException();
		}

		Claims claims = jwtService.parseToken(accessToken);

		if (!jwtService.validateToken(accessToken)) {
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
