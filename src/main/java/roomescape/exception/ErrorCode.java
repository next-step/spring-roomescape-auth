package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    THEME_NOT_FOUND(HttpStatus.NOT_FOUND),

    INVALID_RESERVATION_TIME(HttpStatus.BAD_REQUEST),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND),

    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_ADMIN(HttpStatus.UNAUTHORIZED),

    INVALID_THEME_NAME(HttpStatus.BAD_REQUEST),

    INVALID_INPUT(HttpStatus.BAD_REQUEST),

    COOKIE_NOT_FOUND(HttpStatus.NOT_FOUND);

    private final HttpStatus status;

    ErrorCode(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
