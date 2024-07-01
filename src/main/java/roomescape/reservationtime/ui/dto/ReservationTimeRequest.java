package roomescape.reservationtime.ui.dto;

import jakarta.validation.constraints.NotBlank;

public record ReservationTimeRequest (
    @NotBlank String startAt
) {
    public static ReservationTimeRequest create(String startAt) {
        return new ReservationTimeRequest(startAt);
    }
}
