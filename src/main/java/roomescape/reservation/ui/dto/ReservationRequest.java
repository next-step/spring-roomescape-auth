package roomescape.reservation.ui.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservationRequest(
    @NotBlank String memberName,
    @NotBlank String date,
    @NotNull Long timeId,
    @NotNull Long themeId
) {
    public static ReservationRequest of(String memberName, String date, Long timeId, Long themeId) {
        return new ReservationRequest(memberName, date, timeId, themeId);
    }

    public static ReservationRequest fromCookieRequest(String memberName, CookieReservationRequest request) {
        return new ReservationRequest(
                memberName,
                request.date(),
                request.timeId(),
                request.themeId());
    }

    public static ReservationRequest fromAdminRequest(String memberName, AdminReservationRequest request) {
        return new ReservationRequest(
                memberName,
                request.date(),
                request.timeId(),
                request.themeId());
    }
}
