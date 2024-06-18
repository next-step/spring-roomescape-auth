package roomescape.time.exception;

public class CannotDeleteReserveTimeException extends RuntimeException {

    public CannotDeleteReserveTimeException() {
        super("예약된 시간은 삭제할 수 없습니다.");
    }
}
