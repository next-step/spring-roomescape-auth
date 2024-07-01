package roomescape.reservation.ui.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CookieReservationRequest(
        @NotBlank String date,
        @NotNull Long timeId,
        @NotNull Long themeId
) {
}
