package com.bbs.exception;

import com.bbs.common.ErrorResponse;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 컨트롤러 에러 처리를 위한 핸들러
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 제약 조건 위배 시 발생 (유효성 검사 실패)
	 *
	 * @param exception ConstraintViolationException
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity handleConstraintViolationException(ConstraintViolationException exception) {
		log.error("ConstraintViolationException", exception.getConstraintViolations());
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_PATH_VALUE, exception.getConstraintViolations());
		return ResponseEntity
			.status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
			.body(errorResponse);
	}

	/**
	 * 클라이언트에서 보내온 Body 가 JSON 으로 변환 실패 시 발생
	 * \ @RequestBody, @RequestParam 으로 값을 받을 때 @Validated, @Valid 에러 시 발생 (검증 오류)
	 *
	 * @param exception MethodArgumentNotValidException
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		log.error("MethodArgumentNotValidException : {}", exception);
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, exception.getBindingResult());
		return ResponseEntity
			.status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
			.body(errorResponse);
	}

	/**
	 * 쿼리 스트링 타입 변환 실패 시 발생
	 *
	 * @param exception MethodArgumentTypeMismatchException
	 * @return
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
		log.error("MethodArgumentTypeMismatchException", exception);
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, exception);
		return ResponseEntity
			.status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
			.body(errorResponse);
	}

	/**
	 * \ @ModelAttribute 로 값을 받을 때 바인딩 에러 시 발생
	 *
	 * @param exception BindException
	 * @return
	 */
	@ExceptionHandler({BindException.class})
	protected ResponseEntity handleBindException(BindException exception) {
		log.error("BindException", exception);
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, exception.getBindingResult());
		return ResponseEntity
			.status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
			.body(errorResponse);
	}

	/**
	 * 자체 정의 에러 발생 시 처리
	 *
	 * @param exception CustomException 을 상속하는 모든 자체 정의 에러
	 * @return
	 */
	@ExceptionHandler({CustomException.class})
	protected ResponseEntity handleCustomApiException(CustomException exception) {
		log.error("CustomException", exception);
		ErrorCode errorCode = exception.getErrorCode();
		ErrorResponse errorResponse = ErrorResponse.of(errorCode);
		return ResponseEntity
			.status(errorCode.getStatus())
			.body(errorResponse);
	}

	/**
	 * 상기 정의된 에러 외에 모든 에러에 대한 처리
	 *
	 * @param exception Exception
	 * @return
	 */
	@ExceptionHandler({Exception.class})
	protected ResponseEntity handleException(Exception exception) {
		log.error("Exception", exception);
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
		return ResponseEntity
			.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
			.body(errorResponse);
	}
}
