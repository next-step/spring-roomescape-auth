package roomescape.reservation.exception;

public class PastDateReservationException extends RuntimeException {

    public PastDateReservationException() {
        super("이미 지난 시간은 예약할 수 없습니다.");
    }
}
