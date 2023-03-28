package com.bbs.exception;

public class ArticleNotFoundException extends DataNotFoundException{

	/**
	 * 게시글 존재하지 않을 시
	 */
	public ArticleNotFoundException() {
		super(ErrorCode.DATA_NOT_FOUND);
	}
}
