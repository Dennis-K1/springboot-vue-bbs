package com.bbs.exception;

public class UserNotFoundException extends DataNotFoundException{

	/**
	 * 유저 정보 불일치 시
	 */
	public UserNotFoundException() {
		super(ErrorCode.USER_NOT_FOUND);
	}
}
