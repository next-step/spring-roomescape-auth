package roomescape.application.domain.reservationtime.api.dto.response;

import roomescape.domain.reservationtime.ReservationTime;

public class CreateReservationTimeResponse {

    private static final String START_AT_PATTEN = "HH:mm";

    private final Long id;

    private final String startAt;

    public CreateReservationTimeResponse(Long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static CreateReservationTimeResponse from(ReservationTime reservationTime) {
        return new CreateReservationTimeResponse(
                reservationTime.getId().id(),
                reservationTime.getFormatStartAt(START_AT_PATTEN)
        );
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }
}
