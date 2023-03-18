package com.bbs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


/**
 * 에러의 status, code, message 를 정의하고 있는 enum
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

	DATA_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "C003", "요청한 정보가 존재하지 않습니다."),
	INVALID_PATH_VALUE(HttpStatus.BAD_REQUEST.value(),"C002","잘못된 경로값"),
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(),"C001","잘못된 입력값"),

	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(),"S001","서버에러");

	private final int status;
	private final String code;
	private final String message;

}
