package com.bbs.domain;

import java.sql.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 대댓글 정보 객체
 */
@Getter
@NoArgsConstructor
public class NestedReply {

	/**
	 * reply 테이블 PK
	 */
	@Setter
	private Long id;

	/**
	 * 종속 게시글 PK
	 */
	private Long articleId;

	/**
	 * 작성자 PK
	 */
	private Long userId;

	/**
	 * 작성자 아이디
	 */
	private String account;

	/**
	 * 댓글 내용
	 */
	private String content;

	/**
	 * 댓글 등록일
	 */
	private Date dateRegistered;

	/**
	 * 댓글 삭제 여부
	 */
	private Byte replyDeleted;

	@Builder
	public NestedReply(Long id, String account, String content, Date dateRegistered, Byte replyDeleted) {
		this.id = id;
		this.account = account;
		this.content = content;
		this.dateRegistered = dateRegistered;
		this.replyDeleted = replyDeleted;
	}
}
