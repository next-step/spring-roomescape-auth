package roomescape.error.exception;

import roomescape.domain.reservationtime.vo.ReservationTimeId;
import roomescape.domain.theme.vo.ThemeId;
import roomescape.domain.user.vo.UserEmail;
import roomescape.domain.user.vo.UserId;
import roomescape.error.code.DomainErrorCode;
import roomescape.error.key.DomainErrorKey;
import roomescape.error.key.DomainErrorKeys;

import static roomescape.error.code.DomainErrorCode.*;

public class NotFoundDomainException extends DomainException {

    private static final String RESERVATION_TIME_ID = "timeId";
    private static final String USER_ID = "userId";
    private static final String THEME_ID = "themeId";
    private static final String USER_EMAIL = "userEmail";

    public NotFoundDomainException(DomainErrorCode code, DomainErrorKeys keys) {
        super(code, keys);
    }

    public static NotFoundDomainException notFoundReservationTime(ReservationTimeId id) {
        return new NotFoundDomainException(
                NOT_FOUND_RESERVATION_TIME,
                DomainErrorKeys.of(
                        new DomainErrorKey(RESERVATION_TIME_ID, id.id().toString())
                )
        );
    }

    public static NotFoundDomainException notFoundTheme(ThemeId id) {
        return new NotFoundDomainException(
                NOT_FOUND_THEME,
                DomainErrorKeys.of(
                        new DomainErrorKey(THEME_ID, id.id().toString())
                )
        );
    }

    public static NotFoundDomainException notFoundUser(UserEmail email) {
        return new NotFoundDomainException(
                NOT_FOUND_USER,
                DomainErrorKeys.of(
                        new DomainErrorKey(USER_EMAIL, email.email())
                )
        );
    }

    public static NotFoundDomainException notFoundUser(UserId id) {
        return new NotFoundDomainException(
                NOT_FOUND_USER,
                DomainErrorKeys.of(
                        new DomainErrorKey(USER_ID, id.id().toString())
                )
        );
    }
}
