package com.bbs.exception;

public class DatabaseException extends CustomException{

	public DatabaseException(String message) {
		super(message, ErrorCode.INTERNAL_SERVER_ERROR);
	}
}
