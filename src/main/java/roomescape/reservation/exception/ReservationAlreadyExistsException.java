package roomescape.reservation.exception;

public class ReservationAlreadyExistsException extends RuntimeException {

    public ReservationAlreadyExistsException() {
        super("이미 예약된 시간입니다.");
    }
}
