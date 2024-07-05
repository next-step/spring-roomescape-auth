package roomescape.auth.error.code;

public enum AuthErrorCode {

    NOT_MATCH_PASSWORD("패스워드가 일치하지 않습니다.");

    private final String message;

    AuthErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
