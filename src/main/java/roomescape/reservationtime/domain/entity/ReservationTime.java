package roomescape.reservationtime.domain.entity;

import roomescape.reservationtime.ui.dto.ReservationTimeRequest;

public class ReservationTime {
    private Long id;
    private final String startAt;

    private ReservationTime(Long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime of(Long id, String startAt) {
        return new ReservationTime(id, startAt);
    }

    public static ReservationTime from(ReservationTimeRequest request) {
        return new ReservationTime(null, request.startAt());
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
