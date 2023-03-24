package com.bbs.controller;

import static com.bbs.validation.BoardNameValidator.boardNameMap;

import com.bbs.common.ApiResponse;
import com.bbs.domain.Article;
import com.bbs.domain.File;
import com.bbs.domain.NestedReply;
import com.bbs.domain.PageParameters;
import com.bbs.domain.Reply;
import com.bbs.domain.User;
import com.bbs.exception.ArticleNotMatchingBoardException;
import com.bbs.service.BoardService;
import com.bbs.service.FileService;
import com.bbs.validation.BoardName;
import com.bbs.validation.NoAuthentication;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 게시판 컨트롤러
 */
@Validated
@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class BoardController {

	// TODO: 2023-03-19 로그인, 회원 기능 만들어서 게시글 삭제 수정 등 사용자 확인 해야 함
	// TODO: 2023-03-19 파일이나 다른 입출력 관련 예외 처리 더 해야 함

	/**
	 * 게시판 관련 (게시글 , ...) 서비스
	 */
	private final BoardService boardService;

	/**
	 * 파일 관련 서비스
	 */
	private final FileService fileService;

	@NoAuthentication
	@GetMapping("index")
	public ApiResponse getIndex() {
		return ApiResponse.success(boardService.getIndex());
	}

	@GetMapping("reply/{articleId}")
	public ApiResponse registerReply(@PathVariable("articleId") @Positive Long articleId) {
		return ApiResponse.success(boardService.getReplyList(articleId));
	}

	@PostMapping("reply")
	public ApiResponse registerReply(@RequestBody @Validated Reply reply) {
		boardService.inputReply(reply);
		if (reply.getReplyId() != null) {
			return ApiResponse.success(reply.toNestedReply());
		}
		return ApiResponse.success(reply);
	}

	@NoAuthentication
	@GetMapping("{boards}")
	public ApiResponse getArticleList(
		@PathVariable("boards")
		@BoardName String boardName,
		@Validated PageParameters pageParameters) throws IOException {
		Long boardId = boardNameMap.get(boardName);
		pageParameters.setBoardId(boardId);
		int numberOfArticles = boardService.getNumberOfArticlesBySearch(pageParameters);
		pageParameters.setPaginationElements(numberOfArticles);
		Map<String, Object> response = new HashMap<>();
		List<Article> articleList = boardService.getArticleList(pageParameters);

		if (boardId == 4L) {
			for (Article article : articleList) {
				setImageIfExists(article);
			}
		}

		response.put("articleList", articleList);
		response.put("pageParameters", pageParameters);
		return ApiResponse.success(response);
	}

	/**
	 * 대상 게시글에 등록된 파일(이미지) 이 있을 경우 정보 (Base64 인코딩 이미지) 입력
	 *
	 * @param article
	 */
	private void setImageIfExists(Article article) throws IOException {
		File file = fileService.getFileByArticleId(article.getId());
		if (!Objects.equals(file, null)) {
			article.setImage(fileService.getEncodedImageFromFile(file));
		}
	}

	@NoAuthentication
	@GetMapping("{boards}/{articleId}")
	public ApiResponse getArticle(
		@PathVariable("boards") @BoardName String boardName,
		@PathVariable("articleId") @Positive Long articleId) throws IOException {
		Long boardId = boardNameMap.get(boardName);
		Article article = boardService.getArticleById(articleId);
		if (article.getBoardId() != boardId) {
			throw new ArticleNotMatchingBoardException(article.getId());
		}
		setImageIfExists(article);
		return ApiResponse.success(article);
	}

	@PostMapping("{boards}")
	public ApiResponse inputArticle(
		@PathVariable("boards") String boardName,
		@Validated Article article,
		@RequestParam MultipartFile file)
		throws IOException {
		Long boardId = boardNameMap.get(boardName);
		article.setBoardId(boardId);

		User user = User.builder()
			.account("zxc")
			.build();

		article.setUser(user);

		boardService.inputArticle(article);
		fileService.inputFile(file, article.getId());

		return ApiResponse.success("게시글 등록 성공");
	}


	@PutMapping("{boards}/{articleId}")
	public ApiResponse editArticle(
		@PathVariable("boards") String boardName,
		@PathVariable("articleId") @Positive Long articleId,
		@Validated Article article,
		@RequestParam @Nullable MultipartFile file) throws IOException {

		Long boardId = boardNameMap.get(boardName);
		Article previousArticle = boardService.getArticleById(articleId);
		if (previousArticle.getBoardId() != boardId) {
			throw new ArticleNotMatchingBoardException(articleId);
		}
		article.setId(articleId);
		boardService.editArticle(article);

		File previousFile = fileService.getFileByArticleId(articleId);
		if (previousFile != null) {
			fileService.deleteFile(previousFile);
		}
		if (file != null) {
			fileService.inputFile(file, article.getId());
		}
		return ApiResponse.success("게시글 수정 성공");
	}

	@DeleteMapping("{boards}/{articleId}")
	public ApiResponse deleteArticle(
		@PathVariable("boards") String boardName,
		@PathVariable("articleId") @Positive Long articleId) throws IOException {

		Long boardId = boardNameMap.get(boardName);
		Article article = boardService.getArticleById(articleId);
		if (article.getBoardId() != boardId) {
			throw new ArticleNotMatchingBoardException(articleId);
		}
		boardService.deleteArticleById(articleId);
		File previousFile = fileService.getFileByArticleId(articleId);
		if (previousFile != null) {
			fileService.deleteFile(previousFile);
			fileService.deleteFolder(previousFile);
		}
		return ApiResponse.success("게시글 삭제 성공");
	}
}
