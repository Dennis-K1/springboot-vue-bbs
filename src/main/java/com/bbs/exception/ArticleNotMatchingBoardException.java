package com.bbs.exception;

public class ArticleNotMatchingBoardException extends DataNotFoundException{

	public ArticleNotMatchingBoardException(Long articleId) {
		super(String.format("%s번 게시글은 요청받은 게시판에 속하지 않음", articleId));
	}
}
