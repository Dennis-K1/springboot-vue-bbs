package com.bbs.controller;

import static com.bbs.validation.BoardNameValidator.boardNameMap;

import com.bbs.common.ApiResponse;
import com.bbs.domain.Article;
import com.bbs.domain.File;
import com.bbs.domain.NestedReply;
import com.bbs.domain.PageParameters;
import com.bbs.domain.Reply;
import com.bbs.domain.User;
import com.bbs.exception.AccessDeniedException;
import com.bbs.exception.ArticleNotMatchingBoardException;
import com.bbs.service.BoardService;
import com.bbs.service.FileService;
import com.bbs.service.UserService;
import com.bbs.validation.BoardName;
import com.bbs.validation.NoAuthentication;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
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
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "*")
@RequiredArgsConstructor
public class BoardController {

	/**
	 * 게시판 관련 (게시글 , ...) 서비스
	 */
	private final BoardService boardService;

	/**
	 * 파일 관련 서비스
	 */
	private final FileService fileService;

	/**
	 * 유저 관련 서비스
	 */
	private final UserService userService;

	/**
	 * 홈(인덱스) 정보 조회
	 *
	 * @return 각 게시판별 최근 게시물 3개씩
	 */
	@NoAuthentication
	@GetMapping("index")
	public ApiResponse getIndex() {
		return ApiResponse.success(boardService.getIndex());
	}

	/**
	 * 댓글 정보 조회
	 * @param articleId 대상 게시글 번호
	 * @return 댓글
	 */
	@NoAuthentication
	@GetMapping("reply/{articleId}")
	public ApiResponse registerReply(@PathVariable("articleId") @Positive Long articleId) {
		return ApiResponse.success(boardService.getReplyList(articleId));
	}

	/**
	 * 댓글 등록
	 *
	 * @param reply 댓글 정보
	 * @return 등록된 댓글
	 */
	@PostMapping("reply")
	public ApiResponse inputReply(@RequestBody @Validated Reply reply, HttpServletRequest request) {

		Long userId = getUserIdByClaims(request);
		User user = userService.getUserById(userId);
		if (!Objects.equals(user.getAccount(), reply.getUser().getAccount())) {
			throw new AccessDeniedException("접근 거부: 잘못된 사용자");
		}

		boardService.inputReply(reply);
		if (reply.getReplyId() != null) {
			return ApiResponse.success(reply.toNestedReply());
		}
		return ApiResponse.success(reply);
	}

	/**
	 * request jwt 에서 유저 번호 추출
	 * @param request 요청 객체
	 * @return 유저 번호
	 */
	private  Long getUserIdByClaims(HttpServletRequest request) {
		Claims claims = (Claims) request.getAttribute("claims");
		Long userId = claims.get("id",Long.class);
		return userId;
	}

	/**
	 * 댓글 삭제
	 *
	 * @param replyId 삭제할 댓글 번호
	 * @param request 요청 객체 (추가 검증용 (사용자))
	 * @return 메세지
	 */
	@DeleteMapping("reply/{replyId}")
	public ApiResponse deleteReply(@PathVariable("replyId") @Positive Long replyId,
		HttpServletRequest request){

		Long userId = getUserIdByClaims(request);
		NestedReply reply = boardService.getNestedReplyById(replyId);
		if (!Objects.equals(reply.getUserId(), userId)) {
			throw new AccessDeniedException("접근 거부: 잘못된 사용자");
		}
		boardService.deleteReplyById(replyId);
		return ApiResponse.success("댓글 삭제 성공");
	}

	/**
	 * 게시글 목록 조회
	 *
	 * @param boardName 게시판명
	 * @param pageParameters 페이징 요소, 검색값
	 *
	 * @return 게시글 목록 (갤러리인 경우, 이미지 base64 인코딩 값 같이 전송)
	 * @throws IOException
	 */
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

		if (isGallery(boardId)) {
			for (Article article : articleList) {
				setImageIfExists(article);
			}
		}
		response.put("articleList", articleList);
		response.put("pageParameters", pageParameters);
		return ApiResponse.success(response);
	}

	/**
	 * 갤러리 게시판인지 확인
	 *
	 * @param boardId 게시판 번호
	 * @return boolean
	 */
	private boolean isGallery(Long boardId) {
		return boardId == 4L;
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

	/**
	 * 게시글 상세 조회
	 *
	 * @param boardName 게시판명
	 * @param articleId 게시글 번호
	 * @return 게시글 정보
	 * @throws IOException
	 */
	@NoAuthentication
	@GetMapping("{boards}/{articleId}")
	public ApiResponse getArticle(
		@PathVariable("boards") @BoardName String boardName,
		@PathVariable("articleId") @Positive Long articleId) throws IOException {

		Long boardId = boardNameMap.get(boardName);

		boardService.increaseArticleViewsById(articleId);
		Article article = boardService.getArticleById(articleId);
		if (!Objects.equals(article.getBoardId(), boardId)) {
			throw new ArticleNotMatchingBoardException(article.getId());
		}

		setImageIfExists(article);
		return ApiResponse.success(article);
	}

	/**
	 * 게시글 등록
	 *
	 * @param boardName 게시판명
	 * @param file 파일
	 * @param article 게시글 정보
	 * @param request 요청 객체
	 * @return 등록된 게시글 번호
	 * @throws IOException
	 */
	@PostMapping("{boards}")
	public ApiResponse inputArticle(
		@PathVariable("boards") String boardName,
		@Nullable @RequestParam("file") MultipartFile file,
		@Validated Article article,
		HttpServletRequest request
	)
		throws IOException {

		Long boardId = boardNameMap.get(boardName);

		//공지사항, 갤러리의 경우 유저 권한 조회하여 예외처리
		Long userId = getUserIdByClaims(request);
		User user = userService.getUserById(userId);
		if ((boardId == 1L || boardId == 4L) && user.getRoleId() == 2L) {
			throw new AccessDeniedException("접근 불가: 권한 없음");
		}
		article.setBoardId(boardId);
		article.setUser(user);

		boardService.inputArticle(article);

		if (file != null) {
			fileService.inputFile(file, article.getId());
		}
		return ApiResponse.success(article.getId());
	}


	/**
	 * 게시글 수정
	 *
	 * @param boardName 게시판명
	 * @param articleId 게시글 번호
	 * @param file 파일
	 * @param article 게시글 정보
	 * @param request 요청 객체
	 * @return 수정된 게시글 번호
	 * @throws IOException
	 */
	@PutMapping("{boards}/{articleId}")
	public ApiResponse editArticle(
		@PathVariable("boards") String boardName,
		@PathVariable("articleId") @Positive Long articleId,
		@Nullable @RequestParam("file") MultipartFile file,
		@Validated Article article,
		HttpServletRequest request
		) throws IOException {

		Long boardId = boardNameMap.get(boardName);
		Article previousArticle = boardService.getArticleById(articleId);
		if (!Objects.equals(previousArticle.getBoardId(), boardId)) {
			throw new ArticleNotMatchingBoardException(articleId);
		}

		//공지사항, 갤러리의 경우 유저 권한 조회하여 예외처리
		Long userId = getUserIdByClaims(request);
		User user = userService.getUserById(userId);
		if ((boardId == 1L || boardId == 4L) && user.getRoleId() == 2L) {
			throw new AccessDeniedException("접근 불가: 권한 없음");
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
		return ApiResponse.success(article.getId());
	}

	/**
	 * 게시글 삭제
	 *
	 * @param boardName 게시판명
	 * @param articleId 게시글 번호
	 * @param request 요청 객체
	 * @return 메세지
	 * @throws IOException
	 */
	@DeleteMapping("{boards}/{articleId}")
	public ApiResponse deleteArticle(
		@PathVariable("boards") String boardName,
		@PathVariable("articleId") @Positive Long articleId,
		HttpServletRequest request) throws IOException {

		Long boardId = boardNameMap.get(boardName);
		Article article = boardService.getArticleById(articleId);
		Long userId = getUserIdByClaims(request);

		if (!Objects.equals(article.getUser().getId(), userId)) {
			throw new AccessDeniedException("접근 거부: 잘못된 사용자");
		}
		if (!Objects.equals(article.getBoardId(), boardId)) {
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
