package com.bbs.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;

/**
 * 게시판 경로명 유효성 검증을 위한 어노테이션
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BoardNameValidator.class)
public @interface BoardName {

	/**
	 * @return api 실패 응답 "errors" 에 표시될 message ("reason")
	 */
	String message() default "잘못된 경로명입니다.";
	Class[] groups() default {};
	Class[] payload() default {};
}
