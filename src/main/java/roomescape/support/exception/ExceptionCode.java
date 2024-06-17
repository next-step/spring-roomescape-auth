package roomescape.support.exception;

public enum ExceptionCode {
    DUPLICATED_ERROR(1001),
    WRONG_TOKEN_ERROR(1002)

    ;

    private final int value;

    ExceptionCode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
