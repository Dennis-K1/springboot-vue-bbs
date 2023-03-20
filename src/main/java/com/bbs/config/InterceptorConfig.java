package com.bbs.config;

import com.bbs.interceptor.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 인터셉터 설정
 */
@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

	private final JwtInterceptor jwtInterceptor;
	private static String[] EXCLUDE_PATH_PATTERNS = {"/api/v1/users/account-availability", "/api/v1/users/login", "/api/v1/users"};

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor)
			.addPathPatterns("/**") // apply to all requests
			.excludePathPatterns(EXCLUDE_PATH_PATTERNS);
		// TODO: 2023-03-20 @NoAuth 같은 커스텀 어노테이션 생성하여 excludePathPatterns 대체
	}

}
