package com.bbs.controller;

import com.bbs.common.ApiResponse;
import com.bbs.domain.User;
import com.bbs.service.JwtService;
import com.bbs.service.UserService;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 유저 컨트롤러 (사용자 정보, 로그인 등)
 */
@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "http://localhost:5173", exposedHeaders = {"Content-Disposition"})
@RequiredArgsConstructor
public class UserController {

	/**
	 * 유저 관련 서비스
	 */
	private final UserService userService;

	/**
	 * Jwt 인증 관련 서비스
	 */
	private final JwtService jwtService;

	/**
	 * 로그인 유저 정보 검증 후, 응답 헤더에 jwt 토큰 추가하여 성공 응답 반환
	 *
	 * @param account 유저 입력 아이디
	 * @param password 유저 입력 패스워드 (해쉬 변환)
	 * @param response 응답 객체
	 * @return 로그인 성공 응답
	 */
	@PostMapping("login")
	public ApiResponse login(String account, String password, HttpServletResponse response) {
		password = userService.encodePassword(password);
		User authenticatedUser = userService.login(account, password);
		response.setHeader("Authorization", jwtService.generateToken(authenticatedUser));

		return ApiResponse.success("로그인 성공");
	}
}
