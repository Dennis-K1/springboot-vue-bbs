package com.bbs.service;

import com.bbs.domain.User;
import com.bbs.exception.UserNotFoundException;
import com.bbs.mapper.UserMapper;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 사용자 관련 서비스
 */
@Service
@RequiredArgsConstructor
public class UserService {

	/**
	 * 유저 매퍼 (사용자)
	 */
	private final UserMapper userMapper;

	/**
	 * 유저 등록
	 *
	 * @param user 유저가 입력한 정보로 생성한 사용자 객체
	 * @return
	 */
	public int registerUser(User user) {
		return userMapper.registerUser(user);
	}


	/**
	 * 로그인 유저가 입력한 아이디, 비밀번호 검증
	 *
	 * @param userInput 사용자 입력 아이디 및 비밀번호가 담긴 객체
	 * @return 통과시 사용자 정보 반환
	 */
	public User login(User userInput) {
		User user = userMapper.getUserByAccount(userInput.getAccount());
		if (user == null) {
			throw new UserNotFoundException();
		}
		if (!userInput.getPassword().equals(user.getPassword())) {
			throw new UserNotFoundException();
		}
		return user;
	}

	/**
	 * 대상 클라이언트 삭제 처리 (삭제 처리, 삭제일 갱신)
	 *
	 * @param id 클라이언트 번호
	 * @return
	 */
	public int deleteUserById(Long id) {
		if (userMapper.deleteUserById(id) == 1) {
			return userMapper.updateDateDeleted(id);
		}
		return 0;
	}

	/**
	 * 유저 접속시 방문횟수 증가
	 *
	 * @param user 유저 정보 객체
	 * @return DB 수행 결과
	 */
	public int increaseVisitCount(User user) {
		return userMapper.increaseVisitCount(user);
	}

	/**
	 * 유저 접속시 마지막 접속시간 업데이트
	 *
	 * @param user 유저 정보 객체
	 * @return DB 수행 결과
	 */
	public int updateLastLogin(User user) {
		return userMapper.updateLastLogin(user);
	}

	/**
	 * 유저 아이디로 유저 정보 조회
	 *
	 * @param account 유저 아이디
	 * @return
	 */
	public User getUserByAccount(String account) {
		return userMapper.getUserByAccount(account);
	}


	/**
	 * 아이디 사용 가능 여부 확인
	 *
	 * @param account 대상 아이디
	 */
	public boolean isAccountAvailable(String account) {
		if (getUserByAccount(account) != null) {
			return false;
		}
		return true;
	}

	/**
	 * 패스워드 인코딩 (SHA-512)
	 *
	 * @param password 유저 입력 패스워드
	 * @return 인코딩된 패스워드
	 */
	public String encodePassword(String password) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("인코딩 실패");
		}

		byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
		messageDigest.update(bytes);

		return Base64
			.getEncoder()
			.encodeToString(messageDigest.digest());
	}

	/**
	 * 사용자 비밀번호 변경
	 *
	 * @param user 사용자 정보
	 * @return
	 */
	public int editUserPassword(User user) {
		return userMapper.editUserPassword(user);
	}

	/**
	 * PK, 사용자 번호로 사용자 정보 조회
	 *
	 * @param userId 대상 사용자 번호
	 * @return User
	 */
	public User getUserById(Long userId) {
		return userMapper.getUserById(userId);
	}

}
