package roomescape.domain.time.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import roomescape.domain.time.domain.Time;

public class TimeRequest {

    private final String date;
    private final Long themeId;
    private final String startAt;


    @JsonCreator
    public TimeRequest(@JsonProperty("date") String date, @JsonProperty("themeId") Long themeId, @JsonProperty("startAt") String startAt) {
        this.date = date;
        this.themeId = themeId;
        this.startAt = startAt;
    }

    public String getDate() {
        return date;
    }

    public Long getThemeId() {
        return themeId;
    }

    public String getStartAt() {
        return startAt;
    }
}
