package com.bbs.exception;

import lombok.Getter;

/**
 * 자체 정의 예외
 */
@Getter
public class CustomException extends RuntimeException{

	/**
	 * 에러의 status, code, message 를 정의하고 있는 enum
	 */
	private ErrorCode errorCode;
}