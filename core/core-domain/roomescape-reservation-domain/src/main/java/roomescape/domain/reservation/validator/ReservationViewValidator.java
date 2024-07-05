package roomescape.domain.reservation.validator;

import roomescape.domain.reservation.vo.ReservationDate;
import roomescape.domain.reservation.vo.ReservationId;
import roomescape.domain.reservation.vo.ReservationName;
import roomescape.domain.reservationtime.vo.ReservationTimeId;
import roomescape.domain.reservationtime.vo.ReservationTimeStartAt;
import roomescape.domain.theme.vo.ThemeId;
import roomescape.domain.theme.vo.ThemeName;
import roomescape.error.exception.DomainValidateException;

import java.util.Objects;

public final class ReservationViewValidator {

    private ReservationViewValidator() {
        throw new UnsupportedOperationException(this.getClass().getName() + "의 인스턴스는 생성되어서 안됩니다.");
    }

    public static void validate(
            ReservationId reservationId,
            ReservationName reservationName,
            ReservationTimeId reservationTimeId,
            ReservationDate reservationDate,
            ReservationTimeStartAt reservationTimeStartAt,
            ThemeId themeId,
            ThemeName themeName
    ) {
        validateNotNull(
                reservationId,
                reservationName,
                reservationTimeId,
                reservationDate,
                reservationTimeStartAt,
                themeId,
                themeName
        );
    }

    private static void validateNotNull(
            ReservationId reservationId,
            ReservationName reservationName,
            ReservationTimeId reservationTimeId,
            ReservationDate reservationDate,
            ReservationTimeStartAt reservationTimeStartAt,
            ThemeId themeId,
            ThemeName themeName
    ) {
        if (Objects.isNull(reservationId)) {
            throw DomainValidateException.notToBeNull(ReservationId.class);
        }
        if (Objects.isNull(reservationName)) {
            throw DomainValidateException.notToBeNull(ReservationName.class);
        }
        if (Objects.isNull(reservationTimeId)) {
            throw DomainValidateException.notToBeNull(ReservationTimeId.class);
        }
        if (Objects.isNull(reservationDate)) {
            throw DomainValidateException.notToBeNull(ReservationDate.class);
        }
        if (Objects.isNull(reservationTimeStartAt)) {
            throw DomainValidateException.notToBeNull(ReservationTimeStartAt.class);
        }
        if (Objects.isNull(themeId)) {
            throw DomainValidateException.notToBeNull(ThemeId.class);
        }
        if (Objects.isNull(themeName)) {
            throw DomainValidateException.notToBeNull(ThemeName.class);
        }
    }
}
