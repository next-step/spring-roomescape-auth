package roomescape.domain.reservation.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AdminReservationRequest {

    private final String date;
    private final Long timeId;
    private final Long memberId;
    private final Long themeId;

    @JsonCreator
    public AdminReservationRequest(
            @JsonProperty("date") String date,
            @JsonProperty("timeId") Long timeId,
            @JsonProperty("memberId") Long memberId,
            @JsonProperty("themeId") Long themeId) {
        this.date = date;
        this.timeId = timeId;
        this.memberId = memberId;
        this.themeId = themeId;
    }

    public String getDate() {
        return date;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getThemeId() {
        return themeId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
