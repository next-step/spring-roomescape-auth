package roomescape.exception.custom;

import org.springframework.http.HttpStatus;

public class UnauthorizedAccessException extends BusinessException {

    private static final String MESSAGE = "권한이 존재하지 않습니다.";

    public UnauthorizedAccessException() {
        super(MESSAGE, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return super.getHttpStatus();
    }
}
