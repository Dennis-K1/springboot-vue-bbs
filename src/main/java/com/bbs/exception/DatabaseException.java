package com.bbs.exception;

public class DatabaseException extends CustomException{

	/**
	 * 데이터베이스 오류
	 */
	public DatabaseException() {
		super(ErrorCode.INTERNAL_SERVER_ERROR);
	}
}
