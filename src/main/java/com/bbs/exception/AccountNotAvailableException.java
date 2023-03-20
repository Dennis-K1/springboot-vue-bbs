package com.bbs.exception;

public class AccountNotAvailableException extends CustomException{

	public AccountNotAvailableException(String message) {
		super(message, ErrorCode.ACCOUNT_NOT_AVAILABLE);
	}
}
