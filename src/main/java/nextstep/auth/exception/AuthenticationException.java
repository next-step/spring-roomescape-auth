package nextstep.auth.exception;

public class AuthenticationException extends RuntimeException {

    private final Status status;

    public AuthenticationException(Status status) {
        this.status = status;
    }

    public enum Status {
        WRONG_USERNAME, WRONG_PASSWORD, NO_AUTHORITY
    }

    public Status getStatus() {
        return status;
    }
}
