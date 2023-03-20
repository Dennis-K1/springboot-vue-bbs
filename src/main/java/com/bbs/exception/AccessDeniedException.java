package com.bbs.exception;

public class AccessDeniedException extends CustomException{

	/**
	 * 접근 불가 응답
	 *
	 * @param message 메세지
	 */
	public AccessDeniedException(String message) {
		super(message, ErrorCode.ACCESS_DENIED);
	}
}
