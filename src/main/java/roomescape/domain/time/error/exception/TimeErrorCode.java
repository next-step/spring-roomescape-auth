package roomescape.domain.time.error.exception;

import org.springframework.http.HttpStatus;

public enum TimeErrorCode {
    INVALID_TIME_FORMAT_ERROR(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 형식의 시간이 입력되었습니다."),
    IS_BEFORE_ERROR(HttpStatus.BAD_REQUEST.value(), "지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");


    private final int status;
    private final String errorMessage;

    TimeErrorCode(int status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
