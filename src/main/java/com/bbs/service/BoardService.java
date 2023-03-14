package com.bbs.service;

import com.bbs.mapper.BoardMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 게시판 관련 서비스 (게시글 조회, 등록, 삭제, 수정 및 기타 관련 로직)
 */
@Service
@RequiredArgsConstructor
public class BoardService {

	/**
	 * 게시판 매퍼 (게시글, 댓글)
	 */
	private final BoardMapper boardMapper;


	public List<String> getArticleList() {
		return boardMapper.getArticleList();
	}
}
