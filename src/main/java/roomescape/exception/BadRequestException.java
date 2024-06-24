package roomescape.exception;

public class BadRequestException extends RuntimeException {
    private BadRequestException(String message) {
        super(message);
    }

    public static BadRequestException of(String message) {
        return new BadRequestException(message);
    }
}
