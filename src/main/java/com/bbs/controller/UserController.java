package com.bbs.controller;

import com.bbs.common.ApiResponse;
import com.bbs.domain.User;
import com.bbs.exception.AccessDeniedException;
import com.bbs.exception.AccountNotAvailableException;
import com.bbs.exception.DatabaseException;
import com.bbs.exception.InvalidJwtException;
import com.bbs.service.JwtService;
import com.bbs.service.UserService;
import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 유저 컨트롤러 (사용자 정보, 로그인 등)
 */
@Slf4j
@Validated
@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "http://localhost:5173", exposedHeaders = {"Content-Disposition"})
@RequiredArgsConstructor
public class UserController {

	// TODO: 2023-03-21 패스워드 인코더 뢔퍼
	/**
	 * 유저 관련 서비스
	 */
	private final UserService userService;

	/**
	 * Jwt 인증 관련 서비스
	 */
	private final JwtService jwtService;

	/**
	 * 레디스 접근 객체
	 */
	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * 로그인 유저 정보 검증 후, 응답 헤더에 jwt 토큰 추가하여 성공 응답 반환
	 *
	 * @param user 유저 입력 아이디 및 비밀번호가 담긴 객체
	 * @param response 응답 객체
	 * @return 로그인 성공 응답
	 */
	@PostMapping("/users/login")
	public ApiResponse login(@RequestBody User user, HttpServletResponse response, HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null) {
			String accessToken = authorizationHeader.substring(7);
			if (redisTemplate.opsForValue().get("logoutList:" + accessToken).equals("logout")) {
				redisTemplate.opsForValue().getAndDelete("logoutList:" + accessToken);
			}
		}
		User userInput = user.toBuilder()
			.password(userService.encodePassword(user.getPassword()))
			.build();
		User authenticatedUser = userService.login(userInput);
		response.setHeader("Authorization", jwtService.generateAccessToken(authenticatedUser));
		return ApiResponse.success("로그인 성공");
	}

	/**
	 * 로그아웃
	 * "logoutList: {accessToken}" 키에 "logout" 값으로, 토큰 만료시간만큼 저장
	 *
	 * @param request 요청 객체
	 * @return
	 */
	@PostMapping("/users/logout")
	public ApiResponse logout(HttpServletRequest request) {
		String accessToken = request.getHeader("Authorization").substring(7);
		Claims claims = (Claims) request.getAttribute("claims");
		redisTemplate.opsForValue()
			.set("logoutList:" + accessToken, "logout",
				claims.getExpiration().getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		return ApiResponse.success("로그아웃 성공");
	}

	/**
	 * 유저 상세 정보 조회
	 *
	 * @param id 회원 번호
	 * @param request 요청 객체 (JWT 정보)
	 * @return
	 */
	@GetMapping("/users/{id}")
	public ApiResponse getUser(@PathVariable("id") @Positive Long id, HttpServletRequest request) {
		Claims claims = (Claims) request.getAttribute("claims");
		Long userId = claims.get("id",Long.class);
		if (userId != id) {
			throw new AccessDeniedException("접근 거부");
		}
		return ApiResponse.success(userService.getUserByAccount(claims.getSubject()));
	}

	/**
	 * 회원 등록
	 *
	 * @param user 사용자 입력값
	 * @return
	 */
	@PostMapping("/users")
	public ApiResponse registerUser(@RequestBody User user) {
		if (!userService.isAccountAvailable(user.getAccount())) {
			throw new AccountNotAvailableException("아이디 사용 불가");
		}
		User userDetail = user.toBuilder()
			.password(userService.encodePassword(user.getPassword()))
			.roleId(2L)
			.build();
		int result = userService.registerUser(userDetail);
		if (result != 1) {
			throw new DatabaseException("회원 등록 중 이상 발생");
		}
		return ApiResponse.success("등록 성공");
	}

	/**
	 * 아이디 중복 여부 검사
	 *
	 * @param account 사용자 입력 아이디
	 * @return
	 */
	@PostMapping("/users/account-availability")
	public ApiResponse checkAccountAvailability(@RequestParam("account") @NotBlank @Length(min = 3, max = 9) String account) {
		if (userService.isAccountAvailable(account)){
			return ApiResponse.success("아이디 사용 가능");
		}
		throw new AccountNotAvailableException("아이디 사용 불가");
	}

	/**
	 * 회원 정보 수정 (비밀번호 수정)
	 *
	 * @param id 회원 번호
	 * @param user 사용자 입력값
	 * @param request 요청 객체 (jwt 정보)
	 * @return
	 */
	@PutMapping("/users/{id}")
	public ApiResponse editUser(@PathVariable("id") @Positive Long id, @RequestBody User user, HttpServletRequest request) {
		Claims claims = (Claims) request.getAttribute("claims");
		Long userId = claims.get("id",Long.class);
		if (userId != id) {
			throw new AccessDeniedException("접근 거부");
		}
		User userInput = user.toBuilder()
			.id(id)
			.roleId(2L)
			.password(userService.encodePassword(user.getPassword()))
			.build();
		int result = userService.editUserPassword(userInput);
		if (result != 1) {
			throw new DatabaseException("회원 수정 처리 중 에러 발생");
		}
		return ApiResponse.success("비밀번호 변경 성공");
	}

	/**
	 * 회원 탈퇴 (삭제)
	 *
	 * @param id 회원 번호
	 * @param request 요청 객체 (jwt 정보)
	 * @return
	 */
	@DeleteMapping("/users/{id}")
	public ApiResponse deleteUser(@PathVariable("id") @Positive Long id, HttpServletRequest request) {
		Claims claims = (Claims) request.getAttribute("claims");
		Long userId = claims.get("id",Long.class);
		if (userId != id) {
			throw new AccessDeniedException("접근 거부");
		}
		int result = userService.deleteUserById(id);
		if (result != 1) {
			throw new DatabaseException("회원 탈퇴 처리 중 에러 발생");
		}
		return ApiResponse.success("탈퇴 성공");
	}
}
