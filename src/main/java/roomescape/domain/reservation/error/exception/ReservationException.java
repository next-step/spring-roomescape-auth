package roomescape.domain.reservation.error.exception;

import roomescape.global.error.exception.UserException;

public class ReservationException extends UserException {

    public ReservationException(ReservationErrorCode reservationErrorCode) {
        super(reservationErrorCode.getStatus(), reservationErrorCode.getErrorMessage());
    }

    @Override
    public int getStatus() {
        return super.getStatus();
    }
}
