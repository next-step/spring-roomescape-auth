package roomescape.exception.custom;

import org.springframework.http.HttpStatus;

public class InvalidReservationTimeException extends BusinessException {
    private static final String MESSAGE = "존재하지 않는 예약시간입니다.";

    public InvalidReservationTimeException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return super.getHttpStatus();
    }
}
