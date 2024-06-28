package roomescape.reservationtime.dto;

import jakarta.validation.constraints.NotBlank;

public class ReservationTimeRequestDto {

    @NotBlank(message = "예약 시간을 입력해주세요")
    private String startAt;

    public ReservationTimeRequestDto() {
    }

    public ReservationTimeRequestDto(String startAt) {
        this.startAt = startAt;
    }

    public String getStartAt() {
        return startAt;
    }
}
