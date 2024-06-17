package roomescape.exception.custom;

import org.springframework.http.HttpStatus;

public class PastDateReservationException extends BusinessException {
    private static final String MESSAGE = "이미 지나간 날짜는 예약할 수 없습니다.";

    public PastDateReservationException() {
        super(MESSAGE, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return super.getHttpStatus();
    }
}
