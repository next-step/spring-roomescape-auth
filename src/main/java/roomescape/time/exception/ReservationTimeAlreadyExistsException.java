package roomescape.time.exception;

public class ReservationTimeAlreadyExistsException extends RuntimeException {

    public ReservationTimeAlreadyExistsException() {
        super("이미 존재하는 시간입니다.");
    }
}
