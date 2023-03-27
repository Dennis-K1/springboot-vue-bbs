package com.bbs.service;

import com.bbs.domain.Article;
import com.bbs.domain.NestedReply;
import com.bbs.domain.PageParameters;
import com.bbs.domain.Reply;
import com.bbs.domain.User;
import com.bbs.exception.ArticleNotFoundException;
import com.bbs.mapper.BoardMapper;
import com.bbs.mapper.UserMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	/**
	 * 유저 매퍼 (사용자)
	 */
	private final UserMapper userMapper;


	/**
	 * 홈 화면 정보 각 게시판별 최근 게시글 3개씩
	 *
	 * @return Map<String, List < Article>> indexList
	 */
	public Map<String, List<Article>> getIndex() {
		Map<String, List<Article>> indexList = new HashMap<>();
		indexList.put("top3RecentArticlesByEachBoard",
			boardMapper.getTop3RecentArticlesByEachBoard());
		return indexList;
	}

	/**
	 * 검색 조건 기반 게시글 목록 조회
	 *
	 * @param pageParameters 검색 조건
	 * @return 게시글 목록
	 */
	public List<Article> getArticleList(PageParameters pageParameters) {
		return boardMapper.getArticleList(pageParameters);
	}

	/**
	 * 검색 조건 기반 총 게시글 갯수 조회
	 *
	 * @param pageParameters 검색 조건
	 * @return 갯수
	 */
	public int getNumberOfArticlesBySearch(PageParameters pageParameters) {
		return boardMapper.getNumberOfArticlesBySearch(pageParameters);
	}

	/**
	 * 대상 게시글 삭제 (삭제 처리, 삭제일 업데이트)
	 *
	 * @param id 대상 게시글 번호
	 * @return 수행 결과
	 */
	public int deleteArticleById(Long id) {
		if (boardMapper.deleteArticleById(id) == 1) {
			return boardMapper.updateDateDeleted(id);
		}
		Article article = boardMapper.getArticleById(id);
		return userMapper.decreaseArticleCountById(article.getUser().getId());
	}

	/**
	 * 게시글 등록 (회원 아이디 조회, 게시글 등록) -- 등록 시 유저 count_article (게시 게시글 수) 증가
	 *
	 * @param article 게시글 정보 객체
	 * @return 수행 결과
	 */
	public int inputArticle(Article article) {

		User user = userMapper.getUserByAccount(article.getUser().getAccount());
		userMapper.increaseArticleCountById(user.getId());

		article.setUser(user);
		return boardMapper.inputArticle(article);
	}

	/**
	 * 게시글 조회
	 *
	 * @param id 대상 게시글 번호
	 * @return 게시글
	 */
	public Article getArticleById(Long id) {
		Article article = boardMapper.getArticleById(id);
		if (article == null) {
			throw new ArticleNotFoundException(id);
		}
		return article;
	}

	/**
	 * 게시글 조회수 증가
	 *
	 * @param id 대상 게시글
	 * @return 수행 결과
	 */
	public int increaseArticleViewsById(Long id) {
		return boardMapper.increaseArticleViewsById(id);
	}

	/**
	 * 게시글 수정
	 *
	 * @param article 수정할 정보 및 대상 게시글 번호가 든 객체
	 * @return 수행 결과
	 */
	public int editArticle(Article article) {
		return boardMapper.editArticle(article);
	}

	/**
	 * 대상 사용자가 작성한 게시글 목록 반환
	 *
	 * @param userId 대상 사용자 번호
	 * @return 게시글 목록
	 */
	public List<Article> getArticleListByUser(Long userId) {
		return boardMapper.getArticleListByUser(userId);
	}

	/**
	 * 답글(댓글) 등록
	 *
	 * @param reply 답글(댓글) 정보 객체
	 * @return 수행 결과
	 */
	public int inputReply(Reply reply) {
		User user = userMapper.getUserByAccount(reply.getUser().getAccount());
		reply.setUser(user);
		return boardMapper.inputReply(reply);
	}

	public List<Reply> getReplyList(Long articleId) {
		return boardMapper.getReplyListById(articleId);
	}


	/**
	 * 대상 답글(댓글) 삭제
	 *
	 * @param replyId 대상 답글(댓글) 번호
	 * @return 수행 결과
	 */
	public int deleteReplyById(Long replyId) {
		return boardMapper.deleteReplyById(replyId);
	}


	/**
	 * 대상 답글(댓글) 조회
	 *
	 * @param replyId 대상 답글(댓글) 번호
	 * @return Reply
	 */
	public NestedReply getNestedReplyById(Long replyId) {
		return boardMapper.getNestedReplyById(replyId);
	}
}
