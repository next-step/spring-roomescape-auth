package roomescape.reservation.ui.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservationRequest(
    @NotNull Long memberId,
    @NotBlank String date,
    @NotNull Long timeId,
    @NotNull Long themeId
) {
    public static ReservationRequest of(Long memberId, String date, Long timeId, Long themeId) {
        return new ReservationRequest(memberId, date, timeId, themeId);
    }

    public static ReservationRequest fromCookieRequest(Long memberId, CookieReservationRequest request) {
        return new ReservationRequest(
                memberId,
                request.date(),
                request.timeId(),
                request.themeId());
    }

    public static ReservationRequest fromAdminRequest(Long memberId, AdminReservationRequest request) {
        return new ReservationRequest(
                memberId,
                request.date(),
                request.timeId(),
                request.themeId());
    }
}
