package roomescape.domain.reservation;

import roomescape.domain.reservation.validator.ReservationViewValidator;
import roomescape.domain.reservation.vo.ReservationDate;
import roomescape.domain.reservation.vo.ReservationId;
import roomescape.domain.reservation.vo.ReservationName;
import roomescape.domain.reservationtime.vo.ReservationTimeId;
import roomescape.domain.reservationtime.vo.ReservationTimeStartAt;
import roomescape.domain.theme.vo.ThemeId;
import roomescape.domain.theme.vo.ThemeName;

public class ReservationView {

    private final ReservationId reservationId;

    private final ReservationName reservationName;

    private final ReservationTimeId reservationTimeId;

    private final ReservationDate reservationDate;

    private final ReservationTimeStartAt reservationTimeStartAt;

    private final ThemeId themeId;

    private final ThemeName themeName;

    public ReservationView(
            ReservationId reservationId,
            ReservationName reservationName,
            ReservationTimeId reservationTimeId,
            ReservationDate reservationDate,
            ReservationTimeStartAt reservationTimeStartAt,
            ThemeId themeId,
            ThemeName themeName
    ) {
        ReservationViewValidator.validate(
                reservationId,
                reservationName,
                reservationTimeId,
                reservationDate,
                reservationTimeStartAt,
                themeId,
                themeName
        );
        this.reservationId = reservationId;
        this.reservationName = reservationName;
        this.reservationTimeId = reservationTimeId;
        this.reservationDate = reservationDate;
        this.reservationTimeStartAt = reservationTimeStartAt;
        this.themeId = themeId;
        this.themeName = themeName;
    }

    public ReservationId getReservationId() {
        return reservationId;
    }

    public ReservationName getReservationName() {
        return reservationName;
    }

    public String getFormatReservationDate(String pattern) {
        return this.reservationDate.getFormat(pattern);
    }

    public String getFormatReservationTimeStartAt(String pattern) {
        return this.reservationTimeStartAt.getFormat(pattern);
    }

    public ReservationTimeId getReservationTimeId() {
        return reservationTimeId;
    }

    public ThemeId getThemeId() {
        return themeId;
    }

    public ThemeName getThemeName() {
        return themeName;
    }
}
