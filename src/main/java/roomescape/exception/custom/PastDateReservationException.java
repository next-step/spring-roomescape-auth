package roomescape.exception.custom;

import org.springframework.http.HttpStatus;

public class PastDateReservationException extends BusinessException {
    private static final String MESSAGE = "해당 시간에 이미 예약이 존재합니다.";

    public PastDateReservationException() {
        super(MESSAGE, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return super.getHttpStatus();
    }
}
