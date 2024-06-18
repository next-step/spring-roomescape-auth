package roomescape.domain.time.domain;

import roomescape.domain.theme.domain.Theme;

import static roomescape.utils.DateTimeCheckUtil.isBeforeCheck;
import static roomescape.utils.FormatCheckUtil.startAtFormatCheck;

public class Time {

    private Long id;
    private String date;
    private Theme theme;
    private String startAt;

    public Time(Long id, String date, Theme theme, String startAt) {
        validationCheck(date, startAt);
        this.id = id;
        this.startAt = startAt;
        this.theme = theme;
        this.date = date;
    }

    private static void validationCheck(String date, String startAt) {
        startAtFormatCheck(startAt);
        isBeforeCheck(date, startAt);
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
