package com.bbs.exception;

public class AccessDeniedException extends CustomException{

	/**
	 * 접근 불가 응답
	 *
	 */
	public AccessDeniedException() {
		super(ErrorCode.ACCESS_DENIED);
	}
}
