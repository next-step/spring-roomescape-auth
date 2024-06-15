package roomescape.apply.reservation.ui.dto;

import roomescape.support.checker.ReservationDateChecker;

public record ReservationRequest(
        String date,
        long timeId,
        long themeId,
        long memberId
) {

    public ReservationRequest {
        ReservationDateChecker.validateDate(date);
    }

}
