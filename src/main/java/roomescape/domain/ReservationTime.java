package roomescape.domain;

import roomescape.dto.time.ReservationTimeRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.LocalTime;

public class ReservationTime {

    private Long id;
    private LocalTime startAt;

    public boolean isBeforeCurrentTime(LocalDate reservatedDate) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservatedDate, this.startAt);
        return reservationDateTime.isBefore(LocalDateTime.now());
    }

    public static ReservationTime from(ReservationTimeRequest request) {
        return new ReservationTime(request.getStartAt());
    }

    public ReservationTime() {
    }

    public ReservationTime(String startAt) {
        this.startAt = LocalTime.parse(startAt);
    }

    public ReservationTime(Long id, String startAt) {
        this.id = id;
        this.startAt = LocalTime.parse(startAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
