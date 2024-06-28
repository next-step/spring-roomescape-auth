package roomescape.reservation.dto;

public class TimeDto {

    private Long timeId;
    private String startAt;

    public TimeDto(Long timeId, String startAt) {
        this.timeId = timeId;
        this.startAt = startAt;
    }

    public Long getTimeId() {
        return timeId;
    }

    public String getStartAt() {
        return startAt;
    }
}
