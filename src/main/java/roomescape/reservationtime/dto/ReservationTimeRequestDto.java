package roomescape.reservationtime.dto;

import jakarta.validation.constraints.NotBlank;

public class ReservationTimeRequestDto {

    private Long timeId;
    @NotBlank(message = "예약 시간을 입력해주세요")
    private String startAt;

    public ReservationTimeRequestDto(Long timeId) {
        this.timeId = timeId;
    }

    public ReservationTimeRequestDto(String startAt) {
        this.startAt = startAt;
    }

    public ReservationTimeRequestDto(Long timeId, String startAt) {
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
        return "{ " +
                "timeId=" + timeId +
                ", startAt='" + startAt + '\'' +
                '}';
    }
}
