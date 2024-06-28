package roomescape.reservation.ui.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminReservationRequest(
        @NotBlank String date,
        @NotNull Long themeId,
        @NotNull Long timeId,
        @NotNull Long memberId
) {
}
