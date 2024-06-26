package roomescape.reservationtime.dto;

public class ReservationTimeResponseDto {

    private Long timeId;
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
