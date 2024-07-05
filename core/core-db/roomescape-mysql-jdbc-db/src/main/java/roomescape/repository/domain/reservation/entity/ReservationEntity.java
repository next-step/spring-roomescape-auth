package roomescape.repository.domain.reservation.entity;

import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.vo.ReservationDate;
import roomescape.domain.reservation.vo.ReservationId;
import roomescape.domain.reservation.vo.ReservationName;
import roomescape.domain.reservationtime.vo.ReservationTimeId;
import roomescape.domain.theme.vo.ThemeId;

import java.time.LocalDate;

public class ReservationEntity {

    private final Long id;
    private final String reservationName;
    private final LocalDate date;
    private final Long reservationTimeId;
    private final Long themeId;

    public ReservationEntity(
            Long id,
            String reservationName,
            LocalDate date,
            Long reservationTimeId,
            Long themeId
    ) {
        this.id = id;
        this.reservationName = reservationName;
        this.date = date;
        this.reservationTimeId = reservationTimeId;
        this.themeId = themeId;
    }

    public static ReservationEntity from(Reservation reservation) {
        return new ReservationEntity(
                reservation.getId().id(),
                reservation.getName().name(),
                reservation.getDate().date(),
                reservation.getTimeId().id(),
                reservation.getThemeId().id()
        );
    }

    public ReservationEntity withId(Long id) {
        return new ReservationEntity(id, this.reservationName, date, this.reservationTimeId, themeId);
    }

    public Long getId() {
        return id;
    }

    public String getReservationName() {
        return this.reservationName;
    }

    public Long getReservationTimeId() {
        return this.reservationTimeId;
    }

    public Long getThemeId() {
        return this.themeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public Reservation toDomain() {
        return Reservation.of(
                new ReservationId(this.id),
                new ReservationName(this.reservationName),
                new ReservationDate(this.date),
                new ReservationTimeId(this.reservationTimeId),
                new ThemeId(this.themeId)
        );
    }
}
