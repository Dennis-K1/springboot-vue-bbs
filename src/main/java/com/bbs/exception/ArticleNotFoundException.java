package com.bbs.exception;

public class ArticleNotFoundException extends DataNotFoundException{

	public ArticleNotFoundException(Long articleId) {
		super(String.format("%s번 게시글은 존재하지 않음", articleId));
	}
}
