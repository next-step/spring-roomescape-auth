package roomescape.exception.custom;

import org.springframework.http.HttpStatus;

public class InvalidReservationThemeException extends BusinessException {
    private static final String MESSAGE = "존재하지 않는 예약테마입니다.";

    public InvalidReservationThemeException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return super.getHttpStatus();
    }
}
