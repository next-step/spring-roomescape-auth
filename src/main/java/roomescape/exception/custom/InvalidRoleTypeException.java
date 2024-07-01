package roomescape.exception.custom;

import org.springframework.http.HttpStatus;

public class InvalidRoleTypeException extends BusinessException {
    private static final String MESSAGE = "존재하지 않는 권한 유형입니다.";

    public InvalidRoleTypeException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return super.getHttpStatus();
    }
}
