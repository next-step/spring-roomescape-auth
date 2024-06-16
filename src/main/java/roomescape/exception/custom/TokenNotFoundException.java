package roomescape.exception.custom;

import org.springframework.http.HttpStatus;

public class TokenNotFoundException extends BusinessException {

    private static final String MESSAGE = "토큰이 존재하지 않습니다.";

    public TokenNotFoundException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return super.getHttpStatus();
    }
}
