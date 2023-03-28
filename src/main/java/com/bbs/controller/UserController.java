package com.bbs.controller;

import com.bbs.common.ApiResponse;
import com.bbs.domain.Article;
import com.bbs.domain.User;
import com.bbs.exception.AccessDeniedException;
import com.bbs.exception.AccountNotAvailableException;
import com.bbs.exception.DatabaseException;
import com.bbs.exception.InvalidJwtException;
import com.bbs.service.BoardService;
import com.bbs.service.JwtService;
import com.bbs.service.UserService;
import com.bbs.validation.NoAuthentication;
import io.jsonwebtoken.Claims;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "*")
@RequiredArgsConstructor
public class UserController {

	/**
	 * 유저 관련 서비스
	 */
	private final UserService userService;

	/**
	 * 게시글 관련 서비스
	 */
	private final BoardService boardService;

	/**
	 * Jwt 인증 관련 서비스
	 */
	private final JwtService jwtService;

	/**
	 * 레디스 접근 객체
	 */
	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * 유저 프로필 정보
	 *
	 * @param request 요청 객체 (JWT 정보)
	 * @return 데이터
	 */
	@GetMapping("/users/profile")
	public ApiResponse getUserProfile(HttpServletRequest request) {
		Long userId = getUserIdByClaims(request);

		User user = userService.getUserById(userId);
		List<Article> articleList = boardService.getArticleListByUser(userId);

		Map<String, Object> response = new HashMap<>();
		response.put("user", user);
		response.put("articleList", articleList);
		return ApiResponse.success(response);
	}
	/**
	 * 아이디 중복 여부 검사
	 *
	 * @param account 사용자 입력 아이디
	 * @return
	 */
	@NoAuthentication
	@PostMapping("/users/account-availability")
	public ApiResponse checkAccountAvailability(@RequestParam("account") @NotBlank @Length(min = 3, max = 9) String account) {
		if (userService.isAccountAvailable(account)){
			return ApiResponse.success("아이디 사용 가능");
		}
		throw new AccountNotAvailableException();
	}

	/**
	 * 로그인 유저 정보 검증 후, 응답 헤더에 jwt 토큰 추가하여 성공 응답 반환
	 * 로그아웃시 프론트에서 jwt 정보 등 삭제, 따라서 logoutList 에 있는 jwt 로 접근 시도하는 경우 에러 처리
	 *
	 * @param user 유저 입력 아이디 및 비밀번호가 담긴 객체
	 * @param response 응답 객체
	 * @return 로그인 성공 응답
	 */
	@NoAuthentication
	@PostMapping("/users/login")
	public ApiResponse login(@RequestBody User user, HttpServletResponse response, HttpServletRequest request) {
		// jwt 가 있는지 확인후, 있다면 이미 로그아웃된 토큰인지 확인
		String authorizationHeader = request.getHeader("Authorization");
		if (isValidHeader(authorizationHeader)) {
			String accessToken = authorizationHeader.substring(7);
			jwtService.validateToken(accessToken);
			String status = (String) redisTemplate.opsForValue().get("logoutList:" + accessToken);
			if (isLoggedOut(status)) {
				throw new InvalidJwtException();
			}
		}
		User userInput = user.toBuilder()
			.password(userService.encodePassword(user.getPassword()))
			.build();
		User authenticatedUser = userService.login(userInput);

		response.setHeader("Authorization", jwtService.generateAccessToken(authenticatedUser));

		userService.increaseVisitCount(authenticatedUser);
		userService.updateLastLogin(authenticatedUser);
		return ApiResponse.success("로그인 성공");
	}

	/**
	 * 로그아웃
	 * "logoutList: {accessToken}" 키에 "logout" 값으로, 토큰 만료시간만큼 저장
	 *
	 * @param request 요청 객체
	 * @return 메세지
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
	 * @return 유저 정보
	 */
	@GetMapping("/users/{id}")
	public ApiResponse getUser(@PathVariable("id") @Positive Long id, HttpServletRequest request) {
		Long userId = getUserIdByClaims(request);
		if (!Objects.equals(userId, id)) {
			throw new AccessDeniedException();
		}
		return ApiResponse.success(userService.getUserById(userId));
	}


	/**
	 * 회원 등록
	 *
	 * @param user 사용자 입력값
	 * @return 메세지
	 */
	@NoAuthentication
	@PostMapping("/users")
	public ApiResponse registerUser(@RequestBody @Validated User user) {
		if (!userService.isAccountAvailable(user.getAccount())) {
			throw new AccountNotAvailableException();
		}
		User userDetail = user.toBuilder()
			.password(userService.encodePassword(user.getPassword()))
			.roleId(2L)
			.build();
		int result = userService.registerUser(userDetail);
		if (result != 1) {
			throw new DatabaseException();
		}
		return ApiResponse.success("등록 성공");
	}


	/**
	 * 회원 정보 수정 (비밀번호 수정)
	 *
	 * @param id 회원 번호
	 * @param user 사용자 입력값
	 * @param request 요청 객체 (jwt 정보)
	 * @return 메세지
	 */
	@PutMapping("/users/{id}")
	public ApiResponse editUser(@PathVariable("id") @Positive Long id, @RequestBody User user, HttpServletRequest request) {
		Long userId = getUserIdByClaims(request);
		if (!Objects.equals(userId, id)) {
			throw new AccessDeniedException();
		}
		User userInput = user.toBuilder()
			.id(id)
			.roleId(2L)
			.password(userService.encodePassword(user.getPassword()))
			.build();
		int result = userService.editUserPassword(userInput);
		if (result != 1) {
			throw new DatabaseException();
		}
		return ApiResponse.success("비밀번호 변경 성공");
	}

	/**
	 * 회원 탈퇴 (삭제)
	 *
	 * @param id 회원 번호
	 * @param request 요청 객체 (jwt 정보)
	 * @return 메세지
	 */
	@DeleteMapping("/users/{id}")
	public ApiResponse deleteUser(@PathVariable("id") @Positive Long id, HttpServletRequest request) {
		Long userId = getUserIdByClaims(request);
		if (!Objects.equals(userId, id)) {
			throw new AccessDeniedException();
		}
		int result = userService.deleteUserById(id);
		if (result != 1) {
			throw new DatabaseException();
		}
		return ApiResponse.success("탈퇴 성공");
	}

	/**
	 * request jwt 에서 유저 번호 추출
	 * @param request 요청 객체
	 * @return 유저 번호
	 */
	private Long getUserIdByClaims(HttpServletRequest request) {
		Claims claims = (Claims) request.getAttribute("claims");
		return claims.get("id",Long.class);
	}

	/**
	 * logoutList 에 올라가 있는 jwt 인지 확인
	 * @param status 상태값
	 * @return boolean
	 */
	private boolean isLoggedOut(String status) {
		if (Objects.equals(null, status)) {
			return false;
		}
		return status.equals("logout");
	}

	/**
	 * jwt 헤더 유효성 검증
	 * @param authorizationHeader jwt 담긴 헤더
	 * @return boolean
	 */
	private boolean isValidHeader(String authorizationHeader) {
		if (!authorizationHeader.startsWith("Bearer")) {
			throw new InvalidJwtException();
		}
		if ("null".equals(authorizationHeader)) {
			throw new InvalidJwtException();
		}
		return true;
	}
}
