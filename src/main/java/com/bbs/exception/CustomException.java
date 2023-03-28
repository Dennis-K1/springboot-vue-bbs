package com.bbs.exception;

import lombok.Getter;

/**
 * 자체 정의 예외 부모 클라스
 */
@Getter
public class CustomException extends RuntimeException{

	/**
	 * 에러의 status, code, message 를 정의하고 있는 enum
	 */
	private ErrorCode errorCode;

	/**
	 * 에러에 직접 메세지를 입력하는 경우
	 *
	 * @param message 메세지
	 * @param errorCode 에러 정보
	 */
	public CustomException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * 에러에 직접 입력하는 메세지 없는 경우
	 *
	 * @param errorCode 에러 정보
	 */
	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
