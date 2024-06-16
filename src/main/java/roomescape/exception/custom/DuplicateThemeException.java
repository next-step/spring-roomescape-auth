package roomescape.exception.custom;

import org.springframework.http.HttpStatus;

public class DuplicateThemeException extends BusinessException {
    private static final String MESSAGE = "동일한 테마이름이 존재합니다.";

    public DuplicateThemeException() {
        super(MESSAGE, HttpStatus.CONFLICT);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return super.getHttpStatus();
    }
}
