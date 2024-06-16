package roomescape.exception.custom;

import org.springframework.http.HttpStatus;

public class DuplicateTimeException extends BusinessException {
    private static final String MESSAGE = "동일한 시간이 존재합니다.";

    public DuplicateTimeException() {
        super(MESSAGE, HttpStatus.CONFLICT);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return super.getHttpStatus();
    }
}
