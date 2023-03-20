package com.bbs.exception;

public class InvalidJwtException extends AccessDeniedException{

	/**
	 * JWT 정보 오류
	 */
	public InvalidJwtException() {
		super(String.format("유효하지 않은 토큰"));
	}
}
