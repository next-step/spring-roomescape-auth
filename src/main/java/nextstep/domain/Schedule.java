package nextstep.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {

    private Long id;
    private Long themeId;
    private LocalDate date;
    private LocalTime time;

    private Schedule() {
    }

    public Schedule(Long themeId, String date, String time) {
        this(null, themeId, LocalDate.parse(date), LocalTime.parse(time));
    }

    public Schedule(Long id, Long themeId, String date, String time) {
        this(id, themeId, LocalDate.parse(date), LocalTime.parse(time));
    }

    public Schedule(Long id, Long themeId, LocalDate date, LocalTime time) {
        this.id = id;
        this.themeId = themeId;
        this.date = date;
        this.time = time;
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
