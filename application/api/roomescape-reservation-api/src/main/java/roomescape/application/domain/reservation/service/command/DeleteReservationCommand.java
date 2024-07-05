package roomescape.application.domain.reservation.service.command;

import roomescape.domain.reservation.vo.ReservationId;

public class DeleteReservationCommand {

    private final Long reservationId;

    public DeleteReservationCommand(Long reservationId) {
        this.reservationId = reservationId;
    }

    public ReservationId toReservationId() {
        return new ReservationId(this.reservationId);
    }
}
