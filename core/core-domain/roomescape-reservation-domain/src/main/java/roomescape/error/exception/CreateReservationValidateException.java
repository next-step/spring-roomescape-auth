package roomescape.error.exception;

import roomescape.domain.reservationtime.ReservationTime;
import roomescape.error.code.DomainErrorCode;
import roomescape.error.key.DomainErrorKey;
import roomescape.error.key.DomainErrorKeys;

import static roomescape.error.code.DomainErrorCode.CANNOT_CREATE_RESERVATION_FOR_PAST_TIME;

public class CreateReservationValidateException extends DomainException {

    private static final String ERROR_KEY_NAME_TIME_ID = "timeId";

    public CreateReservationValidateException(DomainErrorCode code, DomainErrorKeys keys) {
        super(code, keys);
    }

    public static CreateReservationValidateException pastTime(ReservationTime time) {
        return new CreateReservationValidateException(
                CANNOT_CREATE_RESERVATION_FOR_PAST_TIME,
                DomainErrorKeys.of(
                        new DomainErrorKey(
                                ERROR_KEY_NAME_TIME_ID,
                                time.getId().toString()
                        )
                )
        );
    }
}
