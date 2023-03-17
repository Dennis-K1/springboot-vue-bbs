package com.bbs.common;

import lombok.Builder;
import lombok.Getter;

/**
 * 공통 Api 응답 포맷
 *
 * @param <T> 성공 응답시 객체 타입
 */
@Getter
@Builder
public class ApiResponse<T> {

	/**
	 * Api 호출 실행 결과
	 */
	private boolean success;

	/**
	 * Api 응답 데이터
	 */
	private T data;

	/**
	 * Api 응답 실패에 대한 설명
	 */
	private ErrorResponse error;

	/**
	 * Api 요청 성공 시 응답 객체
	 *
	 * @param
	 * @return
	 */
	public static <T> ApiResponse success(T data) {
		return new ApiResponseBuilder<>()
			.success(true)
			.data(data)
			.build();
	}

	/**
	 * Api 요청 실패 (에러) 시 응답 객체
	 *
	 * @param
	 * @return
	 */
	public static ApiResponse error(ErrorResponse error) {
		return new ApiResponseBuilder<>()
			.success(false)
			.error(error)
			.build();
	}

}
