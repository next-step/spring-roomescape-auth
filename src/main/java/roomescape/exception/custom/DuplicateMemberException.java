package roomescape.exception.custom;

import org.springframework.http.HttpStatus;

public class DuplicateMemberException extends BusinessException {
    private static final String MESSAGE = "동일한 아이디가 존재합니다.";

    public DuplicateMemberException() {
        super(MESSAGE, HttpStatus.CONFLICT);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return super.getHttpStatus();
    }
}
