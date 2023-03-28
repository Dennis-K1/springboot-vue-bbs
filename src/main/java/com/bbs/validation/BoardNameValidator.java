package com.bbs.validation;

import java.util.HashMap;
import java.util.Map;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 게시판 경로명 유효성 검증 Validator
 */
@Slf4j
public class BoardNameValidator implements ConstraintValidator<BoardName, String> {
	public static  Map<String, Long> boardNameMap;

	static {
		boardNameMap = new HashMap<>();
		boardNameMap.put("notice", 1L);
		boardNameMap.put("community", 2L);
		boardNameMap.put("inquiry", 3L);
		boardNameMap.put("gallery", 4L);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (boardNameMap.get(value) != null) {
			return true;
		}
		return false;
	}
}
