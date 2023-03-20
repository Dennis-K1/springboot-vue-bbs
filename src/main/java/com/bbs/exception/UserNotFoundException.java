package com.bbs.exception;

public class UserNotFoundException extends DataNotFoundException{

	/**
	 * 유저 정보 불일치 시
	 */
	public UserNotFoundException() {
		super(String.format("아이디나 비밀번호가 틀렸습니다."));
	}
}
