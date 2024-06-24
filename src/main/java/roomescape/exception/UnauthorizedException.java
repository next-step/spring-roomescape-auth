package roomescape.exception;

public class UnauthorizedException extends RuntimeException {
    private UnauthorizedException(String message) {
        super(message);
    }

    public static UnauthorizedException of(String message) {
        return new UnauthorizedException(message);
    }
}
