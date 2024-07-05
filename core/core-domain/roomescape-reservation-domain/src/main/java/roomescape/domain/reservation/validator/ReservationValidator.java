package roomescape.domain.reservation.validator;

import roomescape.domain.reservation.vo.ReservationId;
import roomescape.domain.reservation.vo.ReservationName;
import roomescape.domain.reservationtime.vo.ReservationTimeId;
import roomescape.domain.theme.vo.ThemeId;
import roomescape.error.exception.DomainValidateException;

import java.util.Objects;

public final class ReservationValidator {

    private ReservationValidator() {
        throw new UnsupportedOperationException(this.getClass().getName() + "의 인스턴스는 생성되어서 안됩니다.");
    }

    public static void validate(
            ReservationId id,
            ReservationName name,
            ReservationTimeId timeId,
            ThemeId themeId
    ) {
        validateNotNull(id, name, timeId, themeId);
    }

    private static void validateNotNull(
            ReservationId id,
            ReservationName name,
            ReservationTimeId timeId,
            ThemeId themeId
    ) {
        if (Objects.isNull(id)) {
            throw DomainValidateException.notToBeNull(ReservationId.class);
        }
        if (Objects.isNull(name)) {
            throw DomainValidateException.notToBeNull(ReservationName.class);
        }
        if (Objects.isNull(timeId)) {
            throw DomainValidateException.notToBeNull(ReservationTimeId.class);
        }
        if (Objects.isNull(themeId)) {
            throw DomainValidateException.notToBeNull(ThemeId.class);
        }
    }
}
