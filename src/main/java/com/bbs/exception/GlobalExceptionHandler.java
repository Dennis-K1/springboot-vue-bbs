package com.bbs.exception;

import com.bbs.common.ApiResponse;
import com.bbs.common.ErrorResponse;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
	 * @return ApiResponse.error(errorResponse)
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	protected ApiResponse handleConstraintViolationException(ConstraintViolationException exception) {
		log.error("ConstraintViolationException", exception.getConstraintViolations());
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_PATH_VALUE, exception.getConstraintViolations());
		return ApiResponse.error(errorResponse);
	}

	/**
	 * 클라이언트에서 보내온 Body 가 JSON 으로 변환 실패 시 발생
	 * \ @RequestBody, @RequestParam 으로 값을 받을 때 @Validated, @Valid 에러 시 발생 (검증 오류)
	 *
	 * @param exception MethodArgumentNotValidException
	 * @return ApiResponse.error(errorResponse)
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ApiResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		log.error("MethodArgumentNotValidException", exception);
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, exception.getBindingResult());
		return ApiResponse.error(errorResponse);
	}

	/**
	 * \ @ModelAttribute 로 값을 받을 때 바인딩 에러 시 발생
	 *
	 * @param exception BindException
	 * @return ApiResponse.error(errorResponse)
	 */
	@ExceptionHandler({BindException.class})
	protected ApiResponse handleBindException(BindException exception) {
		log.error("BindException", exception);
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, exception.getBindingResult());
		return ApiResponse.error(errorResponse);
	}

	/**
	 * 자체 정의 에러 발생 시 처리
	 *
	 * @param exception CustomException 을 상속하는 모든 자체 정의 에러
	 * @return ApiResponse.error(errorResponse)
	 */
	@ExceptionHandler({CustomException.class})
	protected ApiResponse handleBusinessException(CustomException exception) {
		ErrorCode errorCode = exception.getErrorCode();
		ErrorResponse errorResponse = ErrorResponse.of(errorCode);
		return ApiResponse.error(errorResponse);
	}

	/**
	 * 상기 정의된 에러 외에 모든 에러에 대한 처리
	 *
	 * @param exception Exception
	 * @return ApiResponse.error(errorResponse)
	 */
	@ExceptionHandler({Exception.class})
	protected ApiResponse handleCustomApiException(Exception exception) {
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
		return ApiResponse.error(errorResponse);
	}
}
