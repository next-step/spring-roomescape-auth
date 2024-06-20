package roomescape.utils;

import roomescape.domain.reservation.error.exception.ReservationErrorCode;
import roomescape.domain.reservation.error.exception.ReservationException;
import roomescape.domain.theme.error.exception.ThemeErrorCode;
import roomescape.domain.theme.error.exception.ThemeException;
import roomescape.domain.time.error.exception.TimeErrorCode;
import roomescape.domain.time.error.exception.TimeException;

public class FormatCheckUtil {

    private static final String TIME_FORMAT = "^(?:[01]\\d|2[0-3]):[0-5]\\d$";
    private static final String RESERVATION_NAME_FORMAT = "^[A-Za-z가-힣\\s]{2,20}$";
    private static final String RESERVATION_DATE_FORMAT = "^(?:(?:(?:(?:19|20)[0-9]{2})-(?:(?:0[13578]|1[02])-(?:0[1-9]|[12][0-9]|3[01])|(?:0[469]|11)-(?:0[1-9]|[12][0-9]|30)|02-(?:0[1-9]|1[0-9]|2[0-8])))|(?:((?:19|20)(?:[02468][048]|[13579][26]))-02-29))$";
    private static final String THEME_NAME_FORMAT = "^[A-Za-z0-9가-힣\\s]{8,20}$";
    private static final String THEME_DESCRIPTION_FORMAT = "^[A-Za-z0-9가-힣\\s]{1,200}$";
    private static final String THEME_THUMBNAIL_FORMAT = "^(https?:\\/\\/)?([a-zA-Z0-9.-]+)(:[0-9]{1,5})?(\\/[^\\s]*)?$";

    public static void startAtFormatCheck(String startAt) {
        if (!startAt.matches(TIME_FORMAT)) {
            throw new TimeException(TimeErrorCode.INVALID_TIME_FORMAT_ERROR);
        }
    }

    public static void reservationNameFormatCheck(String name) {
        if (!name.matches(RESERVATION_NAME_FORMAT)) {
            throw new ReservationException(ReservationErrorCode.INVALID_RESERVATION_NAME_FORMAT_ERROR);
        }
    }

    public static void reservationDateFormatCheck(String date) {
        if (!date.matches(RESERVATION_DATE_FORMAT)) {
            throw new ReservationException(ReservationErrorCode.INVALID_RESERVATION_DATE_FORMAT_ERROR);
        }
    }

    public static void themeNameFormatCheck(String name) {
        if (!name.matches(THEME_NAME_FORMAT)) {
            throw new ThemeException(ThemeErrorCode.INVALID_THEME_NAME_FORMAT_ERROR);
        }
    }

    public static void themeDescriptionCheck(String description) {
        if (!description.matches(THEME_DESCRIPTION_FORMAT)) {
            throw new ThemeException(ThemeErrorCode.INVALID_THEME_DESCRIPTION_FORMAT_ERROR);
        }
    }

    public static void themeThumbnailFormatCheck(String thumbnail) {
        if (!thumbnail.matches(THEME_THUMBNAIL_FORMAT)) {
            throw new ThemeException(ThemeErrorCode.INVALID_THEME_THUMBNAIL_FORMAT_ERROR);
        }
    }
}
