package roomescape.exception.custom;

import org.springframework.http.HttpStatus;

public class ExistingReservationException extends BusinessException {
    private static final String MESSAGE = "동일한 예약이 존재합니다.";

    public ExistingReservationException() {
        super(MESSAGE, HttpStatus.CONFLICT);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return super.getHttpStatus();
    }
}
