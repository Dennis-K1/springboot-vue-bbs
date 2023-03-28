package com.bbs.exception;

public class DataNotFoundException extends CustomException{

	/**
	 * 404 에러
	 *
	 * @param message 메세지
	 */
	public DataNotFoundException(String message) {
		super(message, ErrorCode.DATA_NOT_FOUND);
	}

	/**
	 * 하위 예외에서 메세지 없이 생성할 경우
	 *
	 * @param errorCode 에러 정보
	 */
	public DataNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
