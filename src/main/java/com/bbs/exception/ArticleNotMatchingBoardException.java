package com.bbs.exception;

public class ArticleNotMatchingBoardException extends DataNotFoundException{

	/**
	 * 요청 게시판 경로에 속하는 게시글 번호가 아닌 경우
	 */
	public ArticleNotMatchingBoardException() {
		super(ErrorCode.DATA_NOT_FOUND);
	}
}
