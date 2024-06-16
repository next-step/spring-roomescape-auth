package roomescape.exception.custom;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends BusinessException {

    private static final String MESSAGE = "유효하지 않은 토큰입니다.";

    public InvalidTokenException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return super.getHttpStatus();
    }
}
