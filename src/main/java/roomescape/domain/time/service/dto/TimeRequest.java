package roomescape.domain.time.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeRequest {

    private final String startAt;

    @JsonCreator
    public TimeRequest(@JsonProperty("startAt") String startAt) {

        this.startAt = startAt;
    }

    public String getStartAt() {
        return startAt;
    }
}
