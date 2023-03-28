package com.bbs.exception;

public class InvalidJwtException extends CustomException{

	/**
	 * JWT 정보 오류
	 */
	public InvalidJwtException() {
		super(ErrorCode.INVALID_JWT);
	}
}
