package roomescape.exception;

public class NotFoundException extends RuntimeException {
    private NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException of(String message) {
        return new NotFoundException(message);
    }
}
