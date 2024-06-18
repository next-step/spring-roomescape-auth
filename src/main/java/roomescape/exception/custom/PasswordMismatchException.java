package roomescape.exception.custom;

import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends BusinessException {

    private static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

    public PasswordMismatchException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return super.getHttpStatus();
    }
}
