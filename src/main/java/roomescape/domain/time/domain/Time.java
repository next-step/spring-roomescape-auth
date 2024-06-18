package roomescape.domain.time.domain;

import roomescape.domain.theme.domain.Theme;
import roomescape.domain.time.error.exception.ErrorCode;
import roomescape.domain.time.error.exception.TimeException;



public class Time {

    private static final String REGEX_FORMAT = "^(?:[01]\\d|2[0-3]):[0-5]\\d$";

    private Long id;
    private String date;
    private Theme theme;
    private String startAt;

    public Time(Long id, String date, Theme theme, String startAt) {
        checkFormats(startAt);
        this.id = id;
        this.startAt = startAt;
        this.theme = theme;
        this.date = date;
    }

    private void checkFormats(String startAt) {
        if (!startAt.matches(REGEX_FORMAT)) {
            throw new TimeException(ErrorCode.INVALID_TIME_FORMAT_ERROR);
        }
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }

    public String getDate() {
        return date;
    }

    public Theme getTheme() {
        return theme;
    }
}
