package com.bbs.exception;

public class DataNotFoundException extends CustomException{

	public DataNotFoundException(String message) {
		super(message, ErrorCode.DATA_NOT_FOUND);
	}
}
