package com.bbs.exception;

public class AccountNotAvailableException extends CustomException{

	/**
	 * 아이디 사용 불가
	 */
	public AccountNotAvailableException() {
		super(ErrorCode.ACCOUNT_NOT_AVAILABLE);
	}
}
