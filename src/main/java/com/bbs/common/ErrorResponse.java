package com.bbs.common;

import com.bbs.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Api 응답 실패에 대한 정보
 */
@Getter
@NoArgsConstructor
public class ErrorResponse {

	/**
	 * HTTP status 번호 (헤더와 동일)
	 */
	private int status;

	/**
	 * 에러에 대한 커스텀 코드
	 */
	private String code;

	/**
	 * 에러에 대한 메세지
	 */
	private String message;

	/**
	 * 클라이언트 입력/요청값에 오류가 있을 경우 (BindingResults 에러), 해당 값의 필드,값,에러 메세지를 담은 정보 목록
	 * (없을 시 빈 배열 반환)
	 */
	private List<FieldError> errors;

	/**
	 * BindingResults 오류가 있을 경우 해당 내용을 'errors' 배열에 담아 반환
	 *
	 * @param errorCode 에러의 status, code, message 를 정의하고 있는 enum
	 * @param errors BindingResults 에서 가져온 입력/요청값 오류에 대한 정보
	 */
	private ErrorResponse(ErrorCode errorCode, List<FieldError> errors) {
		this.message = errorCode.getMessage();
		this.status = errorCode.getStatus();
		this.errors = errors;
		this.code = errorCode.getCode();
	}

	/**
	 * 일반 에러 응답
	 *
	 * @param errorCode 에러의 status, code, message 를 정의하고 있는 enum
	 */
	private ErrorResponse(ErrorCode errorCode) {
		this.message = errorCode.getMessage();
		this.status = errorCode.getStatus();
		this.code = errorCode.getCode();
		this.errors = new ArrayList<>();
	}

	/**
	 * ErrorCode 와 BindingResult 를 받아 api 응답 포맷에 맞게 ErrorResponse 생성하여 반환
	 *
	 * @param errorCode 에러의 status, code, message 를 정의하고 있는 enum
	 * @param bindingResult 입력/요청값 검증 오류에 대한 정보
	 * @return ErrorResponse
	 */
	public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
		return new ErrorResponse(errorCode, FieldError.of(bindingResult));
	}

	/**
	 * ErrorCode 와 constraintViolationSe 를 받아 api 응답 포맷에 맞게 ErrorResponse 생성하여 반환
	 *
	 * @param errorCode 에러의 status, code, message 를 정의하고 있는 enum
	 * @param constraintViolationSet 제약 검사 실패 정보
	 * @return ErrorResponse
	 */
	public static ErrorResponse of(ErrorCode errorCode, Set<ConstraintViolation<?>> constraintViolationSet) {
		return new ErrorResponse(errorCode, FieldError.of(constraintViolationSet));
	}

	/**
	 * ErrorCode 와 MethodArgumentTypeMismatchException 를 받아 api 응답 포맷에 맞게 ErrorResponse 생성하여 반환
	 *
	 * @param errorCode 에러의 status, code, message 를 정의하고 있는 enum
	 * @param exception 타입 변환 실패 정보
	 * @return ErrorResponse
	 */
	public static ErrorResponse of(ErrorCode errorCode, MethodArgumentTypeMismatchException exception) {
		return new ErrorResponse(errorCode, FieldError.of(exception));
	}


	/**
	 * api 응답 포맷에 맞게 ErrorResponse 생성하여 반환 (일반 에러 응답, "errors" 는 빈 배열로 반환)
	 *
	 * @param errorCode 에러의 status, code, message 를 정의하고 있는 enum
	 * @return ErrorResponse
	 */
	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(errorCode);
	}


	/**
	 * 스프링 BindingResults 의 FieldError 의 정보를 api 공통 응답 포맷의 이름에 맞추어 반환하기 위해 재정의한 객체
	 */
	@Getter
	@NoArgsConstructor
	public static class FieldError {

		/**
		 * 검증 오류가 발생한 필드명
		 */
		private String field;

		/**
		 * 입력된 값 (null 일 경우 "")
		 */
		private String value;

		/**
		 * 에러 메세지
		 */
		private String reason;

		private FieldError(String field, String value, String reason) {
			this.field = field;
			this.value = value;
			this.reason = reason;
		}

		/**
		 * BindingResult 객체에서 스프링 FieldError 객체를 가져와 재정의된 ErrorResponses.FieldError 필드에 맞추어 반환
		 *
		 * @param bindingResult 입력/요청값 검증 오류에 대한 정보
		 * @return ErrorResponse.FieldError 목록
		 */
		private static List<FieldError> of(BindingResult bindingResult) {
			List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
			return fieldErrors.stream()
				.map(error -> new FieldError(
					error.getField(),
					error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
					error.getDefaultMessage()))
				.collect(Collectors.toList());
		}

		/**
		 * constraintViolationSet 객체에서 실패 정보를 가져와 재정의된 ErrorResponses.FieldError 필드에 맞추어 반환
		 *
		 * @param constraintViolationSet 입력/요청값 검증 오류에 대한 정보
		 * @return ErrorResponse.FieldError 목록
		 */
		private static List<FieldError> of(Set<ConstraintViolation<?>> constraintViolationSet) {
			return constraintViolationSet.stream()
				.map(error -> new FieldError(
					error.getPropertyPath().toString().split("\\.")[1],
					error.getInvalidValue() == null ? "" : error.getInvalidValue().toString(),
					error.getMessage()))
				.collect(Collectors.toList());
		}

		/**
		 * MethodArgumentTypeMismatchException 의 정보를 재정의된 ErrorResponses.FieldError 필드에 맞추어 반환
		 *
		 * @param exception 입력/요청값 검증 오류에 대한 정보
		 * @return ErrorResponse.FieldError 목록
		 */
		private static List<FieldError> of(MethodArgumentTypeMismatchException exception) {
			List fieldErrorList = new ArrayList<>();
			fieldErrorList.add(new FieldError(exception.getName(),
				(String)exception.getValue(),
				exception.getMessage()));
			return fieldErrorList;
		}
	}
}
