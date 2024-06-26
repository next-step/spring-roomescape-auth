package roomescape.reservationtime.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationTimeResponseDto {

    @JsonProperty("timeId")
    private Long timeId;
    @JsonProperty("startAt")
    private String startAt;

    public ReservationTimeResponseDto(Long timeId, String startAt) {
        this.timeId = timeId;
        this.startAt = startAt;
    }

    public Long getTimeId() {
        return timeId;
    }
    public String getStartAt() {
        return startAt;
    }

    @Override
    public String toString() {
        return "{" +
                " timeId=" + timeId +
                ", startAt='" + startAt + '\'' +
                '}';
    }
}
