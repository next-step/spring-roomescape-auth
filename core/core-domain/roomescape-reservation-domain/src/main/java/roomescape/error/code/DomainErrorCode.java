package roomescape.error.code;

public enum DomainErrorCode {

    THIS_VALUE_NOT_TO_BE_EMPTY("이 값은 빈값이어서는 안됩니다."),
    THIS_VALUE_TO_BE_NULL("이 값은 NULL 이어서는 안됩니다."),
    CANNOT_CREATE_RESERVATION_FOR_PAST_TIME("지나간 시간에는 예약을 할 수 없습니다."),
    NOT_FOUND_RESERVATION("예약을 찾을 수 없습니다."),
    NOT_FOUND_RESERVATION_TIME("예약 시간 찾을 수 없습니다."),
    NOT_FOUND_THEME("테마를 찾을 수 없습니다."),
    NOT_FOUND_USER("사용자를 찾을 수 없습니다."),
    CANNOT_CREATE_USER_EXIST_EMAIL("이메일이 이미 존재하여 사용자를 생성할 수 없습니다.");

    private final String message;

    DomainErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
