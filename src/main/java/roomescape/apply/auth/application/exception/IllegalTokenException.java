package roomescape.apply.auth.application.exception;

public class IllegalTokenException extends IllegalArgumentException {

    private static final String DEFAULT_MESSAGE = "잘못된 토큰입니다. 재 로그인 해 주세요";

    public IllegalTokenException() {
        super(DEFAULT_MESSAGE);
    }

    public IllegalTokenException(String message) {
        super(message);
    }

    public IllegalTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
