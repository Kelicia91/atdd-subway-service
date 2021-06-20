package nextstep.subway.common.error;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.application.MemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String NOT_AUTHORIZED = "권한이 없습니다.";
    private static final String MEMBER_NOT_FOUND = "사용자가 존재하지 않습니다.";
    private static final String NOT_VALID_INPUT = "올바르지 않은 입력입니다.";
    private static final String SERVER_ERROR = "에러가 발생했습니다.";

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthorizationException.class)
    public ErrorResponse handleAuthorization() {
        return new ErrorResponse(NOT_AUTHORIZED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MemberNotFoundException.class)
    public ErrorResponse handleMemberNotFound() {
        return new ErrorResponse(MEMBER_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgument() {
        return new ErrorResponse(NOT_VALID_INPUT);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResponse handleIllegalState() {
        return new ErrorResponse(SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorResponse handleException() {
        return new ErrorResponse(SERVER_ERROR);
    }
}
