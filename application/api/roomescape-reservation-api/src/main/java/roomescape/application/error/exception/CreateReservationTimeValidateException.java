package roomescape.application.error.exception;

import roomescape.application.error.code.ApplicationErrorCode;
import roomescape.application.error.key.ApplicationErrorKey;
import roomescape.application.error.key.ApplicationErrorKeys;
import roomescape.domain.reservationtime.vo.ReservationTimeStartAt;

import static roomescape.application.error.code.ApplicationErrorCode.CANNOT_CREATE_EXIST_RESERVATION_TIME;

public class CreateReservationTimeValidateException extends ApplicationException {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm";

    private static final String ERROR_KEY_NAME_DATE = "date";
    private static final String ERROR_KEY_NAME_START_AT = "startAt";

    public CreateReservationTimeValidateException(ApplicationErrorCode code, ApplicationErrorKeys keys) {
        super(code, keys);
    }

    public static CreateReservationTimeValidateException existSameStartAt(ReservationTimeStartAt startAt) {
        return new CreateReservationTimeValidateException(
                CANNOT_CREATE_EXIST_RESERVATION_TIME,
                ApplicationErrorKeys.of(
                        new ApplicationErrorKey(
                                ERROR_KEY_NAME_START_AT,
                                startAt.getFormat(TIME_FORMAT)
                        )
                )
        );
    }
}
