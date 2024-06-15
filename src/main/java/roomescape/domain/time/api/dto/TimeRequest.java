package roomescape.domain.time.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import roomescape.domain.time.domain.Time;

public class TimeRequest {

    private final String startAt;


    @JsonCreator
    public TimeRequest(@JsonProperty("startAt") String startAt) {
        this.startAt = startAt;
    }

    public Time convertToDomainObject() {
        return new Time(null, this.startAt);
    }
}
