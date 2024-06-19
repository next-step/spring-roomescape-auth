package roomescape.theme.exception;

public class ThemeHasReservationException extends RuntimeException {

    public ThemeHasReservationException() {
        super("테마에 예약이 있어 삭제할 수 없습니다.");
    }
}
