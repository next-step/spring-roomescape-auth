package roomescape.exception.custom;

import org.springframework.http.HttpStatus;

public class ReservationThemeConflictException extends BusinessException {
    private static final String MESSAGE = "예약 테마를 사용중인 예약이 존재합니다. 예약 삭제 후 다시 시도해주세요.";

    public ReservationThemeConflictException() {
        super(MESSAGE, HttpStatus.CONFLICT);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return super.getHttpStatus();
    }
}
