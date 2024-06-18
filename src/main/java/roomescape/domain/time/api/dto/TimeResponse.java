package roomescape.domain.time.api.dto;

public class TimeResponse {

    private final Long id;
    private final String date;
    private final String startAt;
    private final Long themeId;

    public TimeResponse(Long id, String date, String startAt, Long themeId) {
        this.id = id;
        this.date = date;
        this.startAt = startAt;
        this.themeId = themeId;
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

    public Long getThemeId() {
        return themeId;
    }
}
