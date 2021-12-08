package nextstep.subway;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.common.EntityNotFoundException;
import nextstep.subway.common.ErrorResponse;

@RestControllerAdvice
public class RestExceptionHandler {

	public static final String ERROR_MESSAGE_ILLEGAL_ARGUMENTS = "잘못된 요청 값입니다.";
	public static final String ERROR_MESSAGE_INTERNAL = "요청 처리 중 문제가 발생했습니다.";

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handle(DataIntegrityViolationException e) {
		return ResponseEntity.badRequest()
			.body(ErrorResponse.of(ERROR_MESSAGE_ILLEGAL_ARGUMENTS));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handle(IllegalArgumentException e) {
		return ResponseEntity.badRequest()
			.body(ErrorResponse.of(e.getMessage()));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handle(EntityNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(ErrorResponse.of(e.getMessage()));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handle(RuntimeException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ErrorResponse.of(ERROR_MESSAGE_INTERNAL));
	}
}
