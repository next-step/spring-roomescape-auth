package roomescape.apply.reservation.ui.dto;

import roomescape.support.checker.ReservationDateChecker;

public record ReservationAdminRequest(
        String date,
        long timeId,
        long themeId,
        long memberId
) {

    public ReservationAdminRequest {
        ReservationDateChecker.validateDate(date);
    }

}