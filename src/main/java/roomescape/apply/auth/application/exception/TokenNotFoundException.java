package roomescape.apply.auth.application.exception;

public class TokenNotFoundException extends IllegalArgumentException{

    public static final String DEFAULT_MESSAGE = "다시 로그인해주세요";

    public TokenNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
