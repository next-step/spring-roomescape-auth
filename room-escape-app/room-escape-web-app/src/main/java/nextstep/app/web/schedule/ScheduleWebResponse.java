package nextstep.app.web.schedule;

import nextstep.core.schedule.in.ScheduleResponse;

import java.time.LocalDate;
import java.time.LocalTime;

class ScheduleWebResponse {
    private Long id;
    private Long themeId;
    private LocalDate date;
    private LocalTime time;

    private ScheduleWebResponse() {
    }

    private ScheduleWebResponse(Long id, Long themeId, LocalDate date, LocalTime time) {
        this.id = id;
        this.themeId = themeId;
        this.date = date;
        this.time = time;
    }

    public static ScheduleWebResponse from(ScheduleResponse schedule) {
        return new ScheduleWebResponse(
                schedule.getId(),
                schedule.getThemeId(),
                schedule.getDate(),
                schedule.getTime()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getThemeId() {
        return themeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }
}
