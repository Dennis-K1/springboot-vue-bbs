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

	/**
	 * jwt 인증 인터셉터
	 */
	private final JwtInterceptor jwtInterceptor;

	/**
	 * 인터셉터 인증 불필요한 컨트롤러 경로의 경우 어노테이션으로 제외 처리
	 *
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor)
			.addPathPatterns("/**");
	}
}
