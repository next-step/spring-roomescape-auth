package roomescape.exception.custom;

import org.springframework.http.HttpStatus;

public class ExistingReservationException extends BusinessException {
    private static final String MESSAGE = "이미 지나간 날짜는 예약할 수 없습니다.";

    public ExistingReservationException() {
        super(MESSAGE, HttpStatus.CONFLICT);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return super.getHttpStatus();
    }
}
