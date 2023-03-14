package com.bbs.controller;

import com.bbs.service.BoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 게시판 컨트롤러
 */
@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class BoardController {

	/**
	 * 게시판 관련 (게시글 , ...) 서비스
	 */
	private final BoardService boardService;

	@GetMapping("{boards}")
	public List<String> getArticle(@PathVariable("boards") String articleId) {
		return boardService.getArticleList();
	}

}
